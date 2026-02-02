package com.example.studentinformationmanagement

import com.example.studentinformationmanagement.presentation.AppScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.studentinformationmanagement.presentation.theme.StudentInformationManagementTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentInformationManagementTheme {
                AppScreen()
            }
        }
    }
}

