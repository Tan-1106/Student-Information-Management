package com.example.studentinformationmanagement.ui.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentinformationmanagement.data.admin.AdminRepository
import com.example.studentinformationmanagement.data.admin.AdminUiState
import com.example.studentinformationmanagement.data.shared.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val repository = AdminRepository()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> =_uiState.asStateFlow()

    init {
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        viewModelScope.launch {
            val result = repository.getAllUsers()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(userList = result.getOrNull() ?: emptyList())
            } else {
                Log.e("AdminViewModel", "Error while fetching user list.", result.exceptionOrNull())
            }
        }
    }

    fun onUserEditClicked(userPhoneNumber: String) {

    }

    fun onUserSeeMoreClicked(userPhoneNumber: String) {

    }
}