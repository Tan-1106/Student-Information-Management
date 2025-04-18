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

    val exampleStudentList: List<Student> = listOf(
        Student(
            studentImageUrl = "",
            studentName = "Nguyen Van A",
            studentAge = "20",
            studentEmail = "vana@student.com",
            studentPhoneNumber = "0987654321",
            studentId = "SV001",
            studentClass = "CNTT1",
            studentFaculty = "Công nghệ thông tin"
        ),
        Student(
            studentImageUrl = "",
            studentName = "Tran Thi B",
            studentAge = "21",
            studentEmail = "thib@student.com",
            studentPhoneNumber = "0912345678",
            studentId = "SV002",
            studentClass = "CNTT2",
            studentFaculty = "Công nghệ thông tin"
        ),
        Student(
            studentImageUrl = "",
            studentName = "Le Van C",
            studentAge = "22",
            studentEmail = "vanc@student.com",
            studentPhoneNumber = "0909123456",
            studentId = "SV003",
            studentClass = "KT1",
            studentFaculty = "Kế toán"
        ),
        Student(
            studentImageUrl = "",
            studentName = "Pham Thi D",
            studentAge = "20",
            studentEmail = "thid@student.com",
            studentPhoneNumber = "0978123456",
            studentId = "SV004",
            studentClass = "QTKD1",
            studentFaculty = "Quản trị kinh doanh"
        ),
        Student(
            studentImageUrl = "",
            studentName = "Hoang Van E",
            studentAge = "19",
            studentEmail = "vane@student.com",
            studentPhoneNumber = "0932123456",
            studentId = "SV005",
            studentClass = "DL1",
            studentFaculty = "Du lịch"
        )
    )
}