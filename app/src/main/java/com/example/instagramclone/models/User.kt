package com.example.instagramclone.models

data class User(
    val id: String,
    val userName: String,
    val email: String,
    val profilePic: String,
    val followers: MutableList<String>,
    val following: MutableList<String>,
    val postIDs: MutableList<String>
)
