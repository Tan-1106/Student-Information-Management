package com.example.studentinformationmanagement.data.shared

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class LoginRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun loginEmail() {

    }
}