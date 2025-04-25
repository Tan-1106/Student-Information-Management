package com.example.studentinformationmanagement.ui.manager

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
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
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.InformationDate
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.secondary_dark

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun EditStudent() {
    Scaffold(modifier = Modifier.systemBarsPadding(), containerColor = Color.White, topBar = {
        TopAppBar(
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Edit Student",
                        fontSize = 35.sp,
                        fontFamily = kanit_bold_font,
                        color = primary_content
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = primary_content,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        )
    }, bottomBar = {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {},
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = secondary_content,
                ),
            ) {
                Text(
                    "Update Student",
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
                .padding(5.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {
                Image(
                    painter = painterResource(R.drawable.login_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(2.dp, secondary_dark, CircleShape)
                        .size(200.dp)
                )
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = primary_content
                    )
                }
            }

            // Các trường có giá trị sẵn
            InformationLine(
                icon = Icons.Filled.Person,
                value = "Nguyen Van A",
                label = "Name",
                enable = true,
                placeholder = "Enter name"
            )
            InformationDate(
                icon = Icons.Filled.Cake,
                label = "Birthday",
                placeholder = "01/01/2000",
                onDatePick = { }
            )
            InformationLine(
                icon = Icons.Filled.Email,
                value = "vana@example.com",
                label = "Email",
                enable = true,
                placeholder = "Enter email"
            )
            InformationLine(
                icon = Icons.Filled.Phone,
                value = "0123456789",
                label = "Phone",
                enable = true,
                placeholder = "Enter phone number"
            )
            InformationLine(
                icon = Icons.Filled.FilterCenterFocus,
                value = "SV2023123",
                label = "Student ID",
                enable = true,
                placeholder = "Enter student ID"
            )
            InformationLine(
                icon = Icons.Filled.Book,
                value = "SE1701",
                label = "Class",
                enable = true,
                placeholder = "Enter class"
            )
            InformationLine(
                icon = Icons.Filled.AccountBalance,
                value = "Information Technology",
                label = "Faculty",
                enable = true,
                placeholder = "Enter faculty"
            )
        }
    }
}
