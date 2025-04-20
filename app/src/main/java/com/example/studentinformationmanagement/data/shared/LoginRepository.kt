
package com.example.studentinformationmanagement.data.shared

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.mindrot.jbcrypt.BCrypt

class LoginRepository {
    // Login and authorization
    suspend fun login(
        phoneNumber: String, password: String
    ): Result<CurrentUser> {
        return try {
            val firestore = FirebaseFirestore.getInstance()

            // Get userCredentials data
            val credentialSnapshot = firestore.collection("userCredentials")
                .document(phoneNumber)
                .get()
                .await()

            // Check exist phone number
            if (!credentialSnapshot.exists()) {
                return Result.failure(Exception("Cannot find phone number."))
            }

            // Hash the password and checking
            val hashedPassword = credentialSnapshot.getString("password") ?: ""
            if (!BCrypt.checkpw(password, hashedPassword)) {
                return Result.failure(Exception("Wrong password."))
            }

            // Get user's information
            val userSnapshot = firestore.collection("users")
                .document(phoneNumber)
                .get()
                .await()
            if (!userSnapshot.exists()) {
                return Result.failure(Exception("Cannot find user's information"))
            }

            // Get and return the logged in user's information
            val currentUser = CurrentUser(
                userImageUrl = userSnapshot.getString("userImageUrl") ?: "",
                userName = userSnapshot.getString("userName") ?: "",
                userBirthday = userSnapshot.getString("userBirthday") ?: "",
                userEmail = userSnapshot.getString("userEmail") ?: "",
                userPhoneNumber = userSnapshot.getString("userPhoneNumber") ?: "",
                userRole = userSnapshot.getString("userRole") ?: "",
                userStatus = userSnapshot.getString("userStatus") ?: ""
            )
            Result.success(currentUser)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
