package com.example.studentinformationmanagement.data.manager

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import android.util.Log

class ManagerRepository {
    val db = Firebase.firestore
    val studentsCollection = db.collection("students")

    // Upload Sample Student List to FireStore
    suspend fun uploadStudentsToFirestore(studentList: List<Student>) {
        for (student in studentList) {
            try {
                studentsCollection
                    .document(student.studentId)  // dùng studentId làm documentId
                    .set(student)
                    .await()
                Log.d("FirestoreUpload", "Uploaded: ${student.studentId}")
            } catch (e: Exception) {
                Log.e("FirestoreUpload", "Failed to upload ${student.studentId}", e)
            }
        }
    }
}