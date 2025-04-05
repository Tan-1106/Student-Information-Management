package com.example.studentinformationmanagement

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.ui.admin.AddUser
import com.example.studentinformationmanagement.ui.admin.AdminScreen
import com.example.studentinformationmanagement.ui.manager.StudentManagement
import com.example.studentinformationmanagement.ui.shared.LoginScreen

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.AdminScreen.name
    ) {
        composable(route = AppScreen.Login.name) {
            LoginScreen(navController = navController)
        }
        composable(route = AppScreen.UserManagement.name) {

        }
        composable(route = AppScreen.AddUser.name) {
            AddUser()
        }
        composable(route = AppScreen.EditUser.name) {

        }
        composable(route = AppScreen.LoginHistory.name) {

        }
        composable(route = AppScreen.StudentManagement.name) {
            StudentManagement(navController = navController)
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
        composable (route = AppScreen.AdminScreen.name){
            AdminScreen(screenNavController = navController)
        }
    }
}