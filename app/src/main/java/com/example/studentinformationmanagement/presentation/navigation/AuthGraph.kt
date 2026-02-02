package com.example.studentinformationmanagement.presentation.navigation

import androidx.navigation.navigation
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studentinformationmanagement.presentation.ui.ForgotPasswordScreen
import com.example.studentinformationmanagement.presentation.ui.LoginScreen
import com.example.studentinformationmanagement.presentation.ui.shared.AppGraph
import com.example.studentinformationmanagement.presentation.ui.shared.Screens
import com.example.studentinformationmanagement.presentation.ui.shared.SnackBarType
import com.example.studentinformationmanagement.presentation.uistate.SnackBarUiState
import com.example.studentinformationmanagement.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    onShowSnackBar: (snackBarUiState: SnackBarUiState) -> Unit
) {
    this.navigation(
        startDestination = Screens.LoginScreen.name,
        route = AppGraph.AuthGraph.name,
    ) {
        composable(route = Screens.LoginScreen.name) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authUiState by authViewModel.uiState.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            LoginScreen(
                uiState = authUiState,
                onForgotPasswordClick = {
                    authViewModel.clearErrorMessage()
                    navController.navigate(Screens.ForgotPasswordScreen.name)
                },
                onLoginClick = { email, password, rememberMe ->
                    coroutineScope.launch {
                        authViewModel.login(email, password, rememberMe, onSuccess = {
                            // TODO: Check role and navigate accordingly
                        })
                    }
                }
            )
        }
        composable(route = Screens.ForgotPasswordScreen.name) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authUiState by authViewModel.uiState.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            ForgotPasswordScreen(
                uiState = authUiState,
                onSignInClick = {
                    authViewModel.clearErrorMessage()
                    navController.popBackStack()
                },
                onSendResetEmailClick = { email ->
                    coroutineScope.launch {
                        authViewModel.sendResetEmail(email, onSuccess = {
                            onShowSnackBar(
                                SnackBarUiState(
                                    message = "An email has been sent to reset your password",
                                    type = SnackBarType.Success
                                )
                            )
                            navController.popBackStack()
                        })
                    }
                }
            )
        }
    }
}