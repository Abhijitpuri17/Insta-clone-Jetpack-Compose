package com.example.instagramclone.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SignUpPage(navController: NavController) {
    val context = LocalContext.current

    var email by remember{
        mutableStateOf("")
    }

    var userName by remember {
        mutableStateOf("")
    }

    var password by remember{
        mutableStateOf("")
    }

    var confirmPassword by remember {
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

        OutlinedTextField(value = userName, placeholder = { Text(text = "username") },onValueChange = {
            userName = it
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ) ,colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.LightGray),modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp))



        OutlinedTextField(value = email, placeholder = { Text(text = "email") },onValueChange = {
            email = it
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ) ,colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.LightGray),modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp))

        OutlinedTextField(value = password, placeholder = { Text(text = "password") }, onValueChange = {
            password = it
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Blue) ,modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp))

        OutlinedTextField(value = confirmPassword, placeholder = { Text(text = "password") }, onValueChange = {
            confirmPassword = it
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Blue) ,modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp))


        Button(onClick = {
            if(userName.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else if(confirmPassword == password)
                signUp(email, password, userName ,context, navController)
            else {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xAA0047AB)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(20)
        ) {
            Text(text = "Sign Up", fontSize = 18.sp,color = Color(0xAAFFFFFF), modifier = Modifier.padding(vertical = 3.dp))
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(top=64.dp), color=Color.Gray, thickness = 1.dp)

        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.Center) {
            Text(text = "Already have an account?")
            Text(text="Log In", fontWeight = FontWeight.Bold, modifier = Modifier.clickable {
                navController.navigate("log_in")
            })
        }

    }
}

fun signUp(email: String, password: String, userName: String ,context: Context, navController: NavController)  {
    val firebaseAuthInstance = FirebaseAuth.getInstance()

    firebaseAuthInstance.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener {
            Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_LONG).show()

            val db = Firebase.firestore

            val user = HashMap<String, Any>()
            user["id"] = it.user!!.uid
            user["email"] = email
            user["userName"] = userName
            user["profilePic"] = ""
            user["followers"] = mutableListOf<String>()
            user["following"] = mutableListOf<String>()
            user["postIDs"] = mutableListOf<String>()

            db.collection("users")
                .document(it.user!!.uid)
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(context, "User Info saved successfully", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
        .addOnFailureListener {
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
        }

}



