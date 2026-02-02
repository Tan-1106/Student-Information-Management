package com.example.studentinformationmanagement.domain.usecase

import com.example.studentinformationmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class SendResetEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Boolean> {
        return authRepository.sendResetEmail(email)
    }
}