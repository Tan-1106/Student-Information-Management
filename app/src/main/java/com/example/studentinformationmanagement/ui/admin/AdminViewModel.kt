package com.example.studentinformationmanagement.ui.admin

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.data.admin.AdminUiState
import com.example.studentinformationmanagement.data.admin.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import com.example.studentinformationmanagement.AppScreen


class AdminViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    // Fetching user list
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

                _uiState.value = _uiState.value.copy(
                    userList = users
                )
            }
    }

    var searchBarValue by mutableStateOf("")
        private set
    fun onUserSearch(userInput: String) {
        searchBarValue = userInput
        // TODO: Xử lý sự kiện tìm kiếm

    }

    fun onAddButtonClicked(navController: NavHostController) {
        navController.navigate(AppScreen.AddUser.name)
    }

    fun onUserEditClicked(userPhoneNumber: String) {
        // TODO: Xử lý sự kiện chỉnh sửa user
    }

    fun onUserSeeMoreClicked(userPhoneNumber: String) {
        // TODO: Xử lý sự kiện xem thêm user
    }

    // Add User
    var newUserName by mutableStateOf("")
        private set
    var newUserEmail by mutableStateOf("")
        private set
    var newUserPhone by mutableStateOf("")
        private set
    var newUserBirthday by mutableStateOf("")
        private set
    var newUserStatus by mutableStateOf("")
        private set
    var newUserRole by mutableStateOf("")
        private set

    fun onNewUserNameChange(userInput: String) {
        newUserName = userInput
    }
    fun onNewUserEmailChange(userInput: String) {
        newUserEmail = userInput
    }
    fun onNewUserPhoneChange(userInput: String) {
        newUserPhone = userInput
    }
    fun onNewUserBirthdayPick(userInput: String) {
        newUserBirthday = userInput
    }
    fun onNewUserStatusPick(userInput: String) {
        newUserStatus = userInput
    }
    fun onNewUserRolePick(userInput: String) {
        newUserRole = userInput
    }

    fun onAddUserButtonClick() {

    }
}
