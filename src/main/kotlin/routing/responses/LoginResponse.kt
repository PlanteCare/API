package com.api.routing.responses

import com.api.models.DTOs.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val token: String? = null,
    val user: UserDTO? = null
)
