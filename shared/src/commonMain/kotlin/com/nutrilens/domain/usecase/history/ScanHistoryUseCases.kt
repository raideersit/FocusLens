package com.nutrilens.domain.usecase.history

import com.nutrilens.domain.model.FoodScan
import com.nutrilens.domain.repository.ScanHistoryRepository
import kotlinx.coroutines.flow.Flow

/** Agrega un escaneo al historial local. */
class AddScanToHistoryUseCase(
    private val repository: ScanHistoryRepository
) {
    suspend operator fun invoke(scan: FoodScan): Long =
        repository.addScan(scan)
}

/** Obtiene todos los escaneos como Flow reactivo. */
class GetScanHistoryUseCase(
    private val repository: ScanHistoryRepository
) {
    operator fun invoke(): Flow<List<FoodScan>> =
        repository.getAllScans()
}

/** Elimina un escaneo por su ID. */
class DeleteScanUseCase(
    private val repository: ScanHistoryRepository
) {
    suspend operator fun invoke(scanId: Long) =
        repository.deleteScan(scanId)
}

/** Actualiza las notas de un escaneo. */
class UpdateScanNotesUseCase(
    private val repository: ScanHistoryRepository
) {
    suspend operator fun invoke(scanId: Long, notes: String) =
        repository.updateScanNotes(scanId, notes)
}
