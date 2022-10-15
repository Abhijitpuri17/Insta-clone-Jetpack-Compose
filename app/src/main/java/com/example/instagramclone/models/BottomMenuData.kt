package com.example.instagramclone.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomMenuData(
    val icon : ImageVector,
    val title:  String,
    val route : String
){
    object Home : BottomMenuData(
        icon = Icons.Filled.Home,
        title = "Home",
        route = "home"
    )
    object FindPeople : BottomMenuData(
        icon = Icons.Filled.Favorite,
        title = "Find People",
        route = "find_people"
    )
    object Profile : BottomMenuData(
        icon = Icons.Filled.Person,
        title = "Profile",
        route = "profile"
    )
}