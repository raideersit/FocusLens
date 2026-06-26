package com.focuslens.server.service

import com.focuslens.server.dto.CreateScanRequest
import com.focuslens.server.dto.ScanResponse
import com.focuslens.server.repository.FoodScanRepository
import java.util.UUID

/**
 * SERVICIO del historial de escaneos.
 */
class FoodScanService(
    private val scanRepository: FoodScanRepository
) {

    suspend fun getHistory(userId: String): List<ScanResponse> =
        scanRepository.findAllByUser(userId.toUUID())

    suspend fun addScan(userId: String, request: CreateScanRequest): ScanResponse {
        if (request.barcode.isBlank()) throw ValidationException("El código de barras es obligatorio")
        if (request.foodName.isBlank()) throw ValidationException("El nombre del alimento es obligatorio")
        return scanRepository.create(userId.toUUID(), request, System.currentTimeMillis())
    }

    suspend fun deleteScan(userId: String, scanId: Long) {
        val deleted = scanRepository.deleteByIdForUser(scanId, userId.toUUID())
        if (!deleted) throw ScanNotFoundException()
    }

    suspend fun updateNotes(userId: String, scanId: Long, notes: String) {
        val updated = scanRepository.updateNotes(scanId, userId.toUUID(), notes)
        if (!updated) throw ScanNotFoundException()
    }

    private fun String.toUUID(): UUID =
        runCatching { UUID.fromString(this) }.getOrElse { throw UserNotFoundException() }
}
