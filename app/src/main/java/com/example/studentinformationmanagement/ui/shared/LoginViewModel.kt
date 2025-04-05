package com.example.studentinformationmanagement.ui.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.studentinformationmanagement.data.shared.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    // _uiState được dùng để chỉnh sửa dữ liệu, chỉ có thể gọi trong class ViewModel.
    private val _uiState = MutableStateFlow(LoginUiState())
    // uiState được dùng để lấy dữ liệu cho việc hiển thị, có thể gọi ở các file bên ngoài.
    val uiState: StateFlow<LoginUiState> =_uiState.asStateFlow()

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
        // Xử lý sự kiện đăng nhập và dùng navController để navigate đến các trang khác trong AppScreen.kt


    }
}