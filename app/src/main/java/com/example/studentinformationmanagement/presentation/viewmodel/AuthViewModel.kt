package com.example.studentinformationmanagement.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.studentinformationmanagement.domain.repository.AuthRepository
import com.example.studentinformationmanagement.domain.usecase.GetCurrentUserUseCase
import com.example.studentinformationmanagement.domain.usecase.LoginUseCase
import com.example.studentinformationmanagement.domain.usecase.LogoutUseCase
import com.example.studentinformationmanagement.domain.usecase.SendResetEmailUseCase
import com.example.studentinformationmanagement.presentation.uistate.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    suspend fun getCurrentUser() {
        val result = GetCurrentUserUseCase(authRepository)()
        if (result.isSuccess) {
            _uiState.value = _uiState.value.copy(user = result.getOrNull(), errorMessage = null)
        } else {
            _uiState.value = _uiState.value.copy(user = null, errorMessage = "Failed to fetch current user")
        }
    }

    suspend fun login(email: String, password: String, rememberMe: Boolean, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email and password must not be empty")
            return
        }

        val result = LoginUseCase(authRepository)(email, password, rememberMe)
        if (result.isSuccess) {
            _uiState.value = _uiState.value.copy(errorMessage = null, user = result.getOrNull())
            onSuccess()
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Wrong email or password",
                user = null
            )
        }
    }

    suspend fun sendResetEmail(email: String, onSuccess: () -> Unit) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email must not be empty")
            return
        }

        val result = SendResetEmailUseCase(authRepository)(email)
        if (result.isSuccess) {
            _uiState.value = _uiState.value.copy(errorMessage = null)
            onSuccess()
        } else {
            _uiState.value = _uiState.value.copy(errorMessage = "Failed to send reset email")
        }
    }

    suspend fun logout(onLoggedOut: () -> Unit) {
        val result = LogoutUseCase(authRepository)()
        if (result.isSuccess) {
            _uiState.value = _uiState.value.copy(user = null, errorMessage = null)
        } else {
            _uiState.value = _uiState.value.copy(errorMessage = "Logout failed")
        }
        onLoggedOut()
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}