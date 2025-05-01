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

data class LoginHistory(
    val loginTime: Long = 0L,
    val userImageUrl: String = "",
    val userName: String = "",
    val userPhoneNumber: String = "",
    val userRole: String = "",
)

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: CurrentUser? = null,
    val loginHistoryList: List<LoginHistory> = emptyList()
)
