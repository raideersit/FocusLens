package com.focuslens.server.dto

import kotlinx.serialization.Serializable

/**
 * DTOs de usuario y sus metas nutricionales.
 * Nunca exponen el `passwordHash` hacia el cliente.
 */

/** Vista pública del usuario (sin datos sensibles). */
@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val goal: GoalDto,
    val createdAt: Long
)

/** Metas nutricionales del usuario. */
@Serializable
data class GoalDto(
    val type: String,
    val dailyCaloriesTarget: Int,
    val dailyProteinTarget: Int,
    val dailyCarbsTarget: Int,
    val dailyFatTarget: Int
)

/** Entrada para actualizar las metas del usuario (PUT /users/me/goals). */
@Serializable
data class UpdateGoalsRequest(
    val type: String,
    val dailyCaloriesTarget: Int,
    val dailyProteinTarget: Int,
    val dailyCarbsTarget: Int,
    val dailyFatTarget: Int
)
