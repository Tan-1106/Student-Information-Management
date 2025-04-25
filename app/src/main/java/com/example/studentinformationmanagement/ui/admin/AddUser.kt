package com.example.studentinformationmanagement.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CameraAlt
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.shared.InformationSelect
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.secondary_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun AddUser(
    adminViewModel: AdminViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(modifier = Modifier.systemBarsPadding(), containerColor = Color.White, topBar = {
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
    }, bottomBar = {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    adminViewModel.onAddUserButtonClick()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = secondary_content,
                ),
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
                placeholder = "Enter name"
            )
            InformationDate(
                icon = Icons.Default.Cake,
                label = "Birthday",
                placeholder = "Enter Birthday",
                onDatePick = { adminViewModel.onNewUserBirthdayPick(it) }
            )
            InformationLine(
                icon = Icons.Filled.Email,
                label = "Email",
                value = adminViewModel.newUserEmail,
                onValueChange = { adminViewModel.onNewUserEmailChange(it) },
                enable = true,
                placeholder = "Enter email"
            )
            InformationLine(
                icon = Icons.Filled.Phone,
                label = "Phone",
                value = adminViewModel.newUserPhone,
                onValueChange = { adminViewModel.onNewUserPhoneChange(it) },
                enable = true,
                placeholder = "Enter phone number"
            )
            InformationSelect(
                icon = Icons.Filled.BrokenImage,
                label = "Status",
                options = listOf("Active", "Inactive"),
                onOptionPick = { adminViewModel.onNewUserStatusPick(it) }
            )
            InformationSelect(
                icon = Icons.Filled.Settings,
                label = "Role",
                options = listOf("Manager", "Employee"),
                onOptionPick = { adminViewModel.onNewUserRolePick(it) }
            )
        }
    }
}
