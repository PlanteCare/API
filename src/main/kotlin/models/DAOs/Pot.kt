package com.api.models.DAOs

import com.api.models.DAOs.Role.default
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.LocalDateTime

object Pot : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(User.id)
    val macAddress = varchar("mac_address", 255)
    val name = varchar("name", 50)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}