package com.nutrilens.domain.model

import com.nutrilens.platform.currentTimeMillis

/**
 * Registro de un alimento escaneado por el usuario.
 * Permite calcular nutrientes según la porción real consumida.
 */
data class FoodScan(
    val id: Long = 0,
    val food: Food,
    val scannedAt: Long = currentTimeMillis(),
    val portionGrams: Double = 100.0,   // Porción consumida (default 100g)
    val notes: String? = null
) {
    /**
     * Calorías reales según la porción consumida.
     */
    val actualCalories: Double
        get() = food.nutrition.calories * (portionGrams / 100.0)

    val actualProteins: Double
        get() = food.nutrition.proteins * (portionGrams / 100.0)
}
