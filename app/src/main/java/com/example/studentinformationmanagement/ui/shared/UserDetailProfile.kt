package com.example.studentinformationmanagement.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentinformationmanagement.ui.theme.primary_content

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun UserDetailProfile(

){
    DetailProfile(
        topBar = {
            TopAppBar(navigationIcon = {
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
                        // Xử lý sự kiện
                    }
                )
            }, title = {}, actions = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = primary_content,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = primary_content
                    )
                }
            })
        },
        modifier = TODO(),
        user = TODO()
    )
}