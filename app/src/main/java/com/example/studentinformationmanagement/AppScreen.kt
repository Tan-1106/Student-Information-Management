package com.example.studentinformationmanagement

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.ui.auth.ForgotPassword
import com.example.studentinformationmanagement.ui.auth.LoginScreen
import com.example.studentinformationmanagement.ui.home.AdminScreen
import com.example.studentinformationmanagement.ui.home.EmployeeScreen
import com.example.studentinformationmanagement.ui.home.ManagerScreen
import com.example.studentinformationmanagement.ui.student.AddStudent
import com.example.studentinformationmanagement.ui.student.EditStudent
import com.example.studentinformationmanagement.ui.student.StudentDetailProfile
import com.example.studentinformationmanagement.ui.student.StudentManagement
import com.example.studentinformationmanagement.ui.student.certificate.AddCertificate
import com.example.studentinformationmanagement.ui.student.certificate.CertificateDetail
import com.example.studentinformationmanagement.ui.student.certificate.CertificateList
import com.example.studentinformationmanagement.ui.student.certificate.EditCertificate
import com.example.studentinformationmanagement.ui.user.AddUser
import com.example.studentinformationmanagement.ui.user.AdminDetailProfile
import com.example.studentinformationmanagement.ui.user.EditUser
import com.example.studentinformationmanagement.ui.user.LoginHistory
import com.example.studentinformationmanagement.ui.viewModel.AdminViewModel
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

enum class AppScreen {
    Login, ForgotPassword,
    AdminScreen, UserManagement, AddUser, EditUser, LoginHistory,
    ManagerScreen, StudentManagement, StudentDetailProfile, AddStudent, EditStudent, CertificateList, AddCertificate, EditCertificate, CertificateDetail,
    EmployeeScreen, UserDetailProfile,
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
        composable(route = AppScreen.ForgotPassword.name) {
            ForgotPassword(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }


        // Home Screens
        composable(route = AppScreen.AdminScreen.name) {
            AdminScreen(
                loginViewModel = loginViewModel,
                adminViewModel = adminViewModel,
                managerViewModel = managerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.ManagerScreen.name) {
            ManagerScreen(
                loginViewModel = loginViewModel,
                managerViewModel = managerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.EmployeeScreen.name) {
            EmployeeScreen(
                loginViewModel = loginViewModel,
                managerViewModel = managerViewModel,
                navController = navController
            )
        }

        // User Management
        composable(route = AppScreen.AddUser.name) {
            AddUser(
                adminViewModel = adminViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.EditUser.name) {
            EditUser(
                loginViewModel = loginViewModel,
                adminViewModel = adminViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.LoginHistory.name) {
            LoginHistory(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }

        // Student Management
        composable(route = AppScreen.StudentManagement.name) {
            StudentManagement(
                navController = navController,
                managerViewModel = managerViewModel,
                loginViewModel = loginViewModel
            )
        }
        composable(route = AppScreen.AddStudent.name) {
            AddStudent(
                managerViewModel = managerViewModel,
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.EditStudent.name) {
            EditStudent(
                managerViewModel = managerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.StudentDetailProfile.name) {
            StudentDetailProfile(
                navController = navController,
                managerViewModel = managerViewModel
            )
        }
        composable(route = AppScreen.CertificateList.name) {
            CertificateList(
                managerViewModel = managerViewModel,
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.AddCertificate.name) {
            AddCertificate(
                loginViewModel = loginViewModel,
                managerViewModel = managerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.EditCertificate.name) {
            EditCertificate(
                managerViewModel = managerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.CertificateDetail.name) {
            CertificateDetail(
                managerViewModel = managerViewModel,
                loginViewModel = loginViewModel,
                navController = navController
            )
        }

        // User Profile
        composable(route = AppScreen.UserDetailProfile.name) {
            AdminDetailProfile(
                loginViewModel = loginViewModel,
                adminViewModel = adminViewModel,
                navController = navController,
            )
        }
    }
}