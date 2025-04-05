package com.example.studentinformationmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentinformationmanagement.data.shared.User
import com.example.studentinformationmanagement.ui.shared.UserList
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
//            StudentInformationManagementTheme {
//                UserList(exampleUserList)}
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DatePickerExampleScreen()
                }
            }
        }
    }
}


//val exampleUserList: List<User> = listOf(
//    User(
//        userImageUrl = "https://www.svgrepo.com/show/382106/male-avatar-boy-face-man-user-9.svg",
//        userName = "Nguyen Van A",
//        userAge = "21",
//        userEmail = "vana@example.com",
//        userPhoneNumber = "0987654321",
//        userRole = "Manager",
//        userState = "Hanoi",
//    ),
//    User(
//        userImageUrl = "",
//        userName = "Tran Thi B",
//        userAge = "22",
//        userEmail = "thib@example.com",
//        userPhoneNumber = "0912345678",
//        userRole = "Manager",
//        userState = "Ho Chi Minh"
//    ),
//    User(
//        userImageUrl = "",
//        userName = "Le Van C",
//        userAge = "20",
//        userEmail = "vanc@example.com",
//        userPhoneNumber = "0909123456",
//        userRole = "Employee",
//        userState = "Da Nang"
//    ),
//    User(
//        userImageUrl = "",
//        userName = "Pham Thi D",
//        userAge = "23",
//        userEmail = "thid@example.com",
//        userPhoneNumber = "0978123456",
//        userRole = "Employee",
//        userState = "Can Tho"
//    ),
//    User(
//        userImageUrl = "",
//        userName = "Hoang Van E",
//        userAge = "19",
//        userEmail = "vane@example.com",
//        userPhoneNumber = "0932123456",
//        userRole = "Employee",
//        userState = "Hai Phong"
//    )
//)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState
        )
    }
}

@Composable
fun DatePickerExampleScreen() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }

    val dateFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDatePicker = true }) {
            Text("Select Date")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (selectedDate != null) {
                "Selected date: ${dateFormatter.format(Date(selectedDate!!))}"
            } else {
                "No date selected"
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                selectedDate = date
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerExamplePreview() {
    MaterialTheme {
        DatePickerExampleScreen()
    }
}
