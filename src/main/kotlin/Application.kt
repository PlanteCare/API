package com.api

import com.api.database.configureDatabases
import com.api.plugins.configureHTTP
import com.api.plugins.configureSecurity
import com.api.plugins.configureSerialization
import com.api.routing.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
