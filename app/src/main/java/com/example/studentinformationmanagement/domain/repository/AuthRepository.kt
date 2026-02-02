package com.example.studentinformationmanagement.domain.repository

import com.example.studentinformationmanagement.domain.model.User

interface AuthRepository {
    // Check current user
    suspend fun getCurrentUser(): Result<User?>

    // Login
    suspend fun login(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Result<User?>

    // Reset password email sending
    suspend fun sendResetEmail(email: String): Result<Unit>

    // Logout
    suspend fun logout(): Result<Unit>
}