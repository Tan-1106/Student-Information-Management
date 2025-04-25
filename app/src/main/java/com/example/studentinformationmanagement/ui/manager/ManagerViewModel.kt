package com.example.studentinformationmanagement.ui.manager

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.manager.ManagerUiState
import com.example.studentinformationmanagement.data.manager.Student
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ManagerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState: StateFlow<ManagerUiState> = _uiState.asStateFlow()

    // Fetch Student List
    init {
        fetchStudentsFromFirestore()
    }
    private fun fetchStudentsFromFirestore() {
        Firebase.firestore.collection("students")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    Log.e("Firestore", "Error fetching students: ${e?.message}")
                    return@addSnapshotListener
                }

                val students = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Student::class.java)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Parsing error: ${e.message}")
                        null
                    }
                }

                _uiState.value = _uiState.value.copy(
                    userList = students
                )
            }
    }

    fun onUserEditClicked(userPhoneNumber: String) {

    }

    fun onUserSeeMoreClicked(userPhoneNumber: String) {

    }

    // Add Student
    fun onAddStudentButtonClicked(navController: NavController) {
        navController.navigate(AppScreen.AddStudent.name)
    }

    var newStudentName by mutableStateOf("")
        private set
    var newStudentEmail by mutableStateOf("")
        private set
    var newStudentPhone by mutableStateOf("")
        private set
    var newStudentBirthday by mutableStateOf("")
        private set
    var newStudentId by mutableStateOf("")
        private set
    var newStudentClass by mutableStateOf("")
        private set
    var newStudentFaculty by mutableStateOf("")
        private set

    fun onNewStudentNameChange(userInput: String) {
        newStudentName = userInput
    }
    fun onNewStudentEmailChange(userInput: String) {
        newStudentEmail = userInput
    }
    fun onNewStudentPhoneChange(userInput: String) {
        newStudentPhone = userInput
    }
    fun onNewStudentIdChange(userInput: String) {
        newStudentId = userInput
    }
    fun onNewStudentClassChange(userInput: String) {
        newStudentClass = userInput
    }
    fun onNewStudentBirthdayPick(userInput: String) {
        newStudentBirthday = userInput
    }
    fun onNewStudentFacultyChange(userInput: String) {
        newStudentFaculty = userInput
    }
    fun clearAddStudentInputs() {
        newStudentName = ""
        newStudentEmail = ""
        newStudentPhone = ""
        newStudentId = ""
        newStudentClass = ""
        newStudentBirthday = ""
        newStudentFaculty = ""
    }

    var invalidMessage by mutableStateOf("")
        private set
    fun onAddStudentButtonClick() {
        val db = Firebase.firestore

        val id = newStudentId.trim()
        val email = newStudentEmail.trim()
        val phone = newStudentPhone.trim()

        if (newStudentName == "" || newStudentEmail == "" ||
            newStudentPhone == "" || newStudentId == "" ||
            newStudentClass == "" || newStudentBirthday == "" ||
            newStudentFaculty == ""
            ) {
            invalidMessage = "All fields must be filled"
            return
        }

        db.collection("students")
            .whereIn("studentId", listOf(id))
            .get()
            .addOnSuccessListener { idResult ->
                if (idResult.isEmpty) {
                    db.collection("students")
                        .whereIn("studentEmail", listOf(email))
                        .get()
                        .addOnSuccessListener { emailResult ->
                            if (emailResult.isEmpty) {
                                db.collection("students")
                                    .whereIn("studentPhoneNumber", listOf(phone))
                                    .get()
                                    .addOnSuccessListener { phoneResult ->
                                        if (phoneResult.isEmpty) {
                                            val newStudent = Student(
                                                studentName = newStudentName,
                                                studentBirthday = newStudentBirthday,
                                                studentEmail = email,
                                                studentPhoneNumber = phone,
                                                studentId = id,
                                                studentClass = newStudentClass,
                                                studentFaculty = newStudentFaculty
                                            )

                                            db.collection("students")
                                                .document(id)
                                                .set(newStudent)
                                                .addOnSuccessListener {
                                                    invalidMessage = ""
                                                    clearAddStudentInputs()
                                                }

                                                .addOnFailureListener { e ->
                                                    invalidMessage = "Cannot add student."
                                                }
                                        } else {
                                            invalidMessage = "Phone number is existed."
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        invalidMessage = "Cannot check existing phone number."
                                    }
                            } else {
                                invalidMessage = "Email is existed."
                            }
                        }
                        .addOnFailureListener { e ->
                            invalidMessage = "Cannot check existing email."
                        }
                } else {
                    invalidMessage = "Student's ID is existed."
                }
            }
            .addOnFailureListener { e ->
                invalidMessage = "Cannot check existing student's ID."
            }
    }
}