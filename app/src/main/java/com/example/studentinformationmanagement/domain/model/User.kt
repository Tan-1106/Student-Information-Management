package com.example.studentinformationmanagement.domain.model

data class User(
    val birthday: String,
    val email: String,
    val imageUrl: String,
    val name: String,
    val phone: String,
    val role: String,
    val status: String
)