package com.example.studentinformationmanagement

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.ui.admin.AddUser
import com.example.studentinformationmanagement.ui.admin.AdminScreen
import com.example.studentinformationmanagement.ui.shared.LoginScreen
import com.example.studentinformationmanagement.ui.shared.LoginViewModel
import com.example.studentinformationmanagement.ui.shared.UserDetailProfile
import com.example.studentinformationmanagement.ui.shared.SwipeActionItem
import com.example.studentinformationmanagement.ui.shared.SwipeComponent

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
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.name
    ) {
        composable(route = AppScreen.Login.name) {
            LoginScreen(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.AddUser.name) {
            AddUser()
        }
        composable(route = AppScreen.EditUser.name) {

        }
        composable(route = AppScreen.LoginHistory.name) {

        }
        composable(route = AppScreen.AddStudent.name) {
            var context = LocalContext.current
            Scaffold {
                Box(modifier = Modifier
                    .padding(it)
                    .systemBarsPadding()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SwipeComponent(
                            onSwipeLeft = {
                                Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show()
                            },
                            onSwipeRight = {
                                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
                            },
                            content = { Text("TEST", color = Color.Red) })
                    }
                }
            }
        }
        composable(route = AppScreen.EditStudent.name) {

        }
        composable(route = AppScreen.AddCertificate.name) {

        }
        composable(route = AppScreen.EditCertificate.name) {

        }
        composable(route = AppScreen.UserDetailProfile.name) {
            UserDetailProfile(
                loginViewModel = loginViewModel
            )
        }
        composable(route = AppScreen.StudentDetailProfile.name) {

        }
        composable (route = AppScreen.StudentManagement.name) {
            StudentManagement(
                navController = navController
            )
        }
        composable (route = AppScreen.AdminScreen.name){
            AdminScreen(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
    }
}