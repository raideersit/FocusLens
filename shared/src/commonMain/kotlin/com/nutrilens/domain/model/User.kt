package com.nutrilens.domain.model

import com.nutrilens.platform.currentTimeMillis

/**
 * Entidad de usuario del dominio.
 */
data class User(
    val id: String = "",
    val name: String,
    val email: String,
    val goal: UserGoal = UserGoal(),
    val createdAt: Long = currentTimeMillis()
)
