package com.example.studentinformationmanagement.data.shared

import com.example.studentinformationmanagement.data.admin.User

object SampleData {
    val sampleUserList: List<User> = listOf(
        User(
            userImageUrl = "",
            userName = "Nguyen Van A",
            userAge = "21",
            userEmail = "vana@example.com",
            userPhoneNumber = "0987654321",
            userRole = "Manager",
            userState = "Normal",
        ),
        User(
            userImageUrl = "",
            userName = "Tran Thi B",
            userAge = "22",
            userEmail = "thib@example.com",
            userPhoneNumber = "0912345678",
            userRole = "Manager",
            userState = "Locked"
        ),
        User(
            userImageUrl = "",
            userName = "Le Van C",
            userAge = "20",
            userEmail = "vanc@example.com",
            userPhoneNumber = "0909123456",
            userRole = "Employee",
            userState = "Normal"
        ),
        User(
            userImageUrl = "",
            userName = "Pham Thi D",
            userAge = "23",
            userEmail = "thid@example.com",
            userPhoneNumber = "0978123456",
            userRole = "Employee",
            userState = "Locked"
        ),
        User(
            userImageUrl = "",
            userName = "Hoang Van E",
            userAge = "19",
            userEmail = "vane@example.com",
            userPhoneNumber = "0932123456",
            userRole = "Employee",
            userState = "Normal"
        )
    )
}