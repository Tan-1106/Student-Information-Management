package com.example.studentinformationmanagement.ui.shared

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.shared.CurrentUser
import com.example.studentinformationmanagement.ui.theme.primary_content

// Composable: User's detail profile
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailProfile(
    loginViewModel: LoginViewModel,
    navController: NavHostController,
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()

    // Get current logged in user
    val currentUser = loginUiState.currentUser
    val showBackButton = currentUser?.userRole != "Admin"

    if (currentUser != null) {
        DetailProfile(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        /* TODO: Check again if Manager and Employee need this button ? */
                        if (showBackButton) {
                            IconButton(
                                onClick = {
                                    navController.navigateUp()
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
                    title = {}, actions = {
                        IconButton(onClick = {
                            /* TODO: Setting button's UI implementation */

                        }) {
                            Icon(
                                Icons.Outlined.Settings,
                                contentDescription = null,
                                tint = primary_content,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        // Log out event
                        IconButton(onClick = {
                            loginViewModel.onLogOutButtonClicked()
                            navController.navigate(AppScreen.Login.name)
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.Logout,
                                contentDescription = null,
                                tint = primary_content,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                )
            },
            user = currentUser
        )
    }
}
