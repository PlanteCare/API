package com.api.database

import com.api.database.tables.Users
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.configureDatabases() {
    val logger = LoggerFactory.getLogger("Database")

    try {

        logger.info("Initialisation de la connexion à la base de données...")
        val database = Database.connect(
            url = environment.config.property("database.url").getString(),
            user = environment.config.property("database.user").getString(),
            driver = environment.config.property("database.driver").getString(),
            password = environment.config.property("database.password").getString(),
        )
        logger.info("Connexion à la base de données établie avec succès")

        transaction(database) {
            logger.info("Début de la création/vérification des tables...")
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Users)
            logger.info("Tables créées/vérifiées avec succès")
        }

        println("✅ Base de données initialisée avec succès")
    }catch (e: Exception) {
        logger.error("Erreur lors de l'initialisation de la base de données: ${e.message}")
        println("❌ Erreur de connexion à la base de données: ${e.message}")
        e.printStackTrace()
    }
}
