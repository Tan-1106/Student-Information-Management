package com.example.studentinformationmanagement.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.presentation.navigation.authGraph
import com.example.studentinformationmanagement.presentation.ui.shared.AppGraph
import com.example.studentinformationmanagement.presentation.ui.shared.GradientSnackBar
import com.example.studentinformationmanagement.presentation.uistate.SnackBarUiState
import com.example.studentinformationmanagement.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authUiState by authViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser()
    }
    LaunchedEffect(authUiState.user) {
        // TODO: Navigate based on user role
        // if (authUiState.user != null) { }
    }

    // SnackBar Management
    var snackBarInstance by remember { mutableStateOf<SnackBarUiState?>(null) }
    LaunchedEffect(snackBarInstance) {
        if (snackBarInstance != null) {
            delay(3000L)
            snackBarInstance = null
        }
    }
    val onShowSnackBar: (SnackBarUiState) -> Unit = {
        snackBarInstance = it
    }

    // Main UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        NavHost(
            navController = navController,
            startDestination = AppGraph.AuthGraph.name
        ) {
            authGraph(
                navController = navController,
                onShowSnackBar = onShowSnackBar
            )
        }

        // SnackBar UI
        snackBarInstance?.let { snackBarUiState ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                GradientSnackBar(
                    type = snackBarUiState.type,
                    message = snackBarUiState.message ?: "",
                )
            }
        }
    }
}