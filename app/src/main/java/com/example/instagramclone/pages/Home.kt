package com.example.instagramclone.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun Home(navController: NavController){


    val context = LocalContext.current

    var flag by remember {
        mutableStateOf(false)
    }

    var postsList by remember {
        mutableStateOf(mutableListOf<Post>())
    }

    var followingIDs = mutableListOf<String>()

    val fireStoreInstance = Firebase.firestore

    var posts = mutableListOf<Post>()


    fireStoreInstance
        .collection("users")
        .document(FirebaseAuth.getInstance().currentUser!!.uid)
        .get()
        .addOnSuccessListener {

            followingIDs = it["following"] as MutableList<String>

            followingIDs.add("000")

            fireStoreInstance
                .collection("posts")
                .whereIn("userID", followingIDs)
                .get()
                .addOnSuccessListener {itt ->
                    val newPostsList = mutableListOf<Post>()
                    for(doc in itt.documents) {

                        Log.d("image link", doc["imageLink"].toString())

                        val post = Post(doc["id"].toString(),
                            doc["userID"].toString(),
                            doc["imageLink"].toString(),
                            doc["caption"].toString(),
                            doc["likes"] as MutableList<String>,
                            doc["userName"].toString()
                            )
                        newPostsList.add(post)
                    }
                    postsList = newPostsList
                    flag = true
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
        .addOnFailureListener {
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
        }


    if(flag)
    LazyColumn(modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)){
        items(postsList){ post->
            PostItem(post)
        }
    } else {
        Text("Loading Posts")
    }
}

@Composable
fun PostItem(post: Post) {

    var liked by remember {
        mutableStateOf(false)
    }


    val fireStoreInstance = Firebase.firestore

    fireStoreInstance
        .collection("posts")
        .document(post.id)
        .get()
        .addOnSuccessListener {
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

            val likes = it["likes"] as MutableList<String>

            if(currentUserId in likes){
                liked = !liked
            }
        }

    Box(
        Modifier
            .wrapContentHeight()
            .padding(4.dp)) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(painter = painterResource(id = R.drawable.android_toy_image),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                        .border(width = 1.dp, color = Color.Red, shape = CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Text(text = post.userName, fontSize = 18.sp,modifier = Modifier.padding(start = 8.dp))
            }

            Box(modifier = Modifier.padding(8.dp)) {
                Image(painter = rememberAsyncImagePainter(post.imageLink),
                    contentDescription = "", modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(width = 0.01.dp, color = Color.DarkGray)
                        .padding(2.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

            Text(text = post.caption, modifier = Modifier.padding(start = 8.dp))

            Row {
                IconButton(onClick = {

                    fireStoreInstance
                        .collection("posts")
                        .document(post.id)
                        .get()
                        .addOnSuccessListener {
                            val likes = it["likes"] as MutableList<String>

                            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                            if(liked)
                                likes.add(currentUserId)
                            else {
                                likes.remove(currentUserId)
                            }

                            val map = HashMap<String, Any>()
                            map["likes"] = likes

                            fireStoreInstance
                                .collection("posts")
                                .document(post.id)
                                .update(map).addOnSuccessListener {
                                    liked = !liked
                                }
                        }
                }) {
                    if(liked) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite, "",
                            Modifier
                                .size(30.dp)
                                .width(1.dp),
                            tint= Color.Red
                        )
                    }
                    else {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder, "",
                            Modifier
                                .size(30.dp)
                                .width(1.dp)
                        )
                    }
                }
            }

            
        }
    }
}

