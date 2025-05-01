package com.example.studentinformationmanagement.ui.admin

import android.content.Context
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID


class AdminViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    var emailList by mutableStateOf<List<String>>(emptyList())
        private set
    var phoneList by mutableStateOf<List<String>>(emptyList())
        private set

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

                // For checking duplicate
                emailList = users.map { it.userEmail }.distinct()
                phoneList = users.map { it.userPhoneNumber }.distinct()
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
    fun clearErrorMessage() {
        nameError = ""
        emailError = ""
        phoneError = ""
        birthdayError = ""
        statusError = ""
        roleError = ""
    }

    fun onAddUserButtonClick(
        navController: NavHostController,
        context: Context
    ) {
        val name = newUserName.trim()
        val email = newUserEmail.trim()
        val phone = newUserPhone.trim()
        val birthday = newUserBirthday.trim()
        val role = newUserRole.trim()
        val status = newUserStatus.trim()

        if (validateUserInputs(newName = name, newEmail = email, newPhone = phone, newBirthday = birthday, newRole = role, newStatus = status)) {
            val db = Firebase.firestore

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
                                            clearErrorMessage()
                                            clearAddUserInputs()
                                            navController.navigateUp()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Cannot add user", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Cannot check existing email", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Cannot check existing phone number", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun validateUserInputs(
        newName: String,
        currentEmail: String = "",
        newEmail: String,
        currentPhone: String = "",
        newPhone: String,
        newBirthday: String,
        newStatus: String,
        newRole: String,
        existingEmails: List<String> = emailList,
        existingPhones: List<String> = phoneList,
    ): Boolean {
        var isValid = true

        if (newName.isEmpty()) {
            nameError = "Name is required"
            isValid = false
        } else {
            nameError = ""
        }

        if (newEmail.isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail.trim()).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else if (currentEmail != "" && currentEmail == newEmail) {
            phoneError = ""
            isValid = true
        } else if (existingEmails.contains(newEmail)) {
            emailError = "Email already exists"
            isValid = false
        } else {
            emailError = ""
        }

        if (newPhone.isEmpty()) {
            phoneError = "Phone number is required"
            isValid = false
        } else if (newPhone.trim().length != 10) {
            phoneError = "Invalid phone number"
            isValid = false
        } else if (currentPhone != "" && currentPhone == newPhone) {
            phoneError = ""
            isValid = true
        } else if (existingPhones.contains(newPhone)) {
            phoneError = "Phone number already exists"
            isValid = false
        } else {
            phoneError = ""
        }

        if (newBirthday.isEmpty()) {
            birthdayError = "Birthday is required"
            isValid = false
        } else {
            birthdayError = ""
        }

        if (newStatus.isEmpty()) {
            statusError = "Status is required"
            isValid = false
        } else {
            statusError = ""
        }

        if (newRole.isEmpty()) {
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

    // Edit a user
    var userToEdit by mutableStateOf<User?>(null)
        private set
    fun onEditUserSwipe(
        userPhoneNumber: String,
        navController: NavHostController
    ) {
        userToEdit = fullUserList.find { it.userPhoneNumber == userPhoneNumber }
        navController.navigate(AppScreen.EditUser.name)
    }

    fun onEditUserSaveClick(
        newName: String,
        newEmail: String,
        newPhone: String,
        newBirthday: String,
        newStatus: String,
        newRole: String,
        context: Context,
        navController: NavHostController
    ) {
        val userToEdit = userToEdit ?: return

        val db = Firebase.firestore
        val updatedData = mutableMapOf<String, Any>()

        if (newName.isNotEmpty() && newName != userToEdit.userName) {
            updatedData["userName"] = newName
        }

        if (newEmail.isNotEmpty() && newEmail != userToEdit.userEmail) {
            updatedData["userEmail"] = newEmail
        }

        if (newPhone.isNotEmpty() && newPhone != userToEdit.userPhoneNumber) {
            updatedData["userPhoneNumber"] = newPhone
        }

        if (newBirthday.isNotEmpty() && newBirthday != userToEdit.userBirthday) {
            updatedData["userBirthday"] = newBirthday
        }

        if (newStatus.isNotEmpty() && newStatus != userToEdit.userStatus) {
            updatedData["userStatus"] = newStatus
        }

        if (newRole.isNotEmpty() && newRole != userToEdit.userRole) {
            updatedData["userRole"] = newRole
        }

        if (updatedData.isNotEmpty()) {
            db.collection("users")
                .document(userToEdit.userPhoneNumber)
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(context, "User details updated successfully", Toast.LENGTH_SHORT).show()
                    fetchUsersFromFirestore()
                    navController.navigateUp()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error updating user: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "No changes detected", Toast.LENGTH_SHORT).show()
        }
    }

    // Change user's image event
    fun updateUserImage(imageUri: Uri, context: Context, onSuccess: (String) -> Unit) {
        val fileName = "userImages/${UUID.randomUUID()}.jpg"

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(fileName)

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val db = Firebase.firestore
                    db.collection("users")
                        .document(userToEdit?.userPhoneNumber ?: "")
                        .update("userImageUrl", downloadUri.toString())
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile image updated ", Toast.LENGTH_SHORT).show()
                            onSuccess(downloadUri.toString())
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error updating profile image: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}