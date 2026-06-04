package com.focuslens.domain.usecase.food

import com.focuslens.domain.model.Food
import com.focuslens.domain.repository.FoodRepository

/**
 * Obtiene la información nutricional de un alimento a partir de su código de barras.
 * Valida que el código no esté vacío antes de consultar el repositorio.
 */
class GetFoodByBarcodeUseCase(
    private val repository: FoodRepository
) {
    suspend operator fun invoke(barcode: String): Result<Food> {
        if (barcode.isBlank()) {
            return Result.failure(IllegalArgumentException("El código de barras no puede estar vacío"))
        }
        return repository.getFoodByBarcode(barcode.trim())
    }
}
