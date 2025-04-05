package com.example.studentinformationmanagement.ui.admin

import androidx.lifecycle.ViewModel
import com.example.studentinformationmanagement.data.admin.AdminUiState
import com.example.studentinformationmanagement.data.shared.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminViewModel : ViewModel() {
    // _uiState được dùng để chỉnh sửa dữ liệu, chỉ có thể gọi trong class ViewModel.
    private val _uiState = MutableStateFlow(AdminUiState())
    // uiState được dùng để lấy dữ liệu cho việc hiển thị, có thể gọi ở các file bên ngoài.
    val uiState: StateFlow<AdminUiState> =_uiState.asStateFlow()

    fun onUserEditClicked(userPhoneNumber: String) {

    }

    fun onUserSeeMoreClicked(userPhoneNumber: String) {

    }
}