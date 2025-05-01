package com.example.studentinformationmanagement

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.ui.admin.AddUser
import com.example.studentinformationmanagement.ui.admin.AdminDetailProfile
import com.example.studentinformationmanagement.ui.admin.AdminScreen
import com.example.studentinformationmanagement.ui.admin.AdminViewModel
import com.example.studentinformationmanagement.ui.admin.EditUser
import com.example.studentinformationmanagement.ui.admin.LoginHistory
import com.example.studentinformationmanagement.ui.employee.EmployeeScreen
import com.example.studentinformationmanagement.ui.manager.AddCertificate
import com.example.studentinformationmanagement.ui.manager.AddStudent
import com.example.studentinformationmanagement.ui.manager.EditCertificate
import com.example.studentinformationmanagement.ui.manager.EditStudent
import com.example.studentinformationmanagement.ui.manager.ManagerScreen
import com.example.studentinformationmanagement.ui.manager.ManagerViewModel
import com.example.studentinformationmanagement.ui.manager.StudentManagement
import com.example.studentinformationmanagement.ui.shared.CertificateDetail
import com.example.studentinformationmanagement.ui.shared.LoginScreen
import com.example.studentinformationmanagement.ui.shared.LoginViewModel
import com.example.studentinformationmanagement.ui.shared.StudentCertificationList
import com.example.studentinformationmanagement.ui.shared.StudentDetailProfile

enum class AppScreen() {
    Login,
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


        // All Main Screens
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

        // Admin Features
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


        // Manager Features
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
            StudentCertificationList(
                managerViewModel = managerViewModel,
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.AddCertificate.name) {
            AddCertificate(
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


        // General Features
        composable(route = AppScreen.UserDetailProfile.name) {
            AdminDetailProfile(
                loginViewModel = loginViewModel,
                adminViewModel = adminViewModel,
                navController = navController,
            )
        }
    }
}