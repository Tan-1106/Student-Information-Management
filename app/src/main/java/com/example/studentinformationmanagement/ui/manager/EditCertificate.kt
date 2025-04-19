package com.example.studentinformationmanagement.ui.manager


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentinformationmanagement.R
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.kanit_bold_font
import com.example.studentinformationmanagement.ui.theme.primary_content
import com.example.studentinformationmanagement.ui.theme.primary_dark
import com.example.studentinformationmanagement.ui.theme.secondary_content
import com.example.studentinformationmanagement.ui.theme.secondary_dark

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun EditCertificate() {
    Scaffold(modifier = Modifier.systemBarsPadding(), containerColor = Color.White, topBar = {
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
                IconButton(onClick = {}) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = primary_content,
                        modifier = Modifier.size(40.dp)
                    )
                }
            },
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
                    "Update Certificate",
                    style = TextStyle(color = primary_dark, fontFamily = kanit_bold_font)
                )
            }
        }
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(5.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hình ảnh
            Image(
                painter = painterResource(R.drawable.login_image),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .border(2.dp, secondary_dark, CircleShape)
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Các trường thông tin (giá trị mặc định)
            InformationLine(
                icon = Icons.Filled.Badge,
                value = "Java Programming",
                label = "Certificate Name",
                enable = true,
                placeholder = "Enter certificate name"
            )
            InformationLine(
                icon = Icons.Filled.Person,
                value = "Nguyen Van A",
                label = "Student Name",
                enable = true,
                placeholder = "Enter student name"
            )
            InformationLine(
                icon = Icons.Filled.Badge,
                value = "SV123456",
                label = "Student ID",
                enable = true,
                placeholder = "Enter student ID"
            )
            InformationLine(
                icon = Icons.AutoMirrored.Filled.EventNote,
                value = "2024 Spring",
                label = "Session",
                enable = true,
                placeholder = "Enter session"
            )
            InformationLine(
                icon = Icons.Filled.OtherHouses,
                value = "FPT Software",
                label = "Organization",
                enable = true,
                placeholder = "Enter organization"
            )
            InformationLine(
                icon = Icons.Filled.Description,
                value = "Excellent student in Java",
                label = "Description",
                enable = true,
                placeholder = "Enter description"
            )
            InformationLine(
                icon = Icons.Filled.Book,
                value = "FPT University",
                label = "School",
                enable = true,
                placeholder = "Enter school"
            )
        }
    }
}
