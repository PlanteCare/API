package com.api.routing

import com.api.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Application.configureRouting(
    userService: UserService
) {
    val logger = LoggerFactory.getLogger("UserRoutes")
    routing {
        get("/") {
            call.respondText { "Plante Care API" }
        }

        get("/users") {
            try {
                val users = userService.getAllUsers()
                call.respond(users)
            } catch (e: Exception) {
                logger.error("Error fetching users", e)
                call.respond(
                    HttpStatusCode.InternalServerError
                )
            }
        }
    }
}
