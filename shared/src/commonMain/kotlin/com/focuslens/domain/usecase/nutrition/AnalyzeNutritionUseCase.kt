package com.focuslens.domain.usecase.nutrition

import com.focuslens.domain.model.Food
import com.focuslens.domain.model.GoalType
import com.focuslens.domain.model.NutritionFeedback
import com.focuslens.domain.model.UserGoal

/**
 * PATRÓN STRATEGY + PRINCIPIO O (OCP):
 * Cada estrategia implementa esta interfaz. Para agregar un nuevo tipo
 * de análisis, se crea una nueva clase sin modificar las existentes.
 */
interface NutritionAnalysisStrategy {
    fun analyze(food: Food, goal: UserGoal): List<NutritionFeedback>
}

// ── Estrategia 1: Pérdida de peso ──────────────────────────────────────────

class WeightLossStrategy : NutritionAnalysisStrategy {
    override fun analyze(food: Food, goal: UserGoal): List<NutritionFeedback> {
        val list = mutableListOf<NutritionFeedback>()
        with(food.nutrition) {
            if (calories > 300)
                list += NutritionFeedback.Warning("Calorías", "Alto en calorías para bajar de peso (${calories.toInt()} kcal/100g)")
            else
                list += NutritionFeedback.Good("Calorías", "Bajo en calorías ✓ (${calories.toInt()} kcal/100g)")

            if (fats > 17.5)
                list += NutritionFeedback.Warning("Grasas", "Alto en grasas (${fats}g/100g)")

            if (fiber >= 3)
                list += NutritionFeedback.Good("Fibra", "Buen aporte de fibra ✓ (${fiber}g/100g)")

            if (sugars > 22.5)
                list += NutritionFeedback.Warning("Azúcares", "Alto en azúcares (${sugars}g/100g)")
        }
        return list
    }
}

// ── Estrategia 2: Ganancia muscular ────────────────────────────────────────

class MuscleGainStrategy : NutritionAnalysisStrategy {
    override fun analyze(food: Food, goal: UserGoal): List<NutritionFeedback> {
        val list = mutableListOf<NutritionFeedback>()
        with(food.nutrition) {
            if (proteins >= 20)
                list += NutritionFeedback.Good("Proteínas", "Excelente fuente de proteínas ✓ (${proteins}g/100g)")
            else if (proteins >= 10)
                list += NutritionFeedback.Info("Proteínas", "Proteínas moderadas (${proteins}g/100g)")
            else
                list += NutritionFeedback.Warning("Proteínas", "Bajo en proteínas (${proteins}g/100g)")

            if (calories >= 200)
                list += NutritionFeedback.Good("Calorías", "Buen aporte calórico ✓ (${calories.toInt()} kcal/100g)")
        }
        return list
    }
}

// ── Estrategia 3: Balanceado ───────────────────────────────────────────────

class BalancedStrategy : NutritionAnalysisStrategy {
    override fun analyze(food: Food, goal: UserGoal): List<NutritionFeedback> {
        val list = mutableListOf<NutritionFeedback>()
        with(food.nutrition) {
            // Semáforo estándar de la OMS
            val calStatus = when {
                calories > 400 -> NutritionFeedback.Warning("Calorías", "Alto en calorías (${calories.toInt()} kcal/100g)")
                else -> NutritionFeedback.Good("Calorías", "${calories.toInt()} kcal/100g")
            }
            list += calStatus

            val sodiumStatus = when {
                sodium > 1.5 -> NutritionFeedback.Warning("Sodio", "Muy alto en sodio (${sodium}g/100g)")
                sodium > 0.6 -> NutritionFeedback.Info("Sodio", "Sodio moderado (${sodium}g/100g)")
                else -> NutritionFeedback.Good("Sodio", "Bajo en sodio ✓")
            }
            list += sodiumStatus

            val satFatStatus = when {
                saturatedFats > 5 -> NutritionFeedback.Warning("Grasas saturadas", "Alto en grasas saturadas (${saturatedFats}g/100g)")
                else -> NutritionFeedback.Good("Grasas saturadas", "Grasas saturadas aceptables ✓")
            }
            list += satFatStatus
        }
        return list
    }
}

// ── Caso de uso principal ──────────────────────────────────────────────────

/**
 * PRINCIPIO O (OCP): Abierto a extensión (nuevas estrategias) sin modificar este UseCase.
 * Selecciona la estrategia correcta según el objetivo del usuario.
 */
class AnalyzeNutritionUseCase {

    operator fun invoke(food: Food, goal: UserGoal): List<NutritionFeedback> {
        val strategy: NutritionAnalysisStrategy = when (goal.type) {
            GoalType.LOSE_WEIGHT  -> WeightLossStrategy()
            GoalType.GAIN_MUSCLE  -> MuscleGainStrategy()
            GoalType.MAINTAIN,
            GoalType.BALANCED     -> BalancedStrategy()
        }
        return strategy.analyze(food, goal)
    }
}
