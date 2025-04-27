package com.example.studentinformationmanagement.ui.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue

@Composable
fun EditUser(
    adminViewModel: AdminViewModel,
    userPhoneNumber: String,
    navController: NavHostController
) {
    val adminUiState by adminViewModel.uiState.collectAsState()
    val user = adminUiState.userList.find { it.userPhoneNumber == userPhoneNumber }
    

}