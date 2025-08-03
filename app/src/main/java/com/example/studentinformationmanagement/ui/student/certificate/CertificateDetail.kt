package com.example.studentinformationmanagement.ui.student.certificate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateDetail(
    managerViewModel: ManagerViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Variables
    val managerUiState by managerViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val editEnable =
        loginUiState.currentUser?.role == "Manager" || loginUiState.currentUser?.role == "Admin"

    // UI
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = PrimaryContent,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                },
                title = {
                    Text(
                        text = stringResource(R.string.Certificate),
                        style = CustomTypography.headlineMedium,
                        color = PrimaryContent
                    )
                },
                actions = {
                    if (editEnable) {
                        IconButton(
                            onClick = {
                                managerViewModel.onEditCertificateSwipe(
                                    certificateId = managerUiState.selectedCertificate.id,
                                    navController = navController
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Certificate Edit",
                                tint = PrimaryContent
                            )
                        }
                    }
                }
            )
        },
        containerColor = Color.White,
        modifier = modifier.systemBarsPadding()
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon = Icons.Filled.Title,
                    label = "Title",
                    value = managerUiState.selectedCertificate.title
                )
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon = Icons.Filled.Book,
                    label = "Course name",
                    value = managerUiState.selectedCertificate.courseName
                )
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon = Icons.Filled.Numbers,
                    label = "Certificate ID",
                    value = managerUiState.selectedCertificate.id
                )
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon = Icons.Filled.LocationCity,
                    label = "Issuing organization",
                    value = managerUiState.selectedCertificate.issuingOrganization
                )
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon = Icons.Filled.DateRange,
                    label = "Issue date",
                    value = managerUiState.selectedCertificate.issueDate
                )
                Spacer(modifier = modifier.height(20.dp))
                InformationLine(
                    icon = Icons.Filled.Timer,
                    label = "Expiration date",
                    value = managerUiState.selectedCertificate.expirationDate
                )
            }
        }
    }

}