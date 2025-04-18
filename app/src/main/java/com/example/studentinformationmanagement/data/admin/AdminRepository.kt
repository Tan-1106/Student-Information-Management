package com.example.studentinformationmanagement.data.admin

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class AdminRepository {
    private val db = Firebase.firestore
    private val usersCollection = db.collection("users")


    // Thêm người dùng mới
    suspend fun addUser(user: User): Result<String> {
        return try {
            val userRef = db.collection("users").document(user.userPhoneNumber)
            userRef.set(user).await()

            Result.success("User added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Thêm danh sách người dùng mới
    suspend fun addUsers(users: List<User>): Result<String> {
        return try {
            users.forEach { user ->
                val userRef = db.collection("users").document(user.userPhoneNumber)
                userRef.set(user).await() // Thêm user vào Firestore
            }
            Result.success("Users added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Lấy danh sách tất cả người dùng
    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val snapshot = usersCollection.get().await()
            val users = snapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(User::class.java)
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Lấy 1 người
    suspend fun getAnUser(phoneNumber: String): Result<User> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("userPhoneNumber", phoneNumber)
                .get()
                .await()

            val user = querySnapshot.documents.firstOrNull()?.toObject(User::class.java)

            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found with phone number: $phoneNumber"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update 1 người dùng
    suspend fun updateUser(phoneNumber: String, updatedUser: User): Result<Unit> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("userPhoneNumber", phoneNumber)
                .get()
                .await()

            val document = querySnapshot.documents.firstOrNull()

            if (document != null) {
                document.reference.set(updatedUser).await()
                Result.success(Unit)  // Cập nhật thành công
            } else {
                Result.failure(Exception("User not found with phone number: $phoneNumber"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Xóa người dùng
    suspend fun deleteUser(phoneNumber: String): Result<Unit> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("userPhoneNumber", phoneNumber)
                .get()
                .await()

            val document = querySnapshot.documents.firstOrNull()

            if (document != null) {
                document.reference.delete().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("User not found with phone number: $phoneNumber"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Lấy người dùng theo Role - Có thể được sử dụng bằng chức năng Filter
    suspend fun getUsersByRole(role: String): Result<List<User>> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("userRole", role)
                .get()
                .await()

            val userList = querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
            Result.success(userList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Lấy người dùng theo Status - Có thể được sử dụng bằng chức năng Filter
    suspend fun getUsersByStatus(status: String): Result<List<User>> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("userStatus", status)
                .get()
                .await()

            val userList = querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
            Result.success(userList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}