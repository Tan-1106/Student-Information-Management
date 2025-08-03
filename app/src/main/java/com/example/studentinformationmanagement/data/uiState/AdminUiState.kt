package com.example.studentinformationmanagement.data.uiState

// All User Basic Information
data class User(
    val imageUrl: String = "https://drive.google.com/uc?id=1XGMQWOMTV5lxHGLpYQMk--3Zqm7-iEtK",
    val name: String = "",
    val birthday: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "",
    val status: String = ""
)

// User List To Show
data class AdminUiState(
    val userList: List<User> = emptyList()
)
