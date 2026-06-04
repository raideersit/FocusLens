package com.focuslens.domain.model

/**
 * Valores nutricionales por 100g del producto.
 * Todos los valores son por 100g para normalización.
 */
data class NutritionFacts(
    val calories: Double,              // kcal por 100g
    val proteins: Double,              // g por 100g
    val carbohydrates: Double,         // g por 100g
    val fats: Double,                  // g por 100g
    val saturatedFats: Double = 0.0,   // g por 100g
    val sodium: Double = 0.0,          // g por 100g
    val fiber: Double = 0.0,           // g por 100g
    val sugars: Double = 0.0           // g por 100g
)
