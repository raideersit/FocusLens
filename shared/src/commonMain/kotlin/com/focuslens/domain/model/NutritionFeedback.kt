package com.focuslens.domain.model

/**
 * Feedback nutricional generado por AnalyzeNutritionUseCase.
 * Sealed class que implementa el patrón de tipos algebraicos en Kotlin.
 * Usada por el semáforo nutricional en la UI.
 */
sealed class NutritionFeedback {
    abstract val message: String
    abstract val nutrient: String

    data class Good(
        override val nutrient: String,
        override val message: String
    ) : NutritionFeedback()

    data class Warning(
        override val nutrient: String,
        override val message: String
    ) : NutritionFeedback()

    data class Info(
        override val nutrient: String,
        override val message: String
    ) : NutritionFeedback()
}
