package com.example.instagramclone.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun LoginPage(navController: NavController){

    val context = LocalContext.current

    var email by remember{
        mutableStateOf("")
    }

    var password by remember{
        mutableStateOf("")
    }


    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {

        Text(text = "Instagram",
            textAlign= TextAlign.Center,
            fontFamily = FontFamily.Cursive,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = email, placeholder = { Text(text = "email")},onValueChange = {
            email = it
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ) ,colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.LightGray),modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp))

        OutlinedTextField(value = password, placeholder = { Text(text = "password")}, onValueChange = {
            password = it
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Blue) ,modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp))
        Button(onClick = {
                         login(email, password, context, navController)
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xAA0047AB)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(20)
            ) {
            Text(text = "LogIn", fontSize = 18.sp,color = Color(0xAAFFFFFF), modifier = Modifier.padding(vertical = 3.dp))
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(top=64.dp), color=Color.Gray, thickness = 1.dp)

        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.Center) {
            Text(text = "Don't have an account? ", fontSize = 12.sp)
            Text(text="Sign Up",fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable {
                navController.navigate("sign_up")
            })
        }

    }


}

fun login(email: String, password: String, context: Context, navController: NavController)  {
    val firebaseAuthInstance = FirebaseAuth.getInstance()

    firebaseAuthInstance.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener {
            Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
            navController.navigate("home_page")
        }
        .addOnFailureListener {
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
        }

}
