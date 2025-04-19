package com.example.studentinformationmanagement.ui.shared

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.studentinformationmanagement.data.admin.User
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.State


class UserDetailViewModel: ViewModel() {
    private  val  firestore = FirebaseFirestore.getInstance()

    private  val  _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    fun fetchUser() {
        val userPhoneNumber = "0901234567"

        firestore.collection("users")
            .document(userPhoneNumber)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fetchedUser = document.toObject(User::class.java)
                    _user.value = fetchedUser
                } else {
                    // Không có user này
                }
            }
            .addOnFailureListener { exception ->
                // Xử lý lỗi nếu cần
            }
    }

}