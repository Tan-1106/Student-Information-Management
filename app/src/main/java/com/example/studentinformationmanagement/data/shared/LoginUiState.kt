package com.example.studentinformationmanagement.data.shared

data class CurrentUser(
    val userImageUrl: String = "",
    val userName: String = "",
    val userBirthday: String = "",
    val userEmail: String = "",
    val userPhoneNumber: String = "",
    val userRole: String = "",
    val userStatus: String = ""
)

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: CurrentUser? = null
)
