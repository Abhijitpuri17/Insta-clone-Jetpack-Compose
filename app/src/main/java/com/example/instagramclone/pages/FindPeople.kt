package com.example.instagramclone.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

@Composable
fun FindPeople(navController: NavController){

    Log.d("Start", "Find People Composable called")

    var flag by remember {
        mutableStateOf(false)
    }

    var usersList by remember {
        mutableStateOf(mutableListOf<User>())
    }

        val fireStoreInstance = Firebase.firestore

        val followersSet = HashSet<String>()

    fireStoreInstance
        .collection("users")
        .document(FirebaseAuth.getInstance().currentUser!!.uid)
        .get()
        .addOnSuccessListener {
            val followersList = it["following"] as MutableList<String>
            for(following in followersList){
                followersSet.add(following)
            }

            fireStoreInstance
                .collection("users")
                .get()
                .addOnSuccessListener {
                    Log.d("GET Success", "Successsssssssss ${it.documents.size}")
                    val newList = mutableListOf<User>()
                    for(doc in it.documents){
                        if(followersSet.contains(doc["id"].toString())){
                            continue
                        }
                        Log.d("Doc", doc.toString())

                        val user = User(doc["id"].toString(),doc["userName"].toString() ,doc["email"].toString(),
                            doc["profilePic"].toString(), doc["followers"] as MutableList<String>,
                            doc["following"] as MutableList<String>, doc["postIDs"] as MutableList<String>
                        )
                        newList.add(user)
                    }
                    flag = true
                    usersList = newList
                    Log.d("USer list size after", usersList.size.toString())
                }

        }


    if(flag)
    LazyColumn {
        items(usersList){ user ->
            FindPersonItem(user)
        }
    }
    else {
        Text(text = "Loading...")
    }
}


@Composable
fun FindPersonItem(user: User){

    var buttonText by remember {
        mutableStateOf("Follow")
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.android_toy_image),
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
            )
            
            //Image(painter= rememberAsyncImagePainter(user.profilePic), contentDescription = "")

            Text(text = user.userName, fontSize = 24.sp, modifier = Modifier.padding(start = 8.dp))
        }

        Button(onClick = {
            if(buttonText == "Following"){
                return@Button
            }

            val toFollowUserID = user.id
            val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid

            val fireStoreInstance = Firebase.firestore

            fireStoreInstance
                .collection("users")
                .document(toFollowUserID)
                .get()
                .addOnSuccessListener {

                    Log.d("AAA", it.toString())

                    val list = it["followers"] as MutableList<String>

                    list.add(currentUserID)

                    val map = HashMap<String, Any>()

                    map["followers"] = list

                    fireStoreInstance
                        .collection("users")
                        .document(toFollowUserID)
                        .update(map)
                        .addOnSuccessListener {
                            Log.d("Following", "Added to following list")
                        }
                        .addOnFailureListener {
                            Log.d("Failed", "Could not add to Follow list")
                        }
                }
                .addOnFailureListener {
                    Log.d("Failed", "Could not add to follow list")
                }

            fireStoreInstance
                .collection("users")
                .document(currentUserID)
                .get()
                .addOnSuccessListener {
                    val list = it["following"] as MutableList<String>

                    list.add(toFollowUserID)

                    val map = HashMap<String, Any>()

                    map["following"] = list

                    buttonText = "Following"


                    fireStoreInstance
                        .collection("users")
                        .document(currentUserID)
                        .update(map)
                        .addOnSuccessListener {
                            Log.d("Following", "Added to following list")
                        }
                        .addOnFailureListener {
                            Log.d("Failed", "Could not add to following")
                        }
                }
                .addOnFailureListener {
                    Log.d("Failed", "Could not add to following")
                }

        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue), shape = RoundedCornerShape(10)) {
            Text(text = buttonText, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
        }
    }
}
