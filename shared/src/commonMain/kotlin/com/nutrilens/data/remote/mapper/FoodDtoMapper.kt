package com.nutrilens.data.remote.mapper

import com.nutrilens.data.remote.dto.NutrimentsDto
import com.nutrilens.data.remote.dto.ProductDto
import com.nutrilens.domain.model.Food
import com.nutrilens.domain.model.NutritionFacts

/**
 * PRINCIPIO S (SRP): Responsabilidad única — convertir DTOs de la API
 * en entidades del dominio. Nada más.
 */
class FoodDtoMapper {

    fun toDomain(dto: ProductDto): Food = Food(
        barcode    = dto.code?.trim() ?: "",
        name       = dto.productName?.trim().takeIf { !it.isNullOrBlank() } ?: "Producto sin nombre",
        brand      = dto.brands?.trim().takeIf { !it.isNullOrBlank() },
        imageUrl   = dto.imageUrl?.trim().takeIf { !it.isNullOrBlank() },
        quantity   = dto.quantity?.trim().takeIf { !it.isNullOrBlank() },
        nutriScore = dto.nutriscoreGrade?.uppercase().takeIf { !it.isNullOrBlank() },
        nutrition  = mapNutrition(dto.nutriments)
    )

    private fun mapNutrition(dto: NutrimentsDto?): NutritionFacts = NutritionFacts(
        calories      = dto?.energyKcal    ?: 0.0,
        proteins      = dto?.proteins      ?: 0.0,
        carbohydrates = dto?.carbohydrates ?: 0.0,
        fats          = dto?.fat           ?: 0.0,
        saturatedFats = dto?.saturatedFat  ?: 0.0,
        sodium        = dto?.sodium        ?: 0.0,
        fiber         = dto?.fiber         ?: 0.0,
        sugars        = dto?.sugars        ?: 0.0
    )
}
