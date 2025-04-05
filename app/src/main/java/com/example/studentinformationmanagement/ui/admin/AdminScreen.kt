package com.example.studentinformationmanagement.ui.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.shared.NavItem
import com.example.studentinformationmanagement.ui.manager.StudentManagement

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
