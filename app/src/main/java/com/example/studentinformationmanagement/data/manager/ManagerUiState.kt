package com.example.studentinformationmanagement.data.manager

data class Student(
    val studentImageUrl: String = "https://drive.google.com/uc?id=1kem0Hwjm7UUYKHNKx4RXAkfZFCXvrxYQ",
    val studentName: String = "",
    val studentBirthday: String = "",
    val studentEmail: String = "",
    val studentPhoneNumber: String = "",
    val studentId: String = "",
    val studentClass: String = "",
    val studentFaculty: String = "",
    val studentCertificates: List<Certificate> = emptyList()
)

data class Certificate(
    val certificateTitle: String = "",
    val courseName: String = "",
    val issueDate: String = "",
    val issuingOrganization: String = "",
    val certificateId: String = "",
    val expirationDate: String = ""
)

data class ManagerUiState(
    val userList: List<Student> = emptyList()
)
