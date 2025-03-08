package com.api.database

import com.api.models.DAOs.User
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import org.jetbrains.exposed.sql.transactions.transaction

// Global variable to store the database instance
lateinit var database: Database

/**
 * Utility function to execute database queries asynchronously
 * This function can be used by all repositories
 */
suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

fun Application.configureDatabases() {
    val logger = LoggerFactory.getLogger(this::class.java)

    try {
        logger.info("Initializing database connection...")
        database = Database.connect(
            url = environment.config.property("database.url").getString(),
            user = environment.config.property("database.user").getString(),
            driver = environment.config.property("database.driver").getString(),
            password = environment.config.property("database.password").getString(),
        )
        logger.info("Database connection established successfully")

        transaction(database) {
            logger.info("Starting table creation/verification...")
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(User)
            logger.info("Tables created/verified successfully")
        }
    } catch (e: Exception) {
        logger.error("Error initializing database: ${e.message}")
        e.printStackTrace()
    }
}