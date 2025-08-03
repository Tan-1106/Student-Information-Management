package com.example.studentinformationmanagement.ui.student.certificate

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.HelpIcon
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.viewModel.LoginViewModel
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCertificate(
    loginViewModel: LoginViewModel,
    managerViewModel: ManagerViewModel,
    navController: NavHostController
) {
    // Variables
    val context: Context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val canUploadCSV = loginUiState.currentUser?.role == "Manager" || loginUiState.currentUser?.role == "Admin"
    val csvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            managerViewModel.uploadCertificatesFromCsv(it, navController, context)
        }
    }
    var title by remember { mutableStateOf("") }
    var courseName by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var issuingOrganization by remember { mutableStateOf("") }
    var issueDate by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }

    // UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.Add_AddCertificate),
                            style = CustomTypography.headlineMedium,
                            color = PrimaryContent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            title = ""
                            courseName = ""
                            id = ""
                            issuingOrganization = ""
                            issueDate = ""
                            expirationDate = ""
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = PrimaryContent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    if (canUploadCSV) {
                        IconButton(
                            onClick = {
                                csvLauncher.launch("text/csv")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.UploadFile,
                                contentDescription = "Upload CSV",
                                tint = PrimaryContent
                            )
                        }
                        HelpIcon(message = stringResource(R.string.HelpIcon_Certificate))
                    }
                }
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        managerViewModel.onAddCertificateButtonClick(
                            titleInput = title,
                            courseNameInput = courseName,
                            idInput = id,
                            organizationInput = issuingOrganization,
                            issueDateInput = issueDate,
                            expirationDateInput = expirationDate,
                            navController = navController,
                            context = context
                        )
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = PrimaryContent,
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.Button_Add),
                        style = CustomTypography.titleMedium,
                        color = Color.White
                    )
                }
            }
        },
        containerColor = Color.White,
        modifier = Modifier.systemBarsPadding()
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(all = 5.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Title,
                value = title,
                onValueChange = { title = it },
                label = "Title",
                enable = true,
                placeholder = "Enter certificate title",
                errorMessage = managerViewModel.titleError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Book,
                value = courseName,
                onValueChange = { courseName = it },
                label = "Course name",
                enable = true,
                placeholder = "Enter course name",
                errorMessage = managerViewModel.courseNameError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Numbers,
                value = id,
                onValueChange = { id = it },
                label = "ID",
                enable = true,
                placeholder = "Enter certificate ID",
                errorMessage = managerViewModel.cIdError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.LocationCity,
                value = issuingOrganization,
                onValueChange = { issuingOrganization = it },
                label = "Issuing organization",
                enable = true,
                placeholder = "Enter organization",
                errorMessage = managerViewModel.organizationError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationDate(
                icon = Icons.Default.DateRange,
                label = "Issue date",
                placeholder = "Enter issue date",
                onDatePick = { issueDate = it },
                errorMessage = managerViewModel.issueDateError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationDate(
                icon = Icons.Default.Timer,
                label = "Expiration date",
                placeholder = "Enter expiration date",
                onDatePick = { expirationDate = it },
                errorMessage = managerViewModel.expirationDateError,
                canSelectFuture = true
            )
        }
    }
}
