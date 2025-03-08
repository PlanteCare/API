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
    val logger = LoggerFactory.getLogger(this::class.java)
    routing {
        get("/") {
            call.respondText { "Plante Care API" }
        }

        route("/api/user") {
            userRoute(userService)
        }
    }
}
