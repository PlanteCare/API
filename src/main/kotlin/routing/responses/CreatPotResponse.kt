package com.api.routing.responses

import kotlinx.serialization.Serializable
import com.api.models.DTOs.PotDTO

@Serializable
data class CreatPotResponse(
    val message: String,
    val pot: PotDTO? = null
)
