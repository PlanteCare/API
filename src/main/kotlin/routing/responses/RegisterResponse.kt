package com.api.routing.responses

import com.api.models.DTOs.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val message: String,
    val user: UserDTO? = null
)
