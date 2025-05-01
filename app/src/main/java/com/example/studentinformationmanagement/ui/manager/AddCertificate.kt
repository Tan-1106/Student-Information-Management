package com.example.studentinformationmanagement.ui.manager

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun AddCertificate(
    managerViewModel: ManagerViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Add Certificate",
                            fontSize = 35.sp,
                            fontFamily = kanit_bold_font,
                            color = primary_content
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                            managerViewModel.clearCertificateInputs()
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
                        managerViewModel.onAddCertificateButtonClick(
                            navController = navController,
                            context = context
                        )
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
                        "Add Certificate",
                        style = TextStyle(color = primary_dark, fontFamily = kanit_bold_font)
                    )
                }
            }
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(all = 5.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InformationLine(
                icon = Icons.Filled.Title,
                value = managerViewModel.certificateTitleValue,
                onValueChange = { managerViewModel.onCTitleChange(it) },
                label = "Title",
                enable = true,
                placeholder = "Enter certificate title",
                errorMessage = managerViewModel.titleError
            )
            InformationLine(
                icon = Icons.Filled.Book,
                value = managerViewModel.certificateCourseNameValue,
                onValueChange = { managerViewModel.onCCourseNameChange(it) },
                label = "Course name",
                enable = true,
                placeholder = "Enter course name",
                errorMessage = managerViewModel.courseNameError
            )
            InformationLine(
                icon = Icons.Filled.Numbers,
                value = managerViewModel.certificateIdValue,
                onValueChange = { managerViewModel.onCIdChange(it) },
                label = "ID",
                enable = true,
                placeholder = "Enter certificate ID",
                errorMessage = managerViewModel.cIdError
            )
            InformationLine(
                icon = Icons.Filled.LocationCity,
                value = managerViewModel.certificateOrganizationValue,
                onValueChange = { managerViewModel.onCOrganizationChange(it) },
                label = "Issuing organization",
                enable = true,
                placeholder = "Enter organization",
                errorMessage = managerViewModel.organizationError
            )
            InformationDate(
                icon = Icons.Default.DateRange,
                label = "Issue date",
                placeholder = "Enter issue date",
                onDatePick = { managerViewModel.onCIssueDateChange(it) },
                errorMessage = managerViewModel.issueDateError
            )
            InformationDate(
                icon = Icons.Default.Timer,
                label = "Expiration date",
                placeholder = "Enter expiration date",
                onDatePick = { managerViewModel.onCExpirationDateChange(it) },
                errorMessage = managerViewModel.expirationDateError,
                canSelectFuture = true
            )
        }
    }
}
