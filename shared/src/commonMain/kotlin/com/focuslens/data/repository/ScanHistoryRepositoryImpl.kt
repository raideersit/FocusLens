package com.focuslens.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.focuslens.db.FocusLensDb
import com.focuslens.domain.model.Food
import com.focuslens.domain.model.FoodScan
import com.focuslens.domain.model.NutritionFacts
import com.focuslens.domain.repository.ScanHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación del historial de escaneos usando SQLDelight (KMP).
 * Expone Flow reactivo: la UI se actualiza automáticamente al insertar/eliminar.
 */
class ScanHistoryRepositoryImpl(
    private val db: FocusLensDb
) : ScanHistoryRepository {

    private val queries = db.focusLensQueries

    override fun getAllScans(): Flow<List<FoodScan>> =
        queries.getAllScans()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }

    override fun getScanById(scanId: Long): Flow<FoodScan?> =
        queries.getScanById(scanId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }

    override suspend fun addScan(scan: FoodScan): Long {
        queries.insertScan(
            barcode       = scan.food.barcode,
            foodName      = scan.food.name,
            brand         = scan.food.brand,
            imageUrl      = scan.food.imageUrl,
            nutriScore    = scan.food.nutriScore,
            calories      = scan.food.nutrition.calories,
            proteins      = scan.food.nutrition.proteins,
            carbohydrates = scan.food.nutrition.carbohydrates,
            fats          = scan.food.nutrition.fats,
            saturatedFats = scan.food.nutrition.saturatedFats,
            sodium        = scan.food.nutrition.sodium,
            fiber         = scan.food.nutrition.fiber,
            sugars        = scan.food.nutrition.sugars,
            scannedAt     = scan.scannedAt,
            portionGrams  = scan.portionGrams,
            notes         = scan.notes
        )
        return queries.getLastInsertId().executeAsOne()
    }

    override suspend fun deleteScan(scanId: Long) =
        queries.deleteScanById(scanId)

    override suspend fun updateScanNotes(scanId: Long, notes: String) =
        queries.updateNotes(notes = notes, id = scanId)
}

/**
 * Extensión para convertir la entidad generada por SQLDelight a modelo de dominio.
 */
private fun com.focuslens.db.FoodScanEntity.toDomain() = FoodScan(
    id           = id,
    scannedAt    = scannedAt,
    portionGrams = portionGrams,
    notes        = notes,
    food = Food(
        barcode    = barcode,
        name       = foodName,
        brand      = brand,
        imageUrl   = imageUrl,
        nutriScore = nutriScore,
        nutrition  = NutritionFacts(
            calories      = calories,
            proteins      = proteins,
            carbohydrates = carbohydrates,
            fats          = fats,
            saturatedFats = saturatedFats,
            sodium        = sodium,
            fiber         = fiber,
            sugars        = sugars
        )
    )
)
