package com.example.studentinformationmanagement.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.ui.manager.ManagerViewModel
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateDetail(
    managerViewModel: ManagerViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val managerUiState by managerViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val editEnable = loginUiState.currentUser?.userRole == "Manager" || loginUiState.currentUser?.userRole == "Admin"

    Scaffold(
        containerColor = Color.White,
        modifier = modifier
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
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

                },
                title = {
                    Text(
                        text = "CERTIFICATE",
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                },
                actions = {
                    if (editEnable) {
                        IconButton(
                            onClick = {
                                // Reuse onEditCertificateSwipe()
                                managerViewModel.onEditCertificateSwipe(
                                    certificateId = managerUiState.selectedCertificate.certificateId,
                                    navController = navController
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Certificate Edit",
                                tint = primary_content
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InformationLine(Icons.Filled.Title, "Title", managerUiState.selectedCertificate.certificateTitle)
                InformationLine(Icons.Filled.Book, "Course name", managerUiState.selectedCertificate.courseName)
                InformationLine(Icons.Filled.Numbers, "Certificate ID", managerUiState.selectedCertificate.certificateId)
                InformationLine(Icons.Filled.LocationCity, "Issuing organization", managerUiState.selectedCertificate.issuingOrganization)
                InformationLine(Icons.Filled.DateRange, "Issue date", managerUiState.selectedCertificate.issueDate)
                InformationLine(Icons.Filled.Timer, "Expiration date", managerUiState.selectedCertificate.expirationDate)
            }
        }
    }

}