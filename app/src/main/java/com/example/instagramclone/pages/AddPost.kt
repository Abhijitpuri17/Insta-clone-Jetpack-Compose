package com.example.instagramclone.pages

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.instagramclone.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddPost(navController: NavController) {

    var imageUri by remember{
        mutableStateOf(Uri.EMPTY)
    }

    var imageUrl : String? = null


    val permissionState = rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)


    val context = LocalContext.current

    var caption by remember{
        mutableStateOf("")
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it!!
    }

    if(permissionState.hasPermission)
    {

    }
    else if(permissionState.shouldShowRationale){
        Text("Reading external storage permission is required by this app")
        Button(onClick = {
            permissionState.launchPermissionRequest()
        }) {
            Text(text = "Allow permission")
        }
    } else {
        Text("Permission fully denied. Go to settings to allow permission")
    }

    Column() {

        Box(modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)) {
            Image(painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(imageUri).build()
            ),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .border(width = 1.dp, color = Color.Gray),
                contentScale = ContentScale.Crop
            )
        }

        OutlinedTextField(value = caption, maxLines = 10,placeholder = { Text(text = "username") },onValueChange = {
            caption = it
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ) ,colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.LightGray),modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp))

        Button(
            onClick = {
                if(permissionState.hasPermission)
                galleryLauncher.launch("image/*")
                else {
                    Toast.makeText(context,
                        "Reading External Storage Permission is denied",
                        Toast.LENGTH_SHORT).show()
                }
                      }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xAA0047AB)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 8.dp)) {
            Text("Select Image")
        }

        Button(onClick = {
                         saveUserImageInFirebaseStorage(navController,context, imageUri, caption)
        },colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xAA0047AB)), modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp, vertical = 8.dp)) {
            Text("Post Image")
        }

    }



}

fun getFileExtension(context: Context, uri: Uri?) : String?
{
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri!!))
}

private fun saveUserImageInFirebaseStorage(navController: NavController,context: Context, imageUri: Uri?, caption: String) {
    val imageExtension = getFileExtension(
        context,
        imageUri
    ) //MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(userImageUri))


    val ref = FirebaseStorage.getInstance()
        .reference.child(
            "posts" +
                    System.currentTimeMillis() + "." +
                    imageExtension
        )

    ref.putFile(imageUri!!)
        .addOnSuccessListener {
            it.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { url ->
                    val imageURL = url!!.toString()

                    val fireStoreInstance = Firebase.firestore

                    fireStoreInstance.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .get().addOnSuccessListener { a ->

                            val map = HashMap<String, Any>()

                            map["userID"] = FirebaseAuth.getInstance().currentUser!!.uid
                            map["userName"] = a["userName"].toString()

                            map["imageLink"] = imageURL
                            map["caption"] = caption
                            map["likes"] = mutableListOf<String>()

                            val id = fireStoreInstance.collection("posts")
                                .document()
                                .id

                            map["id"] = id

                            fireStoreInstance.collection("posts")
                                .document(id)
                                .set(map)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Posted successfully!", Toast.LENGTH_LONG)
                                        .show()


                                    fireStoreInstance
                                        .collection("users")
                                        .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                        .get()
                                        .addOnSuccessListener {
                                            val postIDs = it["postIDs"] as MutableList<String>
                                            postIDs.add(id)

                                            val mapp = HashMap<String, Any>()
                                            mapp["postIDs"] = postIDs

                                            fireStoreInstance
                                                .collection("users")
                                                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                                .update(mapp)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Posted successfully!",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    navController.popBackStack()
                                                    navController.navigate("home_page")
                                                }
                                        }
                                        .addOnFailureListener { ittt ->
                                            Toast.makeText(
                                                context,
                                                ittt.localizedMessage,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                    navController.popBackStack()
                                }




                        }.addOnFailureListener { itt ->
                            Toast.makeText(context, itt.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { itt ->
                    Toast.makeText(context, itt.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
        .addOnFailureListener {
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
}




