package com.example.studentinformationmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studentinformationmanagement.ui.admin.AdminScreen
import com.example.studentinformationmanagement.ui.theme.StudentInformationManagementTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import com.example.studentinformationmanagement.ui.theme.StudentInformationManagementTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            StudentInformationManagementTheme {
//                AppScreen()
//            }
//        }
//    }
//}
//
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentInformationManagementTheme {
//                AppScreen()
                AdminScreen()
            }
        }
    }
}

