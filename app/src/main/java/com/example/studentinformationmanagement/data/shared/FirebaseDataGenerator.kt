package com.example.studentinformationmanagement.data.shared
import com.example.studentinformationmanagement.data.manager.Certificate
import com.example.studentinformationmanagement.data.manager.Student
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirebaseDataGenerator {
    private val db = FirebaseFirestore.getInstance()

    suspend fun generateSampleData() {
        for (i in 1..10) {
            // Tạo một sinh viên
            val studentId = "studentId_$i"
            val studentCertificates = generateCertificatesForStudent(i)

            val student = Student(
                studentImageUrl = "https://example.com/student_$i.jpg",
                studentName = "Student $i",
                studentBirthday = "2000-01-01",
                studentEmail = "student$i@example.com",
                studentPhoneNumber = "0123456789$i",
                studentId = studentId,
                studentClass = "Class ${i % 3 + 1}",
                studentFaculty = "Faculty ${i % 2 + 1}",
                studentCertificates = studentCertificates
            )

            val studentDocRef = db.collection("students").document(studentId)
            studentDocRef.set(student).await()

            addCertificatesToFirestore(studentDocRef, studentCertificates)
        }
    }

    private fun generateCertificatesForStudent(studentIndex: Int): List<Certificate> {
        return List(3) {
            Certificate(
                certificateTitle = "Certificate for Student $studentIndex - ${it + 1}",
                courseName = "Course ${it + 1}",
                issueDate = "2021-12-01",
                issuingOrganization = "Organization ${it + 1}",
                certificateId = "cert_${studentIndex}_${it + 1}",
                expirationDate = "2023-12-01"
            )
        }
    }

    private suspend fun addCertificatesToFirestore(studentDocRef: DocumentReference, certificates: List<Certificate>) {
        for (certificate in certificates) {
            studentDocRef.collection("certificates").document(certificate.certificateId).set(certificate).await()
        }
    }
}