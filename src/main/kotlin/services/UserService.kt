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

    suspend fun getUserByEmail(email: String): UserDTO? {
        logger.info("Retrieving user with email")
        return userRepository.findByEmail(email = email)
    }

    suspend fun getUserById(id: Int): UserDTO? {
        logger.info("Retrieving user with id")
        return userRepository.findById(id = id)
    }

    suspend fun getUserByUsername(username: String): UserDTO? {
        logger.info("Retrieving user with username")
        return userRepository.findByUsername(username = username)
    }
}