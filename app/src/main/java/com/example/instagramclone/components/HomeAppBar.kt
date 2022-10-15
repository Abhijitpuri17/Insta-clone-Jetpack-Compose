package com.example.instagramclone.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeAppBar(navController: NavController) {
    TopAppBar(title = { Text("Instagram",
        fontFamily = FontFamily.Cursive,
        fontSize = 36.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(start = 12.dp)
    ) },  actions = {
        IconButton(onClick = {
            navController.navigate("add_post")
        }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "",
                modifier = Modifier.size(54.dp).padding(6.dp))
        }
    })
}