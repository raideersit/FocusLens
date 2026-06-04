package com.focuslens.domain.repository

import com.focuslens.domain.model.FoodScan
import kotlinx.coroutines.flow.Flow

/**
 * PRINCIPIO I (ISP): Separado de FoodRepository — el historial tiene
 * su propio ciclo de vida y responsabilidades (CRUD local).
 * PRINCIPIO S (SRP): Solo gestiona el historial de escaneos.
 */
interface ScanHistoryRepository {

    /** Flujo reactivo de todos los escaneos ordenados por fecha descendente. */
    fun getAllScans(): Flow<List<FoodScan>>

    /** Obtiene un escaneo específico por ID. */
    fun getScanById(scanId: Long): Flow<FoodScan?>

    /** Guarda un nuevo escaneo y devuelve su ID generado. */
    suspend fun addScan(scan: FoodScan): Long

    /** Elimina un escaneo por su ID. */
    suspend fun deleteScan(scanId: Long)

    /** Actualiza las notas de un escaneo existente. */
    suspend fun updateScanNotes(scanId: Long, notes: String)
}
