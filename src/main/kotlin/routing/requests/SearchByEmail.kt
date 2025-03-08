package com.api.routing.requests

import kotlinx.serialization.Serializable

@Serializable
data class SearchByEmail(val email: String)
