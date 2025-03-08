package com.api.routing.requests

import kotlinx.serialization.Serializable

@Serializable
data class SearchByUsername(val username: String)
