package com.api.services

import com.api.models.DTOs.UserDTO
import com.api.repositories.UserRepository
import com.api.routing.requests.RegisterRequest
import com.api.routing.responses.RegisterResponse
import org.mindrot.jbcrypt.BCrypt
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

    suspend fun registerUser(request: RegisterRequest): RegisterResponse {
        logger.info("Registering new user with username: ${request.username}")

        // Check if user with email already exists
        val existingUserEmail = userRepository.findByEmail(request.email)
        if (existingUserEmail != null) {
            logger.warn("Registration failed: Email ${request.email} already exists")
            return RegisterResponse(
                message = "Email already exists"
            )
        }

        // Check if user with username already exists
        val existingUserUsername = userRepository.findByUsername(request.username)
        if (existingUserUsername != null) {
            logger.warn("Registration failed: Username ${request.username} already exists")
            return RegisterResponse(
                message = "Username already exists"
            )
        }

        // Hash the password
        val salt = BCrypt.gensalt()
        val hashedPassword = BCrypt.hashpw(request.password, salt)

        try {
            // Register the user
            userRepository.registerUser(request.username, request.email, hashedPassword)

            // Get the newly created user to return in the response
            val newUser = userRepository.findByUsername(request.username)

            return if (newUser != null) {
                logger.info("User registered successfully: ${newUser.username}")
                RegisterResponse(
                    message = "User registered successfully",
                    user = newUser
                )
            } else {
                logger.error("Failed to retrieve user after registration")
                RegisterResponse(
                    message = "User registered successfully, but could not retrieve user details"
                )
            }
        } catch (e: Exception) {
            logger.error("Failed to register user", e)
            return RegisterResponse(
                message = "Failed to register user: ${e.message}"
            )
        }
    }
}