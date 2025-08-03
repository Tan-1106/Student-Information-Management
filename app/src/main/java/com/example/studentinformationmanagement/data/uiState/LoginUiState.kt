package com.example.studentinformationmanagement.data.uiState

data class CurrentUser(
    val imageUrl: String = "",
    val name: String = "",
    val birthday: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "",
    val status: String = ""
)

data class LoginHistory(
    val loginTime: Long = 0L,
    val imageUrl: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
)

data class LoginUiState(
    val currentUser: CurrentUser? = null,
    val loginHistoryList: List<LoginHistory> = emptyList()
)
