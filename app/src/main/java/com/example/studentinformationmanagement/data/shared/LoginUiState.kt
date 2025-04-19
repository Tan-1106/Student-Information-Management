package com.example.studentinformationmanagement.data.shared

data class CurrentUser(
    val uid: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userPhoneNumber: String = "",
    val userRole: String = "Student"
)

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: CurrentUser? = null
)
