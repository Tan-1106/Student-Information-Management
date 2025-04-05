package com.example.studentinformationmanagement.ui.manager

import androidx.lifecycle.ViewModel
import com.example.studentinformationmanagement.data.manager.ManagerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ManagerViewModel : ViewModel() {
    // _uiState được dùng để chỉnh sửa dữ liệu, chỉ có thể gọi trong class ViewModel.
    private val _uiState = MutableStateFlow(ManagerUiState())
    // uiState được dùng để lấy dữ liệu cho việc hiển thị, có thể gọi ở các file bên ngoài.
    val uiState: StateFlow<ManagerUiState> =_uiState.asStateFlow()

    fun onUserEditClicked(userPhoneNumber: String) {

    }

    fun onUserSeeMoreClicked(userPhoneNumber: String) {

    }
}