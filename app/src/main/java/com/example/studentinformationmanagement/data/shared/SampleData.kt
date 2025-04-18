package com.example.studentinformationmanagement.data.shared

import com.example.studentinformationmanagement.data.admin.User
import com.example.studentinformationmanagement.data.manager.Student

object SampleData {
    val sampleUserList: List<User> = listOf(
        User(
            userImageUrl = "",
            userName = "Nguyen Van A",
            userBirthday = "21",
            userEmail = "vana@example.com",
            userPhoneNumber = "0987654321",
            userRole = "Manager",
            userStatus = "Normal",
        ),
        User(
            userImageUrl = "",
            userName = "Tran Thi B",
            userBirthday = "22",
            userEmail = "thib@example.com",
            userPhoneNumber = "0912345678",
            userRole = "Manager",
            userStatus = "Locked"
        ),
        User(
            userImageUrl = "",
            userName = "Le Van C",
            userBirthday = "20",
            userEmail = "vanc@example.com",
            userPhoneNumber = "0909123456",
            userRole = "Employee",
            userStatus = "Normal"
        ),
        User(
            userImageUrl = "",
            userName = "Pham Thi D",
            userBirthday = "23",
            userEmail = "thid@example.com",
            userPhoneNumber = "0978123456",
            userRole = "Employee",
            userStatus = "Locked"
        ),
        User(
            userImageUrl = "",
            userName = "Hoang Van E",
            userBirthday = "19",
            userEmail = "vane@example.com",
            userPhoneNumber = "0932123456",
            userRole = "Employee",
            userStatus = "Normal"
        )
    )
}