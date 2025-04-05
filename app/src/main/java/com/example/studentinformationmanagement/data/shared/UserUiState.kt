package com.example.studentinformationmanagement.data.shared

import android.media.Image

data class User(
    val userImageUrl: String,
    val userName: String,
    val userAge: String,
    val userEmail: String,
    val userPhoneNumber: String,
    val userRole: String,
    val userState: String,
)

data class UserUiState(
    val userList: List<User> = emptyList()
)