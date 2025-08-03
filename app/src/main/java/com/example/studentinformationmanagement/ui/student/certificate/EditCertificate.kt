package com.example.studentinformationmanagement.ui.student.certificate


import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.viewModel.ManagerViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCertificate(
    managerViewModel: ManagerViewModel,
    navController: NavHostController
) {
    // Variables
    val context: Context = LocalContext.current
    val certificate = managerViewModel.certificateToEdit
    var titleValue by remember { mutableStateOf(certificate?.title ?: "") }
    var courseNameValue by remember { mutableStateOf(certificate?.courseName ?: "") }
    val idValue by remember { mutableStateOf(certificate?.id ?: "") }
    var organizationValue by remember { mutableStateOf(certificate?.issuingOrganization ?: "") }
    var issueDateValue by remember { mutableStateOf(certificate?.issueDate ?: "") }
    var expirationDateValue by remember { mutableStateOf(certificate?.expirationDate ?: "") }

    // UI
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.Edit_EditCertificate),
                            style = CustomTypography.headlineMedium,
                            color = PrimaryContent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            managerViewModel.clearCErrorMessage()
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector =  Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = PrimaryContent,
                            modifier = Modifier.size(30.dp)
                        )
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
                        if (managerViewModel.validateCertificateInputs(
                            newTitle = titleValue,
                            newCourseName = courseNameValue,
                            newId = idValue,
                            currentId = certificate?.id ?: "",
                            newOrganization = organizationValue,
                            newIssueDate = issueDateValue,
                            newExpirationDate = expirationDateValue
                        )) {
                            managerViewModel.onEditCertificateSaveClick(
                                newTitle = titleValue,
                                newCourseName = courseNameValue,
                                newId = idValue,
                                newOrganization = organizationValue,
                                newIssueDate = issueDateValue,
                                newExpirationDate = expirationDateValue,
                                context = context,
                                navController = navController
                            )
                        }
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
                        text = stringResource(R.string.Button_Save),
                        style = CustomTypography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(all = 5.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Title,
                value = titleValue,
                onValueChange = { titleValue = it },
                label = "Title",
                enable = true,
                errorMessage = managerViewModel.titleError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Book,
                value = courseNameValue,
                onValueChange = { courseNameValue = it },
                label = "Course name",
                enable = true,
                errorMessage = managerViewModel.courseNameError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.LocationCity,
                value = organizationValue,
                onValueChange = { organizationValue = it },
                label = "Issuing organization",
                enable = true,
                errorMessage = managerViewModel.organizationError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationDate(
                icon = Icons.Default.DateRange,
                label = "Issue date",
                placeholder = issueDateValue,
                onDatePick = { issueDateValue = it },
                errorMessage = managerViewModel.issueDateError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationDate(
                icon = Icons.Default.Timer,
                label = "Expiration date",
                placeholder = expirationDateValue,
                onDatePick = { expirationDateValue = it },
                errorMessage = managerViewModel.expirationDateError,
                canSelectFuture = true
            )
        }
    }
}
