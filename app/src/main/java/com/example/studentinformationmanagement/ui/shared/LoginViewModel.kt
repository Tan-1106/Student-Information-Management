package com.example.studentinformationmanagement.ui.shared

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.studentinformationmanagement.AppScreen
import com.example.studentinformationmanagement.data.shared.CurrentUser
import com.example.studentinformationmanagement.data.shared.LoginHistory
import com.example.studentinformationmanagement.data.shared.LoginRepository
import com.example.studentinformationmanagement.data.shared.LoginUiState
import com.google.firebase.Firebase
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

class LoginViewModel(
    private val loginRepository: LoginRepository = LoginRepository()
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    // TODO: CLEAR THE USERNAME & PASSWORD AFTER DONE TESTING
    // User's phone number input
    var userPhoneNumberInput by mutableStateOf("3333333333")
        private set
    fun onPhoneNumberChange(userInput: String) {
        userPhoneNumberInput = userInput
    }

    // User's password input
    var userPasswordInput by mutableStateOf("Employee")
        private set
    fun onPasswordChange(userInput: String) {
        userPasswordInput = userInput
    }

    // Showing or hiding the password text field
    var isPasswordShowing by mutableStateOf(false)
        private set
    fun onPasswordVisibilityChange() {
        isPasswordShowing = !isPasswordShowing
    }

    // Login button event
    fun onLoginButtonClicked(
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            _loginUiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            val result = loginRepository.login(userPhoneNumberInput, userPasswordInput)

            if (result.isSuccess) {
                val currentUser = result.getOrNull()
                _loginUiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        currentUser = currentUser,
                        errorMessage = null
                    )
                }

                currentUser?.let {
                    saveLoginHistoryToFirestore(it)
                }

                when (currentUser?.userRole) {
                    "Admin" -> navController.navigate(AppScreen.AdminScreen.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                    "Manager" -> navController.navigate(AppScreen.ManagerScreen.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                    "Employee" -> navController.navigate(AppScreen.EmployeeScreen.name) { popUpTo(AppScreen.Login.name) { inclusive = true } }
                }
            } else {
                _loginUiState.value = _loginUiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed."
                )
                Toast.makeText(context, "Wrong phone number or password.", Toast.LENGTH_SHORT).show()
            }
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
            val currentImage = currentState.currentUser?.userImageUrl

            currentState.copy(
                currentUser = CurrentUser(
                    userImageUrl = currentImage.toString(),
                    userName = newName,
                    userEmail = newEmail,
                    userPhoneNumber = newPhone,
                    userBirthday = newBirthday,
                    userStatus = newStatus,
                    userRole =  newRole
                )
            )
        }
    }

    fun updateCurrentUserImage(newImageUrl: String) {
        _loginUiState.update { currentState ->
            currentState.copy(
                currentUser = currentState.currentUser?.copy(
                    userImageUrl = newImageUrl
                )
            )
        }
    }

    // Log out
    fun onLogOutButtonClicked() {
        userPhoneNumberInput = ""
        userPasswordInput = ""

        viewModelScope.launch {
            _loginUiState.update { currentState ->
                currentState.copy(
                    currentUser = null,
                    errorMessage = null
                )
            }
        }
    }

    // Login history
    private fun saveLoginHistoryToFirestore(currentUser: CurrentUser) {
        val db = Firebase.firestore
        val loginHistory = hashMapOf(
            "userImageUrl" to currentUser.userImageUrl,
            "userPhoneNumber" to currentUser.userPhoneNumber,
            "userName" to currentUser.userName,
            "loginTime" to System.currentTimeMillis(),
            "userRole" to currentUser.userRole
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
        _loginUiState.update { currentState ->
            currentState.copy(isLoading = true)
        }

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
                        isLoading = false,
                        loginHistoryList = loginHistoryList
                    )
                }
            }
            .addOnFailureListener { exception ->
                _loginUiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Failed to fetch login history: ${exception.message}"
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
                        .document(loginUiState.value.currentUser?.userPhoneNumber ?: "")
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