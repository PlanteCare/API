package com.api

import com.api.database.configureDatabases
import com.api.plugins.configureHTTP
import com.api.plugins.configureSecurity
import com.api.plugins.configureSerialization
import com.api.repositories.UserRepository
import com.api.routing.configureRouting
import com.api.services.JwtService
import com.api.services.UserService
import com.api.repositories.PotRepository
import com.api.services.PotService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepository()
    val potRepository = PotRepository()
    val userService = UserService(userRepository)
    val potService = PotService(potRepository)
    val jwtService = JwtService(this, userService)
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity(jwtService)
    configureRouting(userService, jwtService, potService)
}
