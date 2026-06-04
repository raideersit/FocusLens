package com.focuslens.domain.repository

import com.focuslens.domain.model.User
import com.focuslens.domain.model.UserGoal

/**
 * PRINCIPIO I (ISP): Separado en dos interfaces para que cada ViewModel
 * solo dependa de lo que realmente necesita.
 */

/** Operaciones de autenticación */
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun logout()
    suspend fun getCurrentUser(): User?
}

/** Operaciones sobre el perfil del usuario */
interface UserProfileRepository {
    suspend fun getUserProfile(userId: String): Result<User>
    suspend fun updateUserGoals(userId: String, goals: UserGoal): Result<Unit>
}
