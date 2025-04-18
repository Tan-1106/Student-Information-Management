package com.example.studentinformationmanagement.ui.manager

import android.util.Log
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

    fun onAddButtonClicked(navController: NavController) {
        navController.navigate(AppScreen.AddStudent.name)
    }
}