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
import com.example.studentinformationmanagement.ui.theme.primary_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailProfile(
    loginViewModel: LoginViewModel
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val currentUser = loginUiState.currentUser

    if (currentUser != null) {
        DetailProfile(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                // Xử lý back
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = primary_content,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    },
                    title = {}, actions = {
                        IconButton(onClick = {
                            // Xử lý setting

                        }) {
                            Icon(
                                Icons.Outlined.Settings,
                                contentDescription = null,
                                tint = primary_content,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        IconButton(onClick = {
                            // Xử lý logout

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
