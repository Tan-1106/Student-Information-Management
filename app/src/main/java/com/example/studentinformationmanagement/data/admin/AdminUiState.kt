package com.example.studentinformationmanagement.data.admin

data class User(
    val userImageUrl: String,
    val userName: String,
    val userAge: String,
    val userEmail: String,
    val userPhoneNumber: String,
    val userRole: String,
    val userState: String,
)

data class AdminUiState(
    val userList: List<User> = emptyList()
)
