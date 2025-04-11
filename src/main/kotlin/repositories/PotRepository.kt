package com.api.repositories

import com.api.database.dbQuery
import com.api.models.DAOs.Pot
import com.api.models.DTOs.PotDTO
import org.jetbrains.exposed.sql.*
import java.time.format.DateTimeFormatter

class PotRepository {

    private fun resultRowToPot(row: ResultRow) = PotDTO(
        id = row[Pot.id],
        userId = row[Pot.userId],
        macAddress = row[Pot.macAddress],
        name = row[Pot.name],
        createdAt = row[Pot.createdAt].format(DateTimeFormatter.ISO_DATE_TIME),
        updatedAt = row[Pot.updatedAt].format(DateTimeFormatter.ISO_DATE_TIME)
    )

    suspend fun getAllPot(): List<PotDTO> {
        return dbQuery {
            Pot.selectAll().map(::resultRowToPot)
        }
    }

    suspend fun findByUserId(userId: Int): List<PotDTO> {
        return dbQuery {
            Pot.selectAll().where { Pot.userId eq userId }
                .map(::resultRowToPot)
        }
    }

    suspend fun findByMacAddress(macAddress: String): PotDTO? {
        return dbQuery {
            Pot.selectAll().where { Pot.macAddress eq macAddress }
                .mapNotNull { resultRowToPot(it) }
                .singleOrNull()
        }
    }

    suspend fun findById(id: Int): PotDTO? {
        return dbQuery {
            Pot.selectAll().where { Pot.id eq id }
                .mapNotNull { resultRowToPot(it) }
                .singleOrNull()
        }
    }

    suspend fun createPot(userId: Int, macAddress: String, name: String) {
        return dbQuery {
            Pot.insert {
                it[Pot.userId] = userId
                it[Pot.macAddress] = macAddress
                it[Pot.name] = name
            }
        }
    }
}