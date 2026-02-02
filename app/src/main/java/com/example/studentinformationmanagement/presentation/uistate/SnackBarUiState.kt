package com.example.studentinformationmanagement.presentation.uistate

import com.example.studentinformationmanagement.presentation.ui.shared.SnackBarType

data class SnackBarUiState(
    val message: String? = null,
    val type: SnackBarType
)