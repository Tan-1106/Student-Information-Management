package com.example.studentinformationmanagement.ui.admin

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.ui.shared.DetailProfile
import com.example.studentinformationmanagement.ui.shared.LoginViewModel
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content

// Composable: User's detail profile
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDetailProfile(
    loginViewModel: LoginViewModel,
    adminViewModel: AdminViewModel,
    navController: NavHostController
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()

    // Get current logged in user
    val currentUser = loginUiState.currentUser
    val showBackButton = currentUser?.userRole != "Admin" || adminViewModel.userToView.userPhoneNumber != ""

    if (currentUser != null) {
        DetailProfile(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(
                                onClick = {
                                    navController.navigateUp()
                                    adminViewModel.clearUserToView()
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = primary_content,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    },
                    title = {
                        if (adminViewModel.userToView.userPhoneNumber == "") {
                            Text(
                                text = "YOUR PROFILE",
                                fontFamily = kanit_bold_font,
                                color = primary_content
                            )
                        } else {
                            Text(
                                text = "USER'S PROFILE",
                                fontFamily = kanit_bold_font,
                                color = primary_content
                            )
                        }
                    }, actions = {
                        if (adminViewModel.userToView.userPhoneNumber == "") {
                            IconButton(onClick = {
                                // Reuse swipe to edit function
                                adminViewModel.onEditUserSwipe(
                                    userPhoneNumber = currentUser.userPhoneNumber,
                                    navController = navController
                                )
                            }) {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = null,
                                    tint = primary_content,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            // Log out event
                            IconButton(
                                onClick = {
                                    loginViewModel.onLogOutButtonClicked()
                                    navController.navigate(AppScreen.Login.name) {
                                        popUpTo(0) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Outlined.Logout,
                                    contentDescription = null,
                                    tint = primary_content,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                )
            },
            user = if (adminViewModel.userToView.userPhoneNumber == "") {
                // Current Logged in user's profile
                currentUser
            } else {
                // User want to view 's profile
                adminViewModel.userToView
            }
        )
    }
}
