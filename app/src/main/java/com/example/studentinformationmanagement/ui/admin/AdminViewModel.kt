package com.example.studentinformationmanagement.ui.admin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.admin.AdminUiState
import com.example.studentinformationmanagement.data.admin.User
import com.example.studentinformationmanagement.data.shared.CurrentUser
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class AdminViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    // Fetching user list
    init {
        fetchUsersFromFirestore()
    }
    private var fullUserList: List<User> = emptyList()
    private fun fetchUsersFromFirestore() {
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

                _uiState.update { currentState ->
                    currentState.copy(
                        userList = users
                    )
                }
                // For search function
                fullUserList = users
            }
    }

    // Search bar
    var searchInput by mutableStateOf("")
        private set
    fun onUserSearch(userSearchInput: String) {
        searchInput = userSearchInput

        val keyword = searchInput.trim().lowercase()

        if (keyword.isEmpty()) {
            fetchUsersFromFirestore()
        } else {
            val filteredList = fullUserList.filter { user ->
                user.userName.contains(keyword, ignoreCase = true) || user.userEmail.contains(keyword, ignoreCase = true) ||
                        user.userPhoneNumber.contains(keyword, ignoreCase = true) || user.userStatus.contains(keyword, ignoreCase = true) ||
                                user.userRole.contains(keyword, ignoreCase = true)
            }
            _uiState.update { currentState ->
                currentState.copy(
                    userList = filteredList
                )
            }
        }
    }

    // Filter feature
    var isShowDialog by mutableStateOf(false)
        private set
    private var sortSelected by mutableStateOf("")
    private var roleSelected by mutableStateOf("")
    private var statusSelected by mutableStateOf("")

    fun onSortSelected(userInput: String) {
        sortSelected = userInput
    }
    fun onRoleSelected(userInput: String) {
        roleSelected = userInput
    }
    fun onStatusSelected(userInput: String) {
        statusSelected = userInput
    }

    fun onFilterClick() {
        isShowDialog = true
    }
    fun onDismissFilterClick() {
        isShowDialog = false
    }
    fun onApplyFilterClick() {
        var filtered = fullUserList

        if (roleSelected.isNotBlank()) {
            filtered = filtered.filter { it.userRole == roleSelected }
        }

        if (statusSelected.isNotBlank()) {
            filtered = filtered.filter { it.userStatus == statusSelected }
        }

        filtered = when (sortSelected) {
            "A → Z" -> filtered.sortedBy { it.userName }
            "Z → A" -> filtered.sortedByDescending { it.userName }
            else -> filtered
        }

        _uiState.update { currentState ->
            currentState.copy(userList = filtered)
        }

        isShowDialog = false
    }

    fun onClearFilterClick() {
        sortSelected = ""
        roleSelected = ""
        statusSelected = ""

        _uiState.update { currentState ->
            currentState.copy(userList = fullUserList)
        }
        isShowDialog = false
    }

    // User's detail profile
    var userToView by mutableStateOf(CurrentUser())
        private set

    fun onUserSeeMoreClicked(
        userPhoneNumber: String,
        navController: NavHostController
    ) {
        val selectedUser = _uiState.value.userList.find { it.userPhoneNumber == userPhoneNumber }
        userToView = CurrentUser(
            userImageUrl = selectedUser?.userImageUrl ?: "https://drive.google.com/uc?id=1XGMQWOMTV5lxHGLpYQMk--3Zqm7-iEtK",
            userName = selectedUser?.userName ?: "",
            userBirthday = selectedUser?.userBirthday ?: "",
            userEmail = selectedUser?.userEmail ?: "",
            userPhoneNumber = selectedUser?.userPhoneNumber ?: "",
            userRole = selectedUser?.userRole ?: "",
            userStatus = selectedUser?.userStatus ?: "",
        )

        navController.navigate(AppScreen.UserDetailProfile.name)
    }
    fun clearUserToView() {
        userToView = CurrentUser()
    }

    // Add User
    fun onAddUserButtonClicked(
        navController: NavHostController
    ) {
        navController.navigate(AppScreen.AddUser.name)
    }

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

    fun clearAddUserInputs() {
        newUserName = ""
        newUserEmail = ""
        newUserPhone = ""
        newUserBirthday = ""
        newUserStatus = ""
        newUserRole = ""
    }
    var nameError by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set
    var phoneError by mutableStateOf("")
        private set
    var birthdayError by mutableStateOf("")
        private set
    var statusError by mutableStateOf("")
        private set
    var roleError by mutableStateOf("")
        private set
    fun onAddUserButtonClick(
        navController: NavHostController,
        context: Context
    ) {
        if (validateUserInputs()) {
            val db = Firebase.firestore

            val name = newUserName.trim()
            val email = newUserEmail.trim()
            val phone = newUserPhone.trim()
            val birthday = newUserBirthday.trim()
            val role = newUserRole.trim()
            val status = newUserStatus.trim()

            db.collection("users")
                .whereEqualTo("userPhoneNumber", phone)
                .get()
                .addOnSuccessListener { phoneResult ->
                    if (phoneResult.isEmpty) {
                        db.collection("users")
                            .whereEqualTo("userEmail", email)
                            .get()
                            .addOnSuccessListener { emailResult ->
                                if (emailResult.isEmpty) {
                                    val newUser = User(
                                        userName = name,
                                        userEmail = email,
                                        userPhoneNumber = phone,
                                        userBirthday = birthday,
                                        userRole = role,
                                        userStatus = status
                                    )

                                    db.collection("users")
                                        .document(phone)
                                        .set(newUser)
                                        .addOnSuccessListener {
                                            nameError = ""
                                            emailError = ""
                                            phoneError = ""
                                            birthdayError = ""
                                            statusError = ""
                                            roleError = ""

                                            clearAddUserInputs()
                                            navController.navigateUp()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Cannot add user", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    emailError = "Email is existed"
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Cannot check existing email", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        phoneError = "Phone number is existed"
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Cannot check existing phone number", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun validateUserInputs(): Boolean {
        var isValid = true

        if (newUserName.trim().isEmpty()) {
            nameError = "Name is required"
            isValid = false
        } else {
            nameError = ""
        }

        if (newUserEmail.trim().isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newUserEmail.trim()).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = ""
        }

        if (newUserPhone.trim().isEmpty()) {
            phoneError = "Phone number is required"
            isValid = false
        } else if (newUserPhone.trim().length != 10) {
            phoneError = "Invalid phone number"
            isValid = false
        }
        else {
            phoneError = ""
        }

        if (newUserBirthday.trim().isEmpty()) {
            birthdayError = "Birthday is required"
            isValid = false
        } else {
            birthdayError = ""
        }

        if (newUserStatus.trim().isEmpty()) {
            statusError = "Status is required"
            isValid = false
        } else {
            statusError = ""
        }

        if (newUserRole.trim().isEmpty()) {
            roleError = "Role is required"
            isValid = false
        } else {
            roleError = ""
        }
        return isValid
    }

    // Delete a user
    fun onDeleteUser(userPhoneNumber: String) {
        val db = Firebase.firestore

        db.collection("users")
            .document(userPhoneNumber)
            .delete()
            .addOnSuccessListener {
                Log.d("DeleteUser", "Successfully deleted user with phone: $userPhoneNumber")
                fetchUsersFromFirestore()
            }
            .addOnFailureListener { e ->
                Log.e("DeleteUser", "Error deleting user: ${e.message}")
            }
    }
}
