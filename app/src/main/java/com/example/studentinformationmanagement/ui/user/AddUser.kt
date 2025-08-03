package com.example.studentinformationmanagement.ui.user

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.CustomTypography
import com.example.studentinformationmanagement.ui.theme.PrimaryContent
import com.example.studentinformationmanagement.ui.viewModel.AdminViewModel

// DONE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUser(
    adminViewModel: AdminViewModel,
    navController: NavHostController
) {
    // Variables
    val context: Context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

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
                            text = stringResource(R.string.Add_AddUser),
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
                            name = ""
                            email = ""
                            phone = ""
                            birthday = ""
                            status = ""
                            role = ""
                            navController.navigateUp()
                            adminViewModel.clearErrorMessage()
                        }
                    )
                },
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        adminViewModel.onAddUserButtonClick(
                            nameInput = name,
                            emailInput = email,
                            phoneInput = phone,
                            birthdayInput = birthday,
                            roleInput = role,
                            statusInput = status,
                            onSuccess = {
                                name = ""
                                email = ""
                                phone = ""
                                birthday = ""
                                status = ""
                                role = ""
                            },
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
                        text = stringResource(R.string.Button_Create),
                        style = CustomTypography.titleMedium,
                        color = Color.White,
                    )
                }
            }
        },
        containerColor = Color.White,
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(5.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Person,
                value = name,
                onValueChange = { name = it },
                label = "Name",
                enable = true,
                placeholder = "Enter name",
                errorMessage = adminViewModel.nameError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationDate(
                icon = Icons.Default.Cake,
                label = "Birthday",
                placeholder = "Enter Birthday",
                onDatePick = { birthday = it },
                errorMessage = adminViewModel.birthdayError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Email,
                label = "Email",
                value = email,
                onValueChange = { email = it },
                enable = true,
                placeholder = "Enter email",
                errorMessage = adminViewModel.emailError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationLine(
                icon = Icons.Filled.Phone,
                label = "Phone",
                value = phone,
                onValueChange = { phone = it },
                enable = true,
                placeholder = "Enter phone number",
                errorMessage = adminViewModel.phoneError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationSelect(
                icon = Icons.Filled.BrokenImage,
                label = "Status",
                options = listOf("Active", "Inactive"),
                onOptionPick = { status = it },
                errorMessage = adminViewModel.statusError
            )
            Spacer(modifier = Modifier.height(20.dp))
            InformationSelect(
                icon = Icons.Filled.Settings,
                label = "Role",
                options = listOf("Manager", "Employee"),
                onOptionPick = { role = it },
                errorMessage = adminViewModel.roleError
            )
        }
    }
}