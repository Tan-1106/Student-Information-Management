package com.example.studentinformationmanagement.ui.admin

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentinformationmanagement.data.admin.AdminRepository
import com.example.studentinformationmanagement.data.admin.AdminUiState
import com.example.studentinformationmanagement.data.admin.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

class AdminViewModel : ViewModel() {
    private val repository = AdminRepository()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    // Realtime User List
    private val _userList = mutableStateOf<List<User>>(emptyList())
    val userList: State<List<User>> = _userList

    init {
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        // Dùng realtime Firestore listener giống ManagerViewModel
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

                _userList.value = users

                // Đồng bộ luôn vào AdminUiState nếu cần
                _uiState.value = _uiState.value.copy(userList = users)
            }
    }

    fun onUserEditClicked(userPhoneNumber: String) {
        // TODO: Xử lý sự kiện chỉnh sửa user
    }

    fun onUserSeeMoreClicked(userPhoneNumber: String) {
        // TODO: Xử lý sự kiện xem thêm user
    }
}
