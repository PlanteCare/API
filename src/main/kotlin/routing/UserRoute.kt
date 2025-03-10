package com.api.routing

import com.api.exception.ErrorResponse
import com.api.models.DTOs.UserDTO
import com.api.routing.requests.RegisterRequest
import com.api.routing.requests.SearchByEmail
import com.api.routing.requests.SearchByUsername
import com.api.services.UserService
import com.api.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(
    userService: UserService
) {
    val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    get {
        try {
            val users = userService.getAllUsers()
            call.respond(users)
        } catch (e: Exception) {
            logger.error("Error fetching users", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "An internal server error occurred while fetching users", code = 500)
            )
        }
    }

    get("/{id}") {
        try {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Missing or invalid ID format")
            val user = userService.getUserById(id)
            if (user != null) {
                call.respond(user)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(message = "User not found with ID: $id", code = 404)
                )
            }
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid request", e)
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(message = e.message ?: "Invalid request", code = 400)
            )
        } catch (e: Exception) {
            logger.error("Error fetching user by ID", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "An internal server error occurred", code = 500)
            )
        }
    }

        post("/searchByEmail") {
        try {
            val searchRequest = call.receive<SearchByEmail>()
            val user = userService.getUserByEmail(searchRequest.email)
            if (user != null) {
                call.respond(user)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(message = "User not found with email: ${searchRequest.email}", code = 404)
                )
            }
        } catch (e: ContentTransformationException) {
            logger.error("Invalid request format", e)
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(message = "Invalid request format: ${e.message}", code = 400)
            )
        } catch (e: Exception) {
            logger.error("Error searching user by email", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "An internal server error occurred", code = 500)
            )
        }
    }

    post("/searchByUsername") {
        try {
            val searchRequest = call.receive<SearchByUsername>()
            val user = userService.getUserByUsername(searchRequest.username)
            if (user != null) {
                call.respond(user)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(message = "User not found with username: ${searchRequest.username}", code = 404)
                )
            }
        } catch (e: ContentTransformationException) {
            logger.error("Invalid request format", e)
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(message = "Invalid request format: ${e.message}", code = 400)
            )
        } catch (e: Exception) {
            logger.error("Error searching user by username", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "An internal server error occurred", code = 500)
            )
        }
    }

    post("/register") {
        try {
            val registerRequest = call.receive<RegisterRequest>()

            // Validate request data
            if (registerRequest.username.isBlank() || registerRequest.email.isBlank() || registerRequest.password.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(message = "Username, email, and password are required", code = 400)
                )
                return@post
            }

            // Validate email format
            if (!isValidEmail(registerRequest.email)) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(message = "Invalid email format", code = 400)
                )
                return@post
            }

            // Password strength validation
            if (registerRequest.password.length < 8) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(message = "Password must be at least 8 characters long", code = 400)
                )
                return@post
            }

            val result = userService.registerUser(registerRequest)

            if (result.user != null) {
                call.respond(HttpStatusCode.Created, result)
            } else {
                call.respond(HttpStatusCode.Conflict, result)
            }
        } catch (e: ContentTransformationException) {
            logger.error("Invalid request format", e)
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(message = "Invalid request format: ${e.message}", code = 400)
            )
        } catch (e: Exception) {
            logger.error("Error registering user", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "An internal server error occurred", code = 500)
            )
        }
    }
}