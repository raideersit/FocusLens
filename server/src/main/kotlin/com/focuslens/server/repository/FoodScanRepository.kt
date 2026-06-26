package com.focuslens.server.repository

import com.focuslens.server.db.DatabaseFactory.dbQuery
import com.focuslens.server.dto.CreateScanRequest
import com.focuslens.server.dto.ScanResponse
import com.focuslens.server.models.FoodScans
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

/**
 * REPOSITORIO del historial de escaneos.
 *
 * Todas las operaciones están acotadas al `userId` para que un usuario solo pueda
 * ver o modificar sus propios escaneos.
 */
class FoodScanRepository {

    suspend fun findAllByUser(userId: UUID): List<ScanResponse> = dbQuery {
        FoodScans.selectAll()
            .where { FoodScans.userId eq userId }
            .orderBy(FoodScans.scannedAt to SortOrder.DESC)
            .map { it.toResponse() }
    }

    suspend fun create(userId: UUID, req: CreateScanRequest, scannedAt: Long): ScanResponse = dbQuery {
        val newId = FoodScans.insert {
            it[FoodScans.userId] = userId
            it[barcode] = req.barcode
            it[foodName] = req.foodName
            it[brand] = req.brand
            it[imageUrl] = req.imageUrl
            it[nutriScore] = req.nutriScore
            it[calories] = req.calories
            it[proteins] = req.proteins
            it[carbohydrates] = req.carbohydrates
            it[fats] = req.fats
            it[saturatedFats] = req.saturatedFats
            it[sodium] = req.sodium
            it[fiber] = req.fiber
            it[sugars] = req.sugars
            it[FoodScans.scannedAt] = scannedAt
            it[portionGrams] = req.portionGrams
            it[notes] = req.notes
        } get FoodScans.id

        FoodScans.selectAll().where { FoodScans.id eq newId }.single().toResponse()
    }

    suspend fun deleteByIdForUser(id: Long, userId: UUID): Boolean = dbQuery {
        FoodScans.deleteWhere { (FoodScans.id eq id) and (FoodScans.userId eq userId) } > 0
    }

    suspend fun updateNotes(id: Long, userId: UUID, notes: String): Boolean = dbQuery {
        FoodScans.update({ (FoodScans.id eq id) and (FoodScans.userId eq userId) }) {
            it[FoodScans.notes] = notes
        } > 0
    }

    private fun ResultRow.toResponse() = ScanResponse(
        id = this[FoodScans.id],
        barcode = this[FoodScans.barcode],
        foodName = this[FoodScans.foodName],
        brand = this[FoodScans.brand],
        imageUrl = this[FoodScans.imageUrl],
        nutriScore = this[FoodScans.nutriScore],
        calories = this[FoodScans.calories],
        proteins = this[FoodScans.proteins],
        carbohydrates = this[FoodScans.carbohydrates],
        fats = this[FoodScans.fats],
        saturatedFats = this[FoodScans.saturatedFats],
        sodium = this[FoodScans.sodium],
        fiber = this[FoodScans.fiber],
        sugars = this[FoodScans.sugars],
        scannedAt = this[FoodScans.scannedAt],
        portionGrams = this[FoodScans.portionGrams],
        notes = this[FoodScans.notes]
    )
}
