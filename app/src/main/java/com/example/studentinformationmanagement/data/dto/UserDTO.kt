package com.example.studentinformationmanagement.data.dto

import com.google.firebase.firestore.PropertyName

data class UserDTO(
    @PropertyName("birthday") val birthday: String? = null,
    @PropertyName("email") val email: String? = null,
    @PropertyName("imageUrl") val imageUrl: String? = null,
    @PropertyName("name") val name: String? = null,
    @PropertyName("phone") val phone: String? = null,
    @PropertyName("role") val role: String? = null,
    @PropertyName("status") val status: String? = null,
)