package com.example.instagramclone.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.components.HomeAppBar
import com.example.instagramclone.components.HomeBottomMenu

@Composable
fun InstagramApp(navController: NavHostController) {
    val homeNavController = rememberNavController()
    val scrollState = rememberScrollState()

    MainScreen(homeNavController = homeNavController, navController=navController,scrollState = scrollState)
}

@Composable
fun MainScreen(homeNavController: NavHostController,navController: NavController ,scrollState: ScrollState) {
    Scaffold(topBar = {HomeAppBar(navController)},
        bottomBar = { HomeBottomMenu(homeNavController)}) {
       Navigation(navController1 = navController,navController = homeNavController, scrollState = scrollState)
    }
}

@Composable
fun Navigation(navController1: NavController,navController: NavHostController, scrollState: ScrollState)
{
    NavHost(navController = navController, startDestination = "home"){
        bottomNavigation(navController = navController)

        composable("home"){
            Home(navController = navController)
        }

        composable("find_people"){
            FindPeople(navController = navController)
        }

        composable("profile"){
            Profile(navController = navController1)
        }

    }

}

fun NavGraphBuilder.bottomNavigation(navController: NavController)
{
    composable("home"){
        Home(navController = navController)
    }
    composable("find_people"){
        FindPeople(navController = navController)
    }
    composable("profile"){
        Profile(navController = navController)
    }
}




@Preview
@Composable
fun InstagramAppPreview(){
    InstagramApp(navController = NavHostController(LocalContext.current))
}
