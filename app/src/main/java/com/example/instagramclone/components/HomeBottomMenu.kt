package com.example.instagramclone.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.instagramclone.R
import com.example.instagramclone.models.BottomMenuData

@Composable
fun HomeBottomMenu(navController: NavController)
{
    val menuItems = listOf(
        BottomMenuData.Home,
        BottomMenuData.FindPeople,
        BottomMenuData.Profile
    )

    BottomNavigation(contentColor = colorResource(id = R.color.white)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = navBackStackEntry?.destination?.route

        menuItems.forEach{
            BottomNavigationItem(
                label = { Text(text = it.title) },
                alwaysShowLabel = true,
                selected = currentRoute == it.route,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                onClick = {
                    navController.navigate(it.route){
                    }
                },
                icon = { Icon(it.icon, contentDescription = "") }
            )
        }

    }
}
