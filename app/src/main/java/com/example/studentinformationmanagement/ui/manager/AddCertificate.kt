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
import androidx.compose.material.icons.filled.*
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
import com.example.studentinformationmanagement.ui.shared.InformationLine
import com.example.studentinformationmanagement.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun AddCertificate() {
    Scaffold(modifier = Modifier.systemBarsPadding(), containerColor = Color.White, topBar = {
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
                )
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
            Box(contentAlignment = Alignment.BottomCenter) {
                Image(
                    painter = painterResource(R.drawable.login_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(shape = CircleShape, color = secondary_dark, width = 2.dp)
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
            // Các trường thông tin cho chứng chỉ
            InformationLine(
                icon = Icons.Filled.Bookmark,
                value = "",
                label = "Certificate Name",
                enable = true,
                placeholder = "Enter certificate name"
            )
            InformationLine(
                icon = Icons.Filled.Person,
                value = "",
                label = "Student Name",
                enable = true,
                placeholder = "Enter student name"
            )
            InformationLine(
                icon = Icons.Filled.FilterCenterFocus,
                value = "",
                label = "Student ID",
                enable = true,
                placeholder = "Enter student ID"
            )
            InformationLine(
                icon = Icons.AutoMirrored.Filled.EventNote,
                value = "",
                label = "Session",
                enable = true,
                placeholder = "Enter session"
            )
            InformationLine(
                icon = Icons.Filled.OtherHouses,
                value = "",
                label = "Organization",
                enable = true,
                placeholder = "Enter organization"
            )
            InformationLine(
                icon = Icons.Filled.Description,
                value = "",
                label = "Description",
                enable = true,
                placeholder = "Enter description"
            )
            InformationLine(
                icon = Icons.Filled.AccountBalance,
                value = "",
                label = "School",
                enable = true,
                placeholder = "Enter school"
            )
        }
    }
}
