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
import com.example.studentinformationmanagement.ui.student.StudentManagement
import com.example.studentinformationmanagement.ui.shared.BottomNavBarManager
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.user.UserDetailProfile
import com.example.studentinformationmanagement.ui.theme.PrimaryContainer
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.theme.ThirdContent
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

// DONE
@Composable
fun ManagerScreen(
    loginViewModel: LoginViewModel,
    managerViewModel: ManagerViewModel,
    navController: NavHostController,
    subNavController: NavHostController = rememberNavController()
) {
    // Variables
    val navItems = listOf(
        NavItem("Student", Icons.Default.Home, AppScreen.StudentManagement.name),
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
                    // Title text based on the current route
                    Text(
                        text = when (currentRoute) {
                            AppScreen.StudentManagement.name -> stringResource(R.string.Home_StudentManagement)
                            else -> stringResource(R.string.Home_Profile)
                        },
                        style = CustomTypography.headlineMedium,
                        color = PrimaryContent
                    )
                    // Logo Image
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
            BottomNavBarManager(
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
            composable(AppScreen.UserDetailProfile.name) {
                UserDetailProfile(
                    loginViewModel = loginViewModel,
                    navController = navController
                )
            }
        }
    }
}


