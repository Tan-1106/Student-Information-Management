package com.example.studentinformationmanagement.ui.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentinformationmanagement.ui.theme.MyPurple

@Composable
fun LoginScreen(
    // Gọi ViewModel của trang để sử dụng
    loginViewModel: LoginViewModel = viewModel()
) {
    // Gọi UiState của trang để sử dụng
    val loginUiState by loginViewModel.uiState.collectAsState()

    // Giao diện của LoginScreen được viết tại đây và được xử lý bằng các hàm được gọi từ loginViewModel

}