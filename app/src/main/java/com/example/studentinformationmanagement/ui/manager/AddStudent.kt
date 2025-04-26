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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun AddStudent(
    managerViewModel: ManagerViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context: Context = LocalContext.current

    Scaffold(modifier = Modifier.systemBarsPadding(), containerColor = Color.White, topBar = {
        TopAppBar(
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Add Student",
                        fontSize = 35.sp,
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                }
            },
            navigationIcon = {
                IconButton(content = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = primary_content,
                        modifier = Modifier.size(40.dp)
                    )
                },
                    onClick = {
                        navController.navigateUp()
                    }
                )
            },

            )
    }, bottomBar = {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    managerViewModel.onAddStudentButtonClick(
                        navController = navController,
                        context = context
                    )
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = secondary_content,
                ),
            ) {
                Text(
                    "Add Student",
                    style = TextStyle(color = primary_dark, fontFamily = kanit_bold_font)
                )
            }
        }
    }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(all = 5.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InformationLine(
                icon = Icons.Filled.Person,
                value = managerViewModel.newStudentName,
                onValueChange = { managerViewModel.onNewStudentNameChange(it) },
                label = "Name",
                enable = true,
                placeholder = "Enter name",
                errorMessage = managerViewModel.nameError
            )
            InformationDate(
                icon = Icons.Default.Cake,
                label = "Birthday",
                placeholder = "Enter Birthday",
                onDatePick = { managerViewModel.onNewStudentBirthdayPick(it) },
                errorMessage = managerViewModel.birthdayError
            )
            InformationLine(
                icon = Icons.Filled.Email,
                label = "Email",
                value = managerViewModel.newStudentEmail,
                onValueChange = { managerViewModel.onNewStudentEmailChange(it) },
                enable = true,
                placeholder = "Enter email",
                errorMessage = managerViewModel.emailError
            )
            InformationLine(
                icon = Icons.Filled.Phone,
                label = "Phone",
                value = managerViewModel.newStudentPhone,
                onValueChange = { managerViewModel.onNewStudentPhoneChange(it) },
                enable = true,
                placeholder = "Enter phone number",
                errorMessage = managerViewModel.phoneError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                )
            )
            InformationLine(
                icon = Icons.Filled.FilterCenterFocus,
                label = "Student ID",
                value = managerViewModel.newStudentId,
                onValueChange = { managerViewModel.onNewStudentIdChange(it) },
                enable = true,
                placeholder = "Enter student ID",
                errorMessage = managerViewModel.idError
            )
            InformationLine(
                icon = Icons.Filled.Book,
                label = "Class",
                value = managerViewModel.newStudentClass,
                onValueChange = { managerViewModel.onNewStudentClassChange(it) },
                enable = true,
                placeholder = "Enter class",
                errorMessage = managerViewModel.classError
            )
            InformationLine(
                icon = Icons.Filled.AccountBalance,
                label = "Faculty",
                value = managerViewModel.newStudentFaculty,
                onValueChange = { managerViewModel.onNewStudentFacultyChange(it) },
                enable = true,
                placeholder = "Enter faculty",
                errorMessage = managerViewModel.facultyError
            )
        }
    }
}
