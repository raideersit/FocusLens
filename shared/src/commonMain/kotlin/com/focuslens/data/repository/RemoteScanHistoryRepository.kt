package com.focuslens.data.repository

import com.focuslens.data.remote.api.FocusLensApi
import com.focuslens.data.remote.dto.toCreateRequest
import com.focuslens.data.remote.dto.toDomain
import com.focuslens.domain.model.FoodScan
import com.focuslens.domain.repository.ScanHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Implementación del historial contra el backend FocusLens (Ktor + Neon).
 *
 * Mantiene un [MutableStateFlow] como caché reactiva: la UI sigue observando un
 * Flow (igual que con SQLDelight), y tras cada mutación se refresca desde la API.
 */
class RemoteScanHistoryRepository(
    private val api: FocusLensApi
) : ScanHistoryRepository {

    private val scans = MutableStateFlow<List<FoodScan>>(emptyList())

    override fun getAllScans(): Flow<List<FoodScan>> = flow {
        refresh()
        emitAll(scans)
    }

    override fun getScanById(scanId: Long): Flow<FoodScan?> =
        getAllScans().map { list -> list.firstOrNull { it.id == scanId } }

    override suspend fun addScan(scan: FoodScan): Long {
        val created = api.createScan(scan.toCreateRequest())
        refresh()
        return created.id
    }

    override suspend fun deleteScan(scanId: Long) {
        api.deleteScan(scanId)
        refresh()
    }

    override suspend fun updateScanNotes(scanId: Long, notes: String) {
        api.updateNotes(scanId, notes)
        refresh()
    }

    /** Recarga el historial del usuario desde el backend. */
    private suspend fun refresh() {
        scans.value = api.getScans().map { it.toDomain() }
    }
}
