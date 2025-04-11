package com.api.services

import com.api.repositories.PotRepository
import com.api.models.DTOs.PotDTO
import com.api.routing.requests.CreatPotRequest
import org.slf4j.LoggerFactory

class PotService (
    private val potRepository: PotRepository,
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun getAllPot(): List<PotDTO> {
        logger.info("Getting all pots")
        return potRepository.getAllPot()
    }

    suspend fun getPotByUserId(userId: Int): List<PotDTO> {
        logger.info("Retrieving pots with userId")
        return potRepository.findByUserId(userId = userId)
    }

    suspend fun getPotByMacAddress(macAddress: String): PotDTO? {
        logger.info("Retrieving pot with macAddress")
        return potRepository.findByMacAddress(macAddress = macAddress)
    }

    suspend fun getPotById(id: Int): PotDTO? {
        logger.info("Retrieving pot with id")
        return potRepository.findById(id = id)
    }

    suspend fun createPot(request: CreatPotRequest) {
        logger.info("Creating new pot with macAddress: ${request.macAddress}")
        val existingPot = potRepository.findByMacAddress(request.macAddress)
        if (existingPot != null) {
            logger.warn("Creation failed: Pot with macAddress ${request.macAddress} already exists")
            return
        }
        try {
            potRepository.createPot(
                userId = request.userId ?: 0,
                macAddress = request.macAddress,
                name = request.name
            )
            logger.info("Pot created successfully")
        } catch (e: Exception) {
            logger.error("Error creating pot: ${e.message}")
        }
    }
}