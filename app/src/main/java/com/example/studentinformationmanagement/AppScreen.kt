package com.example.studentinformationmanagement

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.ui.shared.LoginScreen
import com.example.studentinformationmanagement.ui.shared.LoginViewModel

enum class AppScreen() {
    Login,
    UserManagement,
    AddUser,
    EditUser,
    LoginHistory,
    StudentManagement,
    AddStudent,
    EditStudent,
    AddCertificate,
    EditCertificate,
    UserDetailProfile,
    StudentDetailProfile
}

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.name
    ) {
        composable(route = AppScreen.Login.name) {
            LoginScreen()
        }
        composable(route = AppScreen.UserManagement.name) {

        }
        composable(route = AppScreen.AddUser.name) {

        }
        composable(route = AppScreen.EditUser.name) {

        }
        composable(route = AppScreen.LoginHistory.name) {

        }
        composable(route = AppScreen.StudentManagement.name) {

        }
        composable(route = AppScreen.AddStudent.name) {

        }
        composable(route = AppScreen.EditStudent.name) {

        }
        composable(route = AppScreen.AddCertificate.name) {

        }
        composable(route = AppScreen.EditCertificate.name) {

        }
        composable(route = AppScreen.UserDetailProfile.name) {

        }
        composable(route = AppScreen.StudentDetailProfile.name) {

        }
    }
}