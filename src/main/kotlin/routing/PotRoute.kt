package com.api.routing

import com.api.exception.ErrorResponse
import com.api.routing.requests.CreatPotRequest
import com.api.services.PotService
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*


fun Route.potRoute(
    potService: PotService
) {
    val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    authenticate {
        get("/admin") {
            try {
                val principal = call.principal<JWTPrincipal>()
                val roleId = principal?.payload?.getClaim("roleId")?.asInt()
                    ?: throw IllegalArgumentException("Role not found in token")

                if (roleId != 2) {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        ErrorResponse(message = "Access denied: Admin role required", code = 403)
                    )
                    return@get
                }

                val pots = potService.getAllPot()
                call.respond(pots)
            } catch (e: IllegalArgumentException) {
                logger.error("Invalid request", e)
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(message = e.message ?: "Invalid request", code = 400)
                )
            } catch (e: Exception) {
                logger.error("Error fetching pots", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = "An internal server error occurred while fetching pots", code = 500)
                )
            }
        }
    }

    authenticate {
        get("/me") {
            try {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt()
                    ?: throw IllegalArgumentException("User ID not found in token")

                val pots = potService.getPotByUserId(userId)
                if (pots.isNotEmpty()) {
                    call.respond(pots)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(message = "No pots found for your account", code = 404)
                    )
                }
            } catch (e: IllegalArgumentException) {
                logger.error("Invalid request", e)
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(message = e.message ?: "Invalid request", code = 400)
                )
            } catch (e: Exception) {
                logger.error("Error fetching user pots", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(message = "An internal server error occurred", code = 500)
                )
            }
        }
    }

    get("/{id}") {
        try {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Missing or invalid ID format")
            val pot = potService.getPotById(id)
            if (pot != null) {
                call.respond(pot)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(message = "Pot not found with ID: $id", code = 404)
                )
            }
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid request", e)
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(message = e.message ?: "Invalid request", code = 400)
            )
        } catch (e: Exception) {
            logger.error("Error fetching pot by ID", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "An internal server error occurred", code = 500)
            )
        }
    }

    post {
        try {
            val request = call.receive<CreatPotRequest>()
            val pot = potService.createPot(request)
            call.respond(HttpStatusCode.Created, pot)
        } catch (e: Exception) {
            logger.error("Error creating pot", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "An internal server error occurred while creating the pot", code = 500)
            )
        }
    }
}