package com.example.studentinformationmanagement.ui.admin

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUser(
    adminViewModel: AdminViewModel,
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
                            "Add User",
                            fontSize = 35.sp,
                            fontFamily = kanit_bold_font,
                            color = primary_content
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        content = {
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
        },
        bottomBar = {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        adminViewModel.onAddUserButtonClick(
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
                        "Create User",
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
                value = adminViewModel.newUserName,
                onValueChange = { adminViewModel.onNewUserNameChange(it) },
                label = "Name",
                enable = true,
                placeholder = "Enter name",
                errorMessage = adminViewModel.nameError
            )
            InformationDate(
                icon = Icons.Default.Cake,
                label = "Birthday",
                placeholder = "Enter Birthday",
                onDatePick = { adminViewModel.onNewUserBirthdayPick(it) },
                errorMessage = adminViewModel.birthdayError
            )
            InformationLine(
                icon = Icons.Filled.Email,
                label = "Email",
                value = adminViewModel.newUserEmail,
                onValueChange = { adminViewModel.onNewUserEmailChange(it) },
                enable = true,
                placeholder = "Enter email",
                errorMessage = adminViewModel.emailError
            )
            InformationLine(
                icon = Icons.Filled.Phone,
                label = "Phone",
                value = adminViewModel.newUserPhone,
                onValueChange = { adminViewModel.onNewUserPhoneChange(it) },
                enable = true,
                placeholder = "Enter phone number",
                errorMessage = adminViewModel.phoneError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                )
            )
            InformationSelect(
                icon = Icons.Filled.BrokenImage,
                label = "Status",
                options = listOf("Active", "Inactive"),
                onOptionPick = { adminViewModel.onNewUserStatusPick(it) },
                errorMessage = adminViewModel.statusError
            )
            InformationSelect(
                icon = Icons.Filled.Settings,
                label = "Role",
                options = listOf("Manager", "Employee"),
                onOptionPick = { adminViewModel.onNewUserRolePick(it) },
                errorMessage = adminViewModel.roleError
            )
        }
    }
}
