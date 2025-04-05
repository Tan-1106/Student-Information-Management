package com.example.studentinformationmanagement.data.manager

data class Student(
    val studentImageUrl: String,
    val studentName: String,
    val studentAge: String,
    val studentEmail: String,
    val studentPhoneNumber: String,
    val studentId: String,
    val studentClass: String,
    val studentFaculty: String
)

data class ManagerUiState(
    val userList: List<Student> = emptyList()
)
