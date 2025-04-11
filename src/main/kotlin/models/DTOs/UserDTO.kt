package com.api.models.DTOs

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int? = null,
    val brokerId: Int? = null,
    val roleId: Int? = null,
    val username: String,
    val email: String,
    val password: String,
    val status: Boolean,
    val createdAt: String,
    val updatedAt: String
)