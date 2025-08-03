package com.example.studentinformationmanagement.ui.user

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.DetailProfile
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.viewModel.AdminViewModel
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDetailProfile(
    loginViewModel: LoginViewModel,
    adminViewModel: AdminViewModel,
    navController: NavHostController
) {
    // Variables
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val currentUser = loginUiState.currentUser
    val showBackButton = currentUser?.role != "Admin" || adminViewModel.userToView.phone != ""

    // UI
    if (currentUser != null) {
        DetailProfile(
            topBar = {
                TopAppBar(
                    title = {
                        if (adminViewModel.userToView.phone == "") {
                            Text(
                                text = stringResource(R.string.Profile_YourProfile),
                                style = CustomTypography.headlineMedium,
                                color = PrimaryContent
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.Profile_UserProfile),
                                style = CustomTypography.headlineMedium,
                                color = PrimaryContent
                            )
                        }
                    },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(
                                onClick = {
                                    navController.navigateUp()
                                    adminViewModel.clearUserToView()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = PrimaryContent,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    },
                    actions = {
                        if (adminViewModel.userToView.email.isEmpty()) {
                            IconButton(
                                onClick = {
                                    adminViewModel.onEditUser(
                                        userEmail = currentUser.email,
                                        navController = navController
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = null,
                                    tint = PrimaryContent,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
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
                                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                                    contentDescription = null,
                                    tint = PrimaryContent,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                )
            },
            user = if (adminViewModel.userToView.email.isEmpty()) currentUser else adminViewModel.userToView
        )
    }
}
