package com.example.studentinformationmanagement.ui.shared

import androidx.lifecycle.ViewModel
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

}