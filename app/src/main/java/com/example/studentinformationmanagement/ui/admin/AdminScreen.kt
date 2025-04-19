package com.example.studentinformationmanagement.ui.admin

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.shared.NavItem
import com.example.studentinformationmanagement.ui.manager.StudentManagement
import com.example.studentinformationmanagement.ui.shared.LoginViewModel
import com.example.studentinformationmanagement.ui.shared.UserDetailProfile
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.third_content

// Composable: Admin Home screen
@Composable
fun AdminScreen(
    loginViewModel: LoginViewModel,
    navController: NavHostController,
    subNavController: NavHostController = rememberNavController()
) {
    val navItems = listOf(
        NavItem("Student", Icons.Default.Home, AppScreen.StudentManagement.name),
        NavItem("User", Icons.Default.AccountCircle, AppScreen.UserManagement.name),
        NavItem("Profile", Icons.Default.Person, AppScreen.UserDetailProfile.name)
    )

    val navBackStackEntry by subNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.statusBarsPadding()
            .background(primary_container),

        // Top Bar (Only show the if the current route is not "Profile")
        topBar = {
            if (currentRoute != AppScreen.UserDetailProfile.name) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(third_content)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Title text based on the current route
                    Text(
                        text = when (currentRoute) {
                            AppScreen.StudentManagement.name -> "Student Management"
                            AppScreen.UserManagement.name -> "User Management"
                            else -> "Admin Dashboard"
                        },
                        fontSize = 28.sp,
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                    // Logo Image
                    Image(
                        painter = painterResource(id = R.drawable.login_image),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
            }
        },

        // Bottom Bar
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
        }
    ) { innerPadding ->
        NavHost(
            navController = subNavController,
            startDestination = AppScreen.StudentManagement.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.StudentManagement.name) {
                StudentManagement(
                    navController = navController
                )
            }
            composable(AppScreen.UserManagement.name) {
                UserManagement(
                    navController = navController
                )
            }
            composable(AppScreen.UserDetailProfile.name) {
                UserDetailProfile(
                    loginViewModel = loginViewModel
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun AdminScreenPreview() {
    val fakeScreenNavController = rememberNavController()
    AdminScreen(
        loginViewModel = viewModel(),
        fakeScreenNavController
    )
}


