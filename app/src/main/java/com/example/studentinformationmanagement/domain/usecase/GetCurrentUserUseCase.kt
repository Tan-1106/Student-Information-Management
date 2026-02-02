package com.example.studentinformationmanagement.domain.usecase

import com.example.studentinformationmanagement.domain.model.User
import com.example.studentinformationmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User?> {
        return authRepository.getCurrentUser()
    }
}