package com.example.studentinformationmanagement.data.uiState

data class Student(
    val imageUrl: String = "https://drive.google.com/uc?id=1kem0Hwjm7UUYKHNKx4RXAkfZFCXvrxYQ",
    val name: String = "",
    val birthday: String = "",
    val email: String = "",
    val phone: String = "",
    val id: String = "",
    val stdClass: String = "",
    val faculty: String = "",
    val certificates: List<Certificate> = emptyList()
)

data class Certificate(
    val title: String = "",
    val courseName: String = "",
    val id: String = "",
    val issuingOrganization: String = "",
    val issueDate: String = "",
    val expirationDate: String = ""
)

data class ManagerUiState(
    val studentList: List<Student> = emptyList(),
    val selectedStudent: Student = Student(),
    val selectedCertificate: Certificate = Certificate(),
    val facultyList: List<String> = emptyList(),
    val classList: List<String> = emptyList()
)
