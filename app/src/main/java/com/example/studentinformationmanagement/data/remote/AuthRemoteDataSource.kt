package com.example.studentinformationmanagement.data.remote

import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.studentinformationmanagement.data.dto.UserDTO

interface AuthRemoteDataSource {
    suspend fun getCurrentUser(): Result<UserDTO?>
    suspend fun authenticateUser(email: String, password: String): Result<UserDTO?>
    suspend fun sendResetPasswordEmail(email: String): Result<Unit>
    fun logoutUser(): Result<Unit>
}

class AuthRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthRemoteDataSource {
    companion object {
        const val USERS_COLLECTION = "users"
    }

    override suspend fun getCurrentUser(): Result<UserDTO?> {
        return try {
            val currentUser = firebaseAuth.currentUser
            val userEmail = currentUser?.email

            val userDocument = firebaseFirestore.collection(USERS_COLLECTION)
                .document(userEmail ?: "")
                .get()
                .await()
            val user = userDocument.toObject(UserDTO::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun authenticateUser(email: String, password: String): Result<UserDTO?> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userEmail = authResult.user?.email

            val userDocument = firebaseFirestore.collection(USERS_COLLECTION)
                .document(userEmail ?: "")
                .get()
                .await()
            val user = userDocument.toObject(UserDTO::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendResetPasswordEmail(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logoutUser(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}