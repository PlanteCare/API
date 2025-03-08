package com.api.models.DTOs

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserDTO(
    val id: Int? = null,
    val username: String,
    val email: String,
    val status: Boolean,
    val createdAt: String,
    val updatedAt: String
)







