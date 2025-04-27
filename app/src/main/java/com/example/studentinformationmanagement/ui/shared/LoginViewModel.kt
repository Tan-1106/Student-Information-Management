package com.example.studentinformationmanagement.ui.shared

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.shared.LoginRepository
import com.example.studentinformationmanagement.data.shared.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository = LoginRepository()
) : ViewModel() {
    // UiState
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    // User's phone number input
    var userPhoneNumberInput by mutableStateOf("")
        private set
    fun onPhoneNumberChange(userInput: String) {
        userPhoneNumberInput = userInput
    }

    // User's password input
    var userPasswordInput by mutableStateOf("")
        private set
    fun onPasswordChange(userInput: String) {
        userPasswordInput = userInput
    }

    // Showing or hiding the password text field
    var isPasswordShowing by mutableStateOf(false)
        private set
    fun onPasswordVisibilityChange() {
        isPasswordShowing = !isPasswordShowing
    }

    // Login button event
    fun onLoginButtonClicked(
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            _loginUiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            val result = loginRepository.login(userPhoneNumberInput, userPasswordInput)

            if (result.isSuccess) {
                val currentUser = result.getOrNull()
                _loginUiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        currentUser = currentUser,
                        errorMessage = null
                    )
                }

                when (currentUser?.userRole) {
                    "Admin" -> navController.navigate(AppScreen.AdminScreen.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                    "Manager" -> navController.navigate(AppScreen.StudentManagement.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                    "Employee" -> navController.navigate(AppScreen.StudentManagement.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                }
            } else {
                _loginUiState.value = _loginUiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed."
                )
                Toast.makeText(context, "Wrong phone number or password.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onLogOutButtonClicked() {
        userPhoneNumberInput = ""
        userPasswordInput = ""

        viewModelScope.launch {
            _loginUiState.update { currentState ->
                currentState.copy(
                    currentUser = null,
                    errorMessage = null
                )
            }
        }
    }
}