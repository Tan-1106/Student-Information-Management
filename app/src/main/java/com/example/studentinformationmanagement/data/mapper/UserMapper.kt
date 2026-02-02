package com.example.studentinformationmanagement.data.mapper

import com.example.studentinformationmanagement.data.dto.UserDTO
import com.example.studentinformationmanagement.domain.model.User

fun UserDTO.toEntity(): User {
    return User(
        birthday = birthday ?: "",
        email = email ?: "",
        imageUrl = imageUrl ?: "",
        name = name ?: "",
        phone = phone ?: "",
        role = role ?: "",
        status = status ?: "",
    )
}

fun User.toDTO(): UserDTO {
    return UserDTO(
        birthday = birthday,
        email = email,
        imageUrl = imageUrl,
        name = name,
        phone = phone,
        role = role,
        status = status,
    )
}