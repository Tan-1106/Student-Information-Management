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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.data.shared.NavItem
import com.example.studentinformationmanagement.ui.manager.StudentManagement
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.third_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavHostController = rememberNavController()) {
    val navItems = listOf(
        NavItem("Student", Icons.Default.Home, AppScreen.StudentManagement.name),
        NavItem("User", Icons.Default.AccountCircle, AppScreen.UserManagement.name),
        NavItem("Profile", Icons.Default.Person, AppScreen.UserDetailProfile.name)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.statusBarsPadding()
            .background(primary_container),
        topBar = {
            // Only show the top bar if the current route is not "Profile"
            if (currentRoute != AppScreen.UserDetailProfile.name) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(third_content) // Background color
                        .padding(horizontal = 20.dp, vertical = 16.dp), // Padding for inner content
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Title text based on the current route
                    Text(
                        text = when (currentRoute) {
                            AppScreen.StudentManagement.name -> "Student Management"
                            AppScreen.UserManagement.name -> "User Management"
                            else -> "Admin Dashboard" // Default title
                        },
                        fontSize = 28.sp,
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                    // Avatar Image
                    Image(
                        painter = painterResource(id = R.drawable.login_image), // Replace with a valid image
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape) // Avatar shape
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
                        navController.navigate(item.route) {
                            popUpTo(AppScreen.StudentManagement.name) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.StudentManagement.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.StudentManagement.name) { StudentManagement() }
            composable(AppScreen.UserManagement.name) { /* TODO: UserManagement screen */ }
            composable(AppScreen.UserDetailProfile.name) { /* TODO: Profile screen */ }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun AdminScreenPreview() {
    AdminScreen()
}
