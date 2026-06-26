package com.focuslens.server.dto

import kotlinx.serialization.Serializable

/**
 * DTOs del historial de escaneos.
 */

/** Entrada para registrar un escaneo (POST /scans). */
@Serializable
data class CreateScanRequest(
    val barcode: String,
    val foodName: String,
    val brand: String? = null,
    val imageUrl: String? = null,
    val nutriScore: String? = null,
    val calories: Double,
    val proteins: Double,
    val carbohydrates: Double,
    val fats: Double,
    val saturatedFats: Double = 0.0,
    val sodium: Double = 0.0,
    val fiber: Double = 0.0,
    val sugars: Double = 0.0,
    val portionGrams: Double = 100.0,
    val notes: String? = null
)

/** Vista de un escaneo del historial (salida de GET /scans). */
@Serializable
data class ScanResponse(
    val id: Long,
    val barcode: String,
    val foodName: String,
    val brand: String?,
    val imageUrl: String?,
    val nutriScore: String?,
    val calories: Double,
    val proteins: Double,
    val carbohydrates: Double,
    val fats: Double,
    val saturatedFats: Double,
    val sodium: Double,
    val fiber: Double,
    val sugars: Double,
    val scannedAt: Long,
    val portionGrams: Double,
    val notes: String?
)

/** Entrada para actualizar las notas de un escaneo (PATCH /scans/{id}/notes). */
@Serializable
data class UpdateNotesRequest(
    val notes: String
)
