
package com.example.studentinformationmanagement.data.shared

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.mindrot.jbcrypt.BCrypt

class LoginRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun login(phoneNumber: String, password: String): Result<CurrentUser> {
        return try {
            val firestore = FirebaseFirestore.getInstance()

            // 1. Tìm credentials
            val credentialSnapshot = firestore.collection("userCredentials")
                .document(phoneNumber)
                .get()
                .await()

            if (!credentialSnapshot.exists()) {
                return Result.failure(Exception("Cannot find phone number."))
            }

            val hashedPassword = credentialSnapshot.getString("password") ?: ""

            // 2. Kiểm tra password
            if (!BCrypt.checkpw(password, hashedPassword)) {
                return Result.failure(Exception("Wrong password."))
            }

            // 3. Lấy user info
            val userSnapshot = firestore.collection("users")
                .document(phoneNumber)
                .get()
                .await()

            if (!userSnapshot.exists()) {
                return Result.failure(Exception("Cannot find user's information"))
            }

            val currentUser = CurrentUser(
                uid = phoneNumber,
                userName = userSnapshot.getString("userName") ?: "",
                userEmail = userSnapshot.getString("userEmail") ?: "",
                userPhoneNumber = phoneNumber,
                userRole = userSnapshot.getString("userRole") ?: ""
            )

            Result.success(currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
