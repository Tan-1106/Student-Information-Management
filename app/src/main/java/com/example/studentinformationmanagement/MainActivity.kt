package com.example.studentinformationmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
<<<<<<< Updated upstream
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.studentinformationmanagement.ui.admin.AdminScreen
import com.example.studentinformationmanagement.ui.admin.BottomNavBarAdmin
=======
import com.example.studentinformationmanagement.data.shared.User
import com.example.studentinformationmanagement.ui.shared.UserList
import com.example.studentinformationmanagement.ui.shared.exampleUserList
>>>>>>> Stashed changes
import com.example.studentinformationmanagement.ui.theme.StudentInformationManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentInformationManagementTheme {
                AppScreen()
                UserList(exampleUserList)
            }
        }
    }
}


<<<<<<< Updated upstream
=======
val exampleUserList: List<User> = listOf(
    User(
        userImageUrl = "https://www.svgrepo.com/show/382106/male-avatar-boy-face-man-user-9.svg",
        userName = "Nguyen Van A",
        userAge = "21",
        userEmail = "vana@example.com",
        userPhoneNumber = "0987654321",
        userRole = "Manager",
        userState = "Hanoi",
    ),
    User(
        userImageUrl = "",
        userName = "Tran Thi B",
        userAge = "22",
        userEmail = "thib@example.com",
        userPhoneNumber = "0912345678",
        userRole = "Manager",
        userState = "Ho Chi Minh"
    ),
    User(
        userImageUrl = "",
        userName = "Le Van C",
        userAge = "20",
        userEmail = "vanc@example.com",
        userPhoneNumber = "0909123456",
        userRole = "Employee",
        userState = "Da Nang"
    ),
    User(
        userImageUrl = "",
        userName = "Pham Thi D",
        userAge = "23",
        userEmail = "thid@example.com",
        userPhoneNumber = "0978123456",
        userRole = "Employee",
        userState = "Can Tho"
    ),
    User(
        userImageUrl = "",
        userName = "Hoang Van E",
        userAge = "19",
        userEmail = "vane@example.com",
        userPhoneNumber = "0932123456",
        userRole = "Employee",
        userState = "Hai Phong"
    )
)
>>>>>>> Stashed changes

