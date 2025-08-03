package com.example.studentinformationmanagement.ui.viewModel

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
import com.example.studentinformationmanagement.data.uiState.AdminUiState
import com.example.studentinformationmanagement.data.uiState.CurrentUser
import com.example.studentinformationmanagement.data.uiState.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.Collator
import java.util.Locale
import java.util.UUID


class AdminViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    // Fetching User List
    private var fullUserList: List<User> = emptyList()
    private var emailList by mutableStateOf<List<String>>(emptyList())
    private var phoneList by mutableStateOf<List<String>>(emptyList())

    init {
        fetchUsersFromFirestore()
    }

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
                fullUserList = users
                _uiState.update { it.copy(userList = users) }

                emailList = users.map { it.email }.distinct()
                phoneList = users.map { it.phone }.distinct()
            }
    }

    // USER
    fun onUserSearch(userSearchInput: String) {
        val keyword = userSearchInput.trim().lowercase()
        if (keyword.isEmpty()) {
            fetchUsersFromFirestore()
        } else {
            val filteredList = fullUserList.filter { user ->
                user.name.contains(keyword, ignoreCase = true) || user.email.contains(
                    keyword,
                    ignoreCase = true
                ) ||
                        user.phone.contains(keyword, ignoreCase = true) || user.status.contains(
                    keyword,
                    ignoreCase = true
                ) ||
                        user.role.contains(keyword, ignoreCase = true)
            }
            _uiState.update { it.copy(userList = filteredList) }
        }
    }

    // Filter feature
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

    fun onApplyFilterClick() {
        var filtered = fullUserList

        if (roleSelected.isNotBlank()) {
            filtered = filtered.filter { it.role == roleSelected }
        }
        if (statusSelected.isNotBlank()) {
            filtered = filtered.filter { it.status == statusSelected }
        }

        fun lastWord(name: String): String {
            return name.trim().split("\\s+".toRegex()).lastOrNull() ?: ""
        }

        val collator = Collator.getInstance(Locale("vi", "VN"))
        filtered = when (sortSelected) {
            "A → Z" -> filtered.sortedWith(compareBy(collator) { lastWord(it.name) })
            "Z → A" -> filtered.sortedWith(compareByDescending(collator) { lastWord(it.name) })
            else -> filtered
        }
        _uiState.update { it.copy(userList = filtered) }
    }

    fun onClearFilterClick() {
        sortSelected = ""
        roleSelected = ""
        statusSelected = ""
        _uiState.update { it.copy(userList = fullUserList) }
    }

    // User's detail profile
    var userToView by mutableStateOf(CurrentUser())
        private set

    fun clearUserToView() {
        userToView = CurrentUser()
    }

    fun onUserSeeMoreClicked(
        userPhoneNumber: String,
        navController: NavHostController
    ) {
        val selectedUser = _uiState.value.userList.find { it.phone == userPhoneNumber }
        userToView = CurrentUser(
            imageUrl = selectedUser?.imageUrl
                ?: "https://drive.google.com/uc?id=1XGMQWOMTV5lxHGLpYQMk--3Zqm7-iEtK",
            name = selectedUser?.name ?: "",
            birthday = selectedUser?.birthday ?: "",
            email = selectedUser?.email ?: "",
            phone = selectedUser?.phone ?: "",
            role = selectedUser?.role ?: "",
            status = selectedUser?.status ?: "",
        )
        navController.navigate(AppScreen.UserDetailProfile.name)
    }

    // Add User
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

    fun clearErrorMessage() {
        nameError = ""
        emailError = ""
        phoneError = ""
        birthdayError = ""
        statusError = ""
        roleError = ""
    }

    fun onAddUserButtonClick(
        nameInput: String,
        emailInput: String,
        phoneInput: String,
        birthdayInput: String,
        roleInput: String,
        statusInput: String,
        onSuccess: () -> Unit,
        navController: NavHostController,
        context: Context
    ) {
        val name = nameInput.trim()
        val email = emailInput.trim().lowercase().replace("\\s".toRegex(), "")
        val phone = phoneInput.trim()
        val birthday = birthdayInput.trim()
        val role = roleInput.trim()
        val status = statusInput.trim()

        if (!validateUserInputs(
                newName = name,
                newEmail = email,
                newPhone = phone,
                newBirthday = birthday,
                newRole = role,
                newStatus = status
            )) {
            Toast.makeText(context, "Invalid input data.", Toast.LENGTH_SHORT).show()
            return
        }

        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        val newUser = User(
            name = name,
            email = email,
            phone = phone,
            birthday = birthday,
            role = role,
            status = status
        )

        db.collection( "users").document(email).get()
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    Toast.makeText(context, "User already exists in database.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val saveToFirestoreAndSendReset = {
                    db.collection("users").document(email).set(newUser)
                        .addOnSuccessListener {
                            auth.sendPasswordResetEmail(email)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "User added. Reset email sent.", Toast.LENGTH_SHORT).show()
                                    onSuccess()
                                    navController.navigateUp()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "User added but failed to send reset email: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }

                auth.createUserWithEmailAndPassword(email, "TemporaryPassword123!")
                    .addOnSuccessListener {
                        saveToFirestoreAndSendReset()
                    }
                    .addOnFailureListener { e ->
                        if (e is FirebaseAuthUserCollisionException) {
                            saveToFirestoreAndSendReset()
                        } else {
                            Toast.makeText(context, "Failed to create auth account: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.e("Auth", "Error creating user", e)
                        }
                    }

            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to check user database: ${e.message}", Toast.LENGTH_SHORT).show()
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
    fun onDeleteUser(userEmail: String, context: Context) {
        val db = Firebase.firestore

        db.collection("users")
            .document(userEmail)
            .delete()
            .addOnSuccessListener {
                Log.d("DeleteUser", "Successfully deleted user with email: $userEmail")
            }
            .addOnFailureListener { e ->
                Log.e("DeleteUser", "Error deleting user: ${e.message}")
            }
        Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show()
    }

    // Edit a user
    var userToEdit by mutableStateOf<User?>(null)
        private set

    fun onEditUser(
        userEmail: String,
        navController: NavHostController
    ) {
        userToEdit = fullUserList.find { it.email == userEmail }
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

        if (newName.isNotEmpty() && newName != userToEdit.name) {
            updatedData["name"] = newName
        }

        if (newEmail.isNotEmpty() && newEmail != userToEdit.email) {
            updatedData["email"] = newEmail
        }

        if (newPhone.isNotEmpty() && newPhone != userToEdit.phone) {
            updatedData["phone"] = newPhone
        }

        if (newBirthday.isNotEmpty() && newBirthday != userToEdit.birthday) {
            updatedData["birthday"] = newBirthday
        }

        if (newStatus.isNotEmpty() && newStatus != userToEdit.status) {
            updatedData["status"] = newStatus
        }

        if (newRole.isNotEmpty() && newRole != userToEdit.role) {
            updatedData["role"] = newRole
        }

        if (updatedData.isNotEmpty()) {
            db.collection("users")
                .document(userToEdit.email)
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(context, "User details updated successfully", Toast.LENGTH_SHORT)
                        .show()
                    fetchUsersFromFirestore()
                    navController.navigateUp()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error updating user: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
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
                        .document(userToEdit?.phone ?: "")
                        .update("imageUrl", downloadUri.toString())
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile image updated ", Toast.LENGTH_SHORT)
                                .show()
                            onSuccess(downloadUri.toString())
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Error updating profile image: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}