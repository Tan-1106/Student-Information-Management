package com.example.studentinformationmanagement.data.manager

data class Student(
    val studentImageUrl: String,
    val studentName: String,
    val studentBirthday: String,
    val studentEmail: String,
    val studentPhoneNumber: String,
    val studentId: String,
    val studentClass: String,
    val studentFaculty: String,
    val studentCertificates: List<Certificate>
)

data class Certificate(
    val certificateTitle: String,
    val courseName: String,
    val issueDate: String,
    val issuingOrganization: String,
    val certificateId: String,
    val expirationDate: String
)

data class ManagerUiState(
    val userList: List<Student> = emptyList()
)
