package com.focuslens.data.repository

import com.focuslens.data.local.SessionManager
import com.focuslens.data.remote.api.FocusLensApi
import com.focuslens.data.remote.dto.UpdateGoalsRequestDto
import com.focuslens.data.remote.dto.toDomain
import com.focuslens.domain.model.User
import com.focuslens.domain.model.UserGoal
import com.focuslens.domain.repository.AuthRepository
import com.focuslens.domain.repository.UserProfileRepository

/**
 * Implementación de autenticación y perfil contra el backend FocusLens (Ktor + Neon).
 *
 * Reemplaza a la versión local (SQLDelight). El token JWT se guarda en
 * [SessionManager] y se reenvía en cada petición autenticada vía [FocusLensApi].
 */
class RemoteAuthRepository(
    private val api: FocusLensApi,
    private val session: SessionManager
) : AuthRepository, UserProfileRepository {

    // ── AuthRepository ──────────────────────────────────────────────────────────

    override suspend fun login(email: String, password: String): Result<User> = runCatching {
        val response = api.login(email, password)
        persistSession(response.accessToken, response.user.toDomain())
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> = runCatching {
        val response = api.register(name, email, password)
        persistSession(response.accessToken, response.user.toDomain())
    }

    override suspend fun logout() {
        session.clearSession()
    }

    override suspend fun getCurrentUser(): User? {
        session.getToken() ?: return null
        return runCatching { api.getMe().toDomain() }.getOrNull()
    }

    // ── UserProfileRepository ─────────────────────────────────────────────────────

    override suspend fun getUserProfile(userId: String): Result<User> = runCatching {
        api.getMe().toDomain()
    }

    override suspend fun updateUserGoals(userId: String, goals: UserGoal): Result<Unit> = runCatching {
        api.updateGoals(
            UpdateGoalsRequestDto(
                type = goals.type.name,
                dailyCaloriesTarget = goals.dailyCaloriesTarget,
                dailyProteinTarget = goals.dailyProteinTarget,
                dailyCarbsTarget = goals.dailyCarbsTarget,
                dailyFatTarget = goals.dailyFatTarget
            )
        )
        Unit
    }

    // ── Privado ───────────────────────────────────────────────────────────────────

    private suspend fun persistSession(token: String, user: User): User {
        session.saveToken(token)
        session.saveSession(user.id, user.name, user.email)
        return user
    }
}
