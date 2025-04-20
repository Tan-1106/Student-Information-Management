package com.example.studentinformationmanagement.ui.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.studentinformationmanagement.data.admin.AdminRepository
import com.example.studentinformationmanagement.data.admin.AdminUiState
import com.example.studentinformationmanagement.data.admin.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AdminViewModel : ViewModel() {
    private val repository = AdminRepository()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    private var allUserList: List<User> = emptyList()
    private var currentSearchQuery: String = ""

    init {
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        Firebase.firestore.collection("users")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    Log.e("Firestore", "Error fetching users: ${e?.message}")
                    return@addSnapshotListener
                }

                val users = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(User::class.java)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Parsing error: ${e.message}")
                        null
                    }
                }

                allUserList = users
                // applySearch(currentSearchQuery)
            }
    }

//    fun onUserSearch(userInput: String) {
//        currentSearchQuery = userInput
//        applySearch(userInput)
//    }
//
//    private fun applySearch(query: String) {
//        val filteredList = if (query == "") {
//            allUserList
//        } else {
//            allUserList.filter { user ->
//                user.userName.contains(query.trim(), ignoreCase = true) || user.userPhoneNumber.contains(query.trim(), ignoreCase = true)
//            }
//        }
//
//        _uiState.update { currentState ->
//            currentState.copy(userList = filteredList)
//        }
//    }


    fun onUserEditClicked(userPhoneNumber: String) {
        // TODO: Xử lý sự kiện chỉnh sửa user
    }

    fun onUserSeeMoreClicked(userPhoneNumber: String) {
        // TODO: Xử lý sự kiện xem thêm user
    }
}
