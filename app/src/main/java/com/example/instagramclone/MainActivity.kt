package com.example.instagramclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.pages.*
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var startDest = "log_in"

        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            startDest = "home_page"
        }
        setContent {
            InstagramCloneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    Navigation(navController = navController, startDest)
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController, startDest: String) {
    NavHost(navController = navController, startDestination = startDest){

        composable("home_page"){
            InstagramApp(navController = navController)
        }

        composable("log_in"){
            LoginPage(navController = navController)
        }

        composable("add_post"){
            AddPost(navController = navController)
        }

        composable("sign_up"){
            SignUpPage(navController=navController)
        }

    }
}




