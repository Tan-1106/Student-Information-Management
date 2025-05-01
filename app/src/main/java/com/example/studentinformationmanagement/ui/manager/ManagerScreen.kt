package com.example.studentinformationmanagement.ui.manager

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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
import com.example.studentinformationmanagement.ui.shared.LoginViewModel
import com.example.studentinformationmanagement.ui.shared.UserDetailProfile
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_container
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.third_content

@Composable
fun ManagerScreen(
    loginViewModel: LoginViewModel,
    managerViewModel: ManagerViewModel,
    navController: NavHostController,
    subNavController: NavHostController = rememberNavController()
) {
    // Bottom navigation bar's items
    val navItems = listOf(
        NavItem("Student", Icons.Default.Home, AppScreen.StudentManagement.name),
        NavItem("Profile", Icons.Default.Person, AppScreen.UserDetailProfile.name)
    )
    val navBackStackEntry by subNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
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
                            else -> "Manager Dashboard"
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
        }
    ) { innerPadding ->
        // Manager feature navigation
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

@Composable
fun BottomNavBarManager(
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    currentRoute: String?,
    onItemClick: (NavItem) -> Unit
) {
    NavigationBar(
        modifier = modifier
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) primary_content else secondary_content
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) primary_content else secondary_content
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = primary_container,
                    selectedIconColor = primary_content,
                    selectedTextColor = primary_content,
                    unselectedIconColor = secondary_content,
                    unselectedTextColor = secondary_content
                )
            )
        }
    }
}
