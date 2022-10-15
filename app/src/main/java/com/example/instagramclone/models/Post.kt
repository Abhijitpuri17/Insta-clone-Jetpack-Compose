package com.example.instagramclone.models

data class Post(
    val id: String,
    val userID: String,
    val imageLink: String,
    val caption : String,
    val likes: MutableList<String>,
    val userName: String
)
