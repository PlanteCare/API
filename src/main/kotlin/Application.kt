package com.api

import com.api.database.configureDatabases
import com.api.plugins.configureHTTP
import com.api.plugins.configureSecurity
import com.api.plugins.configureSerialization
import com.api.repositories.UserRepository
import com.api.routing.configureRouting
import com.api.services.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting(userService)
}
