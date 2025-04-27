package com.example.studentinformationmanagement

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.ui.admin.AddUser
import com.example.studentinformationmanagement.ui.admin.AdminScreen
import com.example.studentinformationmanagement.ui.admin.AdminViewModel
import com.example.studentinformationmanagement.ui.manager.AddStudent
import com.example.studentinformationmanagement.ui.manager.ManagerViewModel
import com.example.studentinformationmanagement.ui.manager.StudentManagement
import com.example.studentinformationmanagement.ui.shared.LoginScreen
import com.example.studentinformationmanagement.ui.shared.LoginViewModel
import com.example.studentinformationmanagement.ui.shared.StudentDetailProfile
import com.example.studentinformationmanagement.ui.shared.UserDetailProfile

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
    StudentDetailProfile,
    AdminScreen
}

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel(),
    managerViewModel: ManagerViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.name
    ) {
        // Login Screen
        composable(route = AppScreen.Login.name) {
            LoginScreen(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }


        // All Main Screens
        composable(route = AppScreen.AdminScreen.name) {
            AdminScreen(
                loginViewModel = loginViewModel,
                adminViewModel = adminViewModel,
                managerViewModel = managerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.StudentManagement.name) {
            StudentManagement(
                navController = navController,
                managerViewModel = managerViewModel
            )
        }

        // Admin Features
        composable(route = AppScreen.AddUser.name) {
            AddUser(
                adminViewModel = adminViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.EditUser.name) {

        }
        composable(route = AppScreen.LoginHistory.name) {

        }


        // Manager Features
        composable(route = AppScreen.AddStudent.name) {
            AddStudent(
                managerViewModel = managerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.EditStudent.name) {

        }
        composable(route = AppScreen.StudentDetailProfile.name) {
            StudentDetailProfile(
                navController = navController,
                managerViewModel = managerViewModel
            )
        }
        composable(route = AppScreen.AddCertificate.name) {

        }
        composable(route = AppScreen.EditCertificate.name) {

        }


        // General Features
        composable(route = AppScreen.UserDetailProfile.name) {
            UserDetailProfile(
                loginViewModel = loginViewModel,
                adminViewModel = adminViewModel,
                navController = navController,
            )
        }
    }
}