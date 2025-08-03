package com.example.studentinformationmanagement.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.uiState.CurrentUser
import com.example.studentinformationmanagement.data.uiState.LoginHistory
import com.example.studentinformationmanagement.data.uiState.LoginUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class LoginViewModel: ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    // Save Credentials
    private fun saveCredentials(email: String, password: String, context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPrefs = EncryptedSharedPreferences.create(
            context, "EduTrack_UserCredentials", masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        sharedPrefs.edit {
            putString("email", email)
            putString("password", password)
        }
    }
    // Load Credentials
    fun loadSavedCredentials(context: Context): Pair<String, String> {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPrefs = EncryptedSharedPreferences.create(
                context, "EduTrack_UserCredentials", masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            val identifier = sharedPrefs.getString("email", "") ?: ""
            val password = sharedPrefs.getString("password", "") ?: ""
            Pair(identifier, password)
        } catch (e: Exception) {
            Pair("", "")
        }
    }

    // Login button event
    var emailErrorMessage by mutableStateOf("")
        private set
    var passwordErrorMessage by mutableStateOf("")
        private set

    fun clearErrorMessage() {
        emailErrorMessage = ""
        passwordErrorMessage = ""
    }

    fun onLoginButtonClicked(
        email: String,
        password: String,
        rememberPassword: Boolean,
        context: Context,
        navController: NavHostController
    ) {
        val auth = FirebaseAuth.getInstance()
        val usersRef = Firebase.firestore.collection("users")

        val isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        emailErrorMessage = if (email.isEmpty()) {
            "Please enter your email"
        } else if (!isEmail) {
            "Wrong email format"
        } else {
            ""
        }
        passwordErrorMessage = if (password.isEmpty()) "Please enter your password" else ""
        if (email.isEmpty() || password.isEmpty() || !isEmail) return

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                usersRef.document(email)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val currentUser = CurrentUser(
                                imageUrl = document.getString("imageUrl") ?: "",
                                name = document.getString("name") ?: "",
                                birthday = document.getString("birthday") ?: "",
                                email = document.id,
                                phone = document.getString("phone") ?: "",
                                role = document.getString("role") ?: "",
                                status = document.getString("status") ?: ""
                            )

                            if (currentUser.status == "Active") {
                                when (currentUser.role) {
                                    "Admin" -> navController.navigate(AppScreen.AdminScreen.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                                    "Manager" -> navController.navigate(AppScreen.ManagerScreen.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                                    "Employee" -> navController.navigate(AppScreen.EmployeeScreen.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                                }
                                _loginUiState.update { it.copy(currentUser = currentUser) }
                                saveLoginHistoryToFirestore(currentUser)
                                if (rememberPassword) saveCredentials(email, password, context) else saveCredentials("", "", context)
                            } else {
                                Toast.makeText(context, "Your account is inactive. Please contact admin", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "This account does not exists", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Cannot find account information", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Wrong email or password", Toast.LENGTH_SHORT).show()
            }
    }

    // Reset Password
    fun sendResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        emailErrorMessage = if (email.isEmpty()) {
            "Please enter your email"
        } else if (!isEmail) {
            "Wrong email format"
        } else {
            ""
        }
        if (email.isEmpty() || !isEmail) return

        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        val cleanEmail = email.trim().lowercase()

        db.collection("users").document(cleanEmail).get()
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    auth.sendPasswordResetEmail(cleanEmail)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onFailure("Failed to send reset email: ${e.message}")
                        }
                } else {
                    onFailure("User not found in database.")
                }
            }
            .addOnFailureListener { e ->
                onFailure("Failed to check user in Firestore: ${e.message}")
            }
    }


    // Update login information
    fun updateCurrentUserInformation(
        newName: String,
        newEmail: String,
        newPhone: String,
        newBirthday: String,
        newStatus: String,
        newRole: String
    ) {
        _loginUiState.update { currentState ->
            val currentImage = currentState.currentUser?.imageUrl

            currentState.copy(
                currentUser = CurrentUser(
                    imageUrl = currentImage.toString(),
                    name = newName,
                    email = newEmail,
                    phone = newPhone,
                    birthday = newBirthday,
                    status = newStatus,
                    role =  newRole
                )
            )
        }
    }

    fun updateCurrentUserImage(newImageUrl: String) {
        _loginUiState.update { currentState ->
            currentState.copy(
                currentUser = currentState.currentUser?.copy(
                    imageUrl = newImageUrl
                )
            )
        }
    }

    // Log out
    fun onLogOutButtonClicked() {
        viewModelScope.launch {
            _loginUiState.update { currentState ->
                currentState.copy(
                    currentUser = null,
                )
            }
        }
    }

    // Login history
    private fun saveLoginHistoryToFirestore(currentUser: CurrentUser) {
        val db = Firebase.firestore
        val loginHistory = hashMapOf(
            "imageUrl" to currentUser.imageUrl,
            "email" to currentUser.email,
            "name" to currentUser.name,
            "loginTime" to System.currentTimeMillis(),
            "role" to currentUser.role
        )

        db.collection("loginHistory")
            .add(loginHistory)
            .addOnSuccessListener {
                Log.d("LoginViewModel", "Login history saved successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("LoginViewModel", "Error saving login history: ${e.message}")
            }
    }

    fun fetchLoginHistory() {
        val db = FirebaseFirestore.getInstance()

        db.collection("loginHistory")
            .orderBy("loginTime", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val loginHistoryList = mutableListOf<LoginHistory>()

                for (document in result) {
                    val loginHistory = document.toObject(LoginHistory::class.java)
                    loginHistoryList.add(loginHistory)
                }

                _loginUiState.update { currentState ->
                    currentState.copy(
                        loginHistoryList = loginHistoryList
                    )
                }
            }
    }

    fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

    // Change user's image event
    fun updateProfileImage(imageUri: Uri, context: Context, onSuccess: (String) -> Unit) {
        val fileName = "userImages/${UUID.randomUUID()}.jpg"

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(fileName)

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val db = Firebase.firestore
                    db.collection("users")
                        .document(loginUiState.value.currentUser?.phone ?: "")
                        .update("imageUrl", downloadUri.toString())
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