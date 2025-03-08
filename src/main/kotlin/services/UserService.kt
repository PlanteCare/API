package com.api.services

import com.api.models.DTOs.UserDTO
import com.api.repositories.UserRepository
import org.slf4j.LoggerFactory

class UserService (
    private val userRepository: UserRepository
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun getAllUsers(): List<UserDTO> {
        logger.info("Getting all users")
        return userRepository.getAllUser()
    }
}