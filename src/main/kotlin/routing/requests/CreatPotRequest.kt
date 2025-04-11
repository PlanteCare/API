package com.api.routing.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreatPotRequest(
    val userId: Int? = null,
    val macAddress: String,
    val name: String
)
