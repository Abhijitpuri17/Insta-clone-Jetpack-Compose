package com.example.instagramclone.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun Profile(navController: NavController){
    Card(modifier = Modifier
        .padding(16.dp)
        .wrapContentSize(),
        elevation = 20.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.android_toy_image), contentDescription = "android toy image",
                modifier = Modifier
                    .size(150.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .padding(8.dp)
            )

            Text(text = "Abhijit Puri", fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight.Bold)

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp), horizontalArrangement =
            Arrangement.SpaceEvenly) {
                Column {
                    Text(text = "2", color = Color.Black)
                    Text(text = "Followers", color = Color.Black,fontWeight = FontWeight.Bold)
                }

                Column {
                    Text(text = "3", color = Color.Black)
                    Text(text = "Following", fontWeight = FontWeight.Bold,color = Color.Black)
                }
            }

            Button(modifier = Modifier.padding(top = 16.dp), onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("log_in")
            }) {
                Text(text = "Logout", modifier = Modifier.padding(4.dp))

            }

        }
    }
}
