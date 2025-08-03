package com.example.studentinformationmanagement.ui.student

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
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
fun AddStudent(
    loginViewModel: LoginViewModel,
    managerViewModel: ManagerViewModel,
    navController: NavHostController
) {
    // Variables
    val context: Context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val canUploadCSV =
        loginUiState.currentUser?.role == "Manager" || loginUiState.currentUser?.role == "Admin"
    val csvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            managerViewModel.uploadStudentsFromCsv(it, navController, context)
        }
    }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var stdClass by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }

    // UI
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(R.string.Add_AddStudent),
                            style = CustomTypography.headlineMedium,
                            color = PrimaryContent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = PrimaryContent,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        onClick = {
                            navController.navigateUp()
                            managerViewModel.clearErrorMessage()
                        }
                    )
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
                        HelpIcon(stringResource(R.string.HelpIcon_UploadStudent))
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
                        managerViewModel.onAddStudentButtonClick(
                            name = name,
                            email = email,
                            phone = phone,
                            birthday = birthday,
                            id = id,
                            stdClass = stdClass,
                            faculty = faculty,
                            navController = navController,
                            onSuccess = {
                                name = ""
                                email = ""
                                phone = ""
                                birthday = ""
                                id = ""
                                stdClass = ""
                                faculty = ""
                            },
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
                        text = stringResource(R.string.Add_AddStudent),
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
                icon = Icons.Filled.Person,
                value = name,
                onValueChange = { name = it },
                label = "Name",
                enable = true,
                placeholder = "Enter name",
                errorMessage = managerViewModel.nameError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationDate(
                icon = Icons.Default.Cake,
                label = "Birthday",
                placeholder = "Enter Birthday",
                onDatePick = { birthday = it },
                errorMessage = managerViewModel.birthdayError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Email,
                label = "Email",
                value = email,
                onValueChange = { email = it },
                enable = true,
                placeholder = "Enter email",
                errorMessage = managerViewModel.emailError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Phone,
                label = "Phone",
                value = phone,
                onValueChange = { phone = it },
                enable = true,
                placeholder = "Enter phone number",
                errorMessage = managerViewModel.phoneError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.FilterCenterFocus,
                label = "Student ID",
                value = id,
                onValueChange = { id = it },
                enable = true,
                placeholder = "Enter student ID",
                errorMessage = managerViewModel.idError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Book,
                label = "Class",
                value = stdClass,
                onValueChange = { stdClass = it },
                enable = true,
                placeholder = "Enter class",
                errorMessage = managerViewModel.classError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.AccountBalance,
                label = "Faculty",
                value = faculty,
                onValueChange = { faculty = it },
                enable = true,
                placeholder = "Enter faculty",
                errorMessage = managerViewModel.facultyError
            )
        }
    }
}
