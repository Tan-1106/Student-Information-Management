package com.example.studentinformationmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.studentinformationmanagement.ui.theme.StudentInformationManagementTheme

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
                AppScreen()
            }
        }
    }
}

