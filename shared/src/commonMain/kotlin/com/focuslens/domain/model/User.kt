package com.focuslens.domain.model

import com.focuslens.platform.currentTimeMillis

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
