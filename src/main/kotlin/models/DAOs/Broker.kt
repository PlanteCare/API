package com.api.models.DAOs

import org.jetbrains.exposed.sql.Table

object Broker : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val host = varchar("host", 255)
    val port = integer("port")
    val status = bool("status").default(true)

    override val primaryKey = PrimaryKey(id)
}