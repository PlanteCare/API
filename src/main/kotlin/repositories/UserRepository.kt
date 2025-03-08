package com.api.repositories

import com.api.database.dbQuery
import com.api.models.DAOs.User
import com.api.models.DTOs.UserDTO
import com.api.models.DTOs.UserRegisterDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.format.DateTimeFormatter


class UserRepository {

    private fun resultRowToUser(row: ResultRow) = UserDTO(
        id = row[User.id],
        username = row[User.username],
        email = row[User.email],
        status = row[User.status],
        createdAt = row[User.createdAt].format(DateTimeFormatter.ISO_DATE_TIME),
        updatedAt = row[User.updatedAt].format(DateTimeFormatter.ISO_DATE_TIME)
    )

    suspend fun getAllUser(): List<UserDTO> {
        return dbQuery {
            User.selectAll().map(::resultRowToUser)
        }
    }


    suspend fun findByEmail(email: String): UserDTO? {
        return dbQuery {
            User.selectAll().where { User.email eq email }
                .mapNotNull { resultRowToUser(it) }
                .singleOrNull()
        }
    }

    suspend fun findById(id: Int): UserDTO? {
        return dbQuery {
            User.selectAll().where { User.id eq id }
                .mapNotNull { resultRowToUser(it) }
                .singleOrNull()
        }
    }

    suspend fun findByUsername(username: String): UserDTO? {
        return dbQuery {
            User.selectAll().where { User.username eq username }
                .mapNotNull { resultRowToUser(it) }
                .singleOrNull()
        }
    }

    suspend fun registerUser(hashPassword: String, us)

    suspend fun updateUser(id: Int, user: UserDTO) {
        return dbQuery {
            User.update({ User.id eq id }) {
                it[username] = user.username
                it[email] = user.email
                it[status] = user.status
            } > 0
        }
    }

    suspend fun deleteUser(id: Int) {
        return dbQuery {
            User.deleteWhere { User.id eq id } > 0
        }
    }
}