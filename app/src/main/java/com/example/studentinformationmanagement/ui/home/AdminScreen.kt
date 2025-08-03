package com.example.studentinformationmanagement.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.shared.NavItem
import com.example.studentinformationmanagement.ui.user.AdminDetailProfile
import com.example.studentinformationmanagement.ui.user.UserManagement
import com.example.studentinformationmanagement.ui.shared.BottomNavBarAdmin
import com.example.studentinformationmanagement.ui.student.StudentManagement
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContainer
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.theme.ThirdContent
import com.example.studentinformationmanagement.ui.viewModel.AdminViewModel
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

// DONE
@Composable
fun AdminScreen(
    loginViewModel: LoginViewModel,
    adminViewModel: AdminViewModel,
    managerViewModel: ManagerViewModel,
    navController: NavHostController,
    subNavController: NavHostController = rememberNavController()
) {
    // Variables
    val navItems = listOf(
        NavItem("Student", Icons.Default.Home, AppScreen.StudentManagement.name),
        NavItem("User", Icons.Default.AccountCircle, AppScreen.UserManagement.name),
        NavItem("Profile", Icons.Default.Person, AppScreen.UserDetailProfile.name)
    )
    val navBackStackEntry by subNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // UI
    Scaffold(
        topBar = {
            if (currentRoute != AppScreen.UserDetailProfile.name) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ThirdContent)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = when (currentRoute) {
                            AppScreen.StudentManagement.name -> stringResource(R.string.Home_StudentManagement)
                            AppScreen.UserManagement.name -> stringResource(R.string.Home_UserManagement)
                            else -> stringResource(R.string.Home_Profile)
                        },
                        style = CustomTypography.headlineMedium,
                        color = PrimaryContent
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavBarAdmin(
                items = navItems,
                currentRoute = currentRoute,
                onItemClick = { item ->
                    if (currentRoute != item.route) {
                        subNavController.navigate(item.route) {
                            popUpTo(AppScreen.StudentManagement.name) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        },
        modifier = Modifier
            .statusBarsPadding()
            .background(PrimaryContainer)
    ) { innerPadding ->
        NavHost(
            navController = subNavController,
            startDestination = AppScreen.StudentManagement.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.StudentManagement.name) {
                StudentManagement(
                    navController = navController,
                    managerViewModel = managerViewModel,
                    loginViewModel = loginViewModel
                )
            }
            composable(AppScreen.UserManagement.name) {
                UserManagement(
                    navController = navController,
                    adminViewModel = adminViewModel
                )
            }
            composable(AppScreen.UserDetailProfile.name) {
                adminViewModel.clearUserToView()
                AdminDetailProfile(
                    loginViewModel = loginViewModel,
                    adminViewModel = adminViewModel,
                    navController = navController
                )
            }
        }
    }
}

