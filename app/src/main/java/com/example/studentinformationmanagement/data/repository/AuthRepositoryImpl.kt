package com.example.studentinformationmanagement.data.repository

import com.example.studentinformationmanagement.data.local.AuthLocalDataSource
import com.example.studentinformationmanagement.data.mapper.toEntity
import com.example.studentinformationmanagement.data.remote.AuthRemoteDataSource
import com.example.studentinformationmanagement.domain.model.User
import com.example.studentinformationmanagement.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
): AuthRepository {
    // Login
    override suspend fun login(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Result<User?> {
        val authenticatedUser = authRemoteDataSource.authenticateUser(email, password)
        if (authenticatedUser.isFailure) {
            return Result.failure(authenticatedUser.exceptionOrNull()!!)
        }

        authLocalDataSource.setRememberMe(rememberMe)
        val user = authenticatedUser.getOrNull()?.toEntity()
        return Result.success(user)
    }

    // Reset password email sending
    override suspend fun sendResetEmail(email: String): Result<Unit> {
        val result = authRemoteDataSource.sendResetPasswordEmail(email)
        return result
    }

    // Logout
    override suspend fun logout(): Result<Unit> {
        val result = authRemoteDataSource.logoutUser()
        authLocalDataSource.setRememberMe(false)
        return result
    }
}