package com.example.studentinformationmanagement.domain.usecase

import javax.inject.Inject
import com.example.studentinformationmanagement.domain.model.User
import com.example.studentinformationmanagement.domain.repository.AuthRepository

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Result<User?> {
        return authRepository.login(
            email = email,
            password = password,
            rememberMe = rememberMe
        )
    }
}