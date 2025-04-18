package com.example.studentinformationmanagement.data.admin

// All User Basic Information
data class User(
    val userImageUrl: String,
    val userName: String,
    val userBirthday: String,
    val userEmail: String,
    val userPhoneNumber: String,
    val userRole: String,
    val userStatus: String
)

// User List To Show
data class AdminUiState(
    val userList: List<User> = emptyList()
)
