package com.focuslens.data.remote.dto

import com.focuslens.domain.model.Food
import com.focuslens.domain.model.FoodScan
import com.focuslens.domain.model.GoalType
import com.focuslens.domain.model.NutritionFacts
import com.focuslens.domain.model.User
import com.focuslens.domain.model.UserGoal
import kotlinx.serialization.Serializable

/**
 * DTOs que mapean el JSON del backend FocusLens (módulo :server).
 * Espejo de los DTOs del servidor — incluye TokenRequest/TokenResponse.
 */

// ── Autenticación ─────────────────────────────────────────────────────────────

@Serializable
data class TokenRequestDto(val email: String, val password: String)

@Serializable
data class RegisterRequestDto(val name: String, val email: String, val password: String)

@Serializable
data class TokenResponseDto(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresAt: Long,
    val user: UserResponseDto
)

// ── Usuario ───────────────────────────────────────────────────────────────────

@Serializable
data class UserResponseDto(
    val id: String,
    val name: String,
    val email: String,
    val goal: GoalDto,
    val createdAt: Long
)

@Serializable
data class GoalDto(
    val type: String,
    val dailyCaloriesTarget: Int,
    val dailyProteinTarget: Int,
    val dailyCarbsTarget: Int,
    val dailyFatTarget: Int
)

@Serializable
data class UpdateGoalsRequestDto(
    val type: String,
    val dailyCaloriesTarget: Int,
    val dailyProteinTarget: Int,
    val dailyCarbsTarget: Int,
    val dailyFatTarget: Int
)

// ── Escaneos ──────────────────────────────────────────────────────────────────

@Serializable
data class CreateScanRequestDto(
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

@Serializable
data class ScanResponseDto(
    val id: Long,
    val barcode: String,
    val foodName: String,
    val brand: String? = null,
    val imageUrl: String? = null,
    val nutriScore: String? = null,
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
    val notes: String? = null
)

@Serializable
data class UpdateNotesRequestDto(val notes: String)

// ── Mapeadores DTO → modelo de dominio ──────────────────────────────────────────

fun UserResponseDto.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    createdAt = createdAt,
    goal = UserGoal(
        type = runCatching { GoalType.valueOf(goal.type) }.getOrDefault(GoalType.BALANCED),
        dailyCaloriesTarget = goal.dailyCaloriesTarget,
        dailyProteinTarget = goal.dailyProteinTarget,
        dailyCarbsTarget = goal.dailyCarbsTarget,
        dailyFatTarget = goal.dailyFatTarget
    )
)

fun ScanResponseDto.toDomain(): FoodScan = FoodScan(
    id = id,
    scannedAt = scannedAt,
    portionGrams = portionGrams,
    notes = notes,
    food = Food(
        barcode = barcode,
        name = foodName,
        brand = brand,
        imageUrl = imageUrl,
        nutriScore = nutriScore,
        nutrition = NutritionFacts(
            calories = calories,
            proteins = proteins,
            carbohydrates = carbohydrates,
            fats = fats,
            saturatedFats = saturatedFats,
            sodium = sodium,
            fiber = fiber,
            sugars = sugars
        )
    )
)

fun FoodScan.toCreateRequest(): CreateScanRequestDto = CreateScanRequestDto(
    barcode = food.barcode,
    foodName = food.name,
    brand = food.brand,
    imageUrl = food.imageUrl,
    nutriScore = food.nutriScore,
    calories = food.nutrition.calories,
    proteins = food.nutrition.proteins,
    carbohydrates = food.nutrition.carbohydrates,
    fats = food.nutrition.fats,
    saturatedFats = food.nutrition.saturatedFats,
    sodium = food.nutrition.sodium,
    fiber = food.nutrition.fiber,
    sugars = food.nutrition.sugars,
    portionGrams = portionGrams,
    notes = notes
)
