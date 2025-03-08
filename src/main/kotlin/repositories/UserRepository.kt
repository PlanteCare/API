package com.api.repositories

import com.api.database.dbQuery
import com.api.database.tables.Users
import com.api.models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.slf4j.LoggerFactory

class UserRepository {
    val logger = LoggerFactory.getLogger("UserRepository")

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        email = row[Users.email],
        status = row[Users.status]
    )

    suspend fun getAllUsers(): List<User> {
        return dbQuery {
            Users.selectAll().map(::resultRowToUser)
        }
    }

    suspend fun findByEmail(email: String): User? {
        return dbQuery {
            Users.select(Users.email.eq(email))
                .mapNotNull { resultRowToUser(it) }
                .singleOrNull()
        }
    }
}