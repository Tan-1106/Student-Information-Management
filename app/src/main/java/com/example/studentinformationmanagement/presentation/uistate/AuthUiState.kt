package com.example.studentinformationmanagement.presentation.uistate

import com.example.studentinformationmanagement.domain.model.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null,
)