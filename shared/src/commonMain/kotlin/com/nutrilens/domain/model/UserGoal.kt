package com.nutrilens.domain.model

/** Tipos de objetivo nutricional que el usuario puede seleccionar en su perfil. */
enum class GoalType {
    LOSE_WEIGHT,    // Pérdida de peso
    MAINTAIN,       // Mantenimiento
    GAIN_MUSCLE,    // Ganancia muscular
    BALANCED        // Alimentación balanceada
}

/**
 * Metas nutricionales diarias del usuario.
 */
data class UserGoal(
    val type: GoalType = GoalType.BALANCED,
    val dailyCaloriesTarget: Int = 2000,
    val dailyProteinTarget: Int = 50,       // gramos
    val dailyCarbsTarget: Int = 250,        // gramos
    val dailyFatTarget: Int = 70            // gramos
)
