package com.example.studentinformationmanagement.ui.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.shared.LoginRepository
import com.example.studentinformationmanagement.data.shared.LoginUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.mindrot.jbcrypt.BCrypt

class LoginViewModel(
    private val loginRepository: LoginRepository = LoginRepository()
) : ViewModel() {
    // _uiState được dùng để chỉnh sửa dữ liệu, chỉ có thể gọi trong class ViewModel.
    private val _loginUiState = MutableStateFlow(LoginUiState())

    // Các hàm xử lý dữ liệu và xử lý sự kiện Login viết tại đây
    var userUsernameInput by mutableStateOf("")
        private set
    var userPasswordInput by mutableStateOf("")
        private set
    fun onUsernameChange(userInput: String) {
        userUsernameInput = userInput
    }
    fun onPasswordChange(userInput: String) {
        userPasswordInput = userInput
    }

    var isPasswordShowing by mutableStateOf(false)
        private set
    fun onPasswordVisibilityChange() {
        isPasswordShowing = !isPasswordShowing
    }

    fun onLoginButtonClicked(
        navController: NavController
    ) {
        viewModelScope.launch {
            _loginUiState.value = _loginUiState.value.copy(isLoading = true)

            val result = loginRepository.login(userUsernameInput, userPasswordInput)

            if (result.isSuccess) {
                val currentUser = result.getOrNull()
                _loginUiState.value = _loginUiState.value.copy(
                    isLoading = false,
                    currentUser = currentUser,
                    errorMessage = null
                )

                when (currentUser?.userRole) {
                    "Admin" -> navController.navigate(AppScreen.AdminScreen.name)
                    "Manager", "Employee" -> navController.navigate(AppScreen.StudentManagement.name)
                    else -> _loginUiState.value =
                        _loginUiState.value.copy(errorMessage = "Invalid role!")
                }
            } else {
                _loginUiState.value = _loginUiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed."
                )
            }
        }
    }


}