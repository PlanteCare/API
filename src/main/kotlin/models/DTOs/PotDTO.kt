package com.api.models.DTOs

import kotlinx.serialization.Serializable

@Serializable
class PotDTO (
    val id: Int? = null,
    val userId: Int? = null,
    val macAddress: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)

