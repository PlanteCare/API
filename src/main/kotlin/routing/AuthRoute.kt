package com.api.routing

import com.api.exception.ErrorResponse
import com.api.routing.requests.LoginRequest
import com.api.routing.responses.LoginResponse
import com.api.services.JwtService
import com.api.services.UserService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.authRoute(
    userService: UserService,
    jwtService: JwtService
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    post {
        try {
            val loginRequest = call.receive<LoginRequest>()

            if (loginRequest.email.isBlank() || loginRequest.password.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(message = "L'email et le mot de passe sont requis", code = 400)
                )
                return@post
            }

            val token = jwtService.createJwtToken(loginRequest)
            if (token != null) {
                call.respond(
                    HttpStatusCode.OK,
                    LoginResponse(
                        message = "Connexion réussie",
                        token = token,
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(message = "Email ou mot de passe incorrect", code = 401)
                )
            }
        } catch (e: ContentTransformationException) {
            logger.error("Format de requête invalide", e)
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(message = "Format de requête invalide: ${e.message}", code = 400)
            )
        } catch (e: Exception) {
            logger.error("Erreur lors de la connexion", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "Une erreur interne est survenue", code = 500)
            )
        }
    }
}