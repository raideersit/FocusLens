package com.nutrilens.domain.model

/**
 * Entidad principal del dominio: representa un alimento escaneado.
 * Esta clase NO tiene dependencias de Android ni de frameworks externos.
 * Es el corazón del modelo de datos de NutriLens.
 */
data class Food(
    val barcode: String,
    val name: String,
    val brand: String? = null,
    val imageUrl: String? = null,
    val quantity: String? = null,
    val nutrition: NutritionFacts,
    val nutriScore: String? = null   // A, B, C, D, E (NutriScore europeo)
)
