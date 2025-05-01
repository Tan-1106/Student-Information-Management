package com.example.studentinformationmanagement.ui.manager


import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCertificate(
    managerViewModel: ManagerViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current

    val certificate = managerViewModel.certificateToEdit
    var titleValue by remember { mutableStateOf(certificate?.certificateTitle ?: "") }
    var courseNameValue by remember { mutableStateOf(certificate?.courseName ?: "") }
    var idValue by remember { mutableStateOf(certificate?.certificateId ?: "") }
    var organizationValue by remember { mutableStateOf(certificate?.issuingOrganization ?: "") }
    var issueDateValue by remember { mutableStateOf(certificate?.issueDate ?: "") }
    var expirationDateValue by remember { mutableStateOf(certificate?.expirationDate ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Edit Certificate",
                            fontSize = 35.sp,
                            fontFamily = kanit_bold_font,
                            color = primary_content
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
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = primary_content,
                            modifier = Modifier.size(40.dp)
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
                            currentId = certificate?.certificateId ?: "",
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
                        containerColor = secondary_content,
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        "Save",
                        style = TextStyle(color = primary_dark, fontFamily = kanit_bold_font)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(all = 5.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            InformationLine(
                icon = Icons.Filled.Title,
                value = titleValue,
                onValueChange = { titleValue = it },
                label = "Title",
                enable = true,
                errorMessage = managerViewModel.titleError
            )

            InformationLine(
                icon = Icons.Filled.Book,
                value = courseNameValue,
                onValueChange = { courseNameValue = it },
                label = "Course name",
                enable = true,
                errorMessage = managerViewModel.courseNameError
            )

            // Fixed - Cannot edit certificate ID
            InformationLine(
                icon = Icons.Filled.Numbers,
                value = "$idValue (Cannot be edited)",
                onValueChange = { idValue = it },
                label = "ID",
                enable = false,
                errorMessage = managerViewModel.cIdError
            )
            InformationLine(
                icon = Icons.Filled.LocationCity,
                value = organizationValue,
                onValueChange = { organizationValue = it },
                label = "Issuing organization",
                enable = true,
                errorMessage = managerViewModel.organizationError
            )
            InformationDate(
                icon = Icons.Default.DateRange,
                label = "Issue date",
                placeholder = issueDateValue,
                onDatePick = { issueDateValue = it },
                errorMessage = managerViewModel.issueDateError
            )
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
