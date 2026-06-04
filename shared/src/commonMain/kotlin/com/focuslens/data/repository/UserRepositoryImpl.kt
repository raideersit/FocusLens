package com.focuslens.data.repository

import com.focuslens.data.local.SessionManager
import com.focuslens.db.FocusLensDb
import com.focuslens.domain.exceptions.EmailAlreadyExistsException
import com.focuslens.domain.exceptions.InvalidCredentialsException
import com.focuslens.domain.model.GoalType
import com.focuslens.domain.model.User
import com.focuslens.domain.model.UserGoal
import com.focuslens.domain.repository.AuthRepository
import com.focuslens.domain.repository.UserProfileRepository
import com.focuslens.platform.currentTimeMillis
import com.focuslens.platform.randomUUID
import com.focuslens.platform.sha256

/**
 * Implementación local de autenticación usando SQLDelight + DataStore para sesión.
 * En una versión futura se puede migrar a Firebase sin tocar el dominio.
 */
class UserRepositoryImpl(
    private val db: FocusLensDb,
    private val sessionManager: SessionManager
) : AuthRepository, UserProfileRepository {

    private val queries = db.nutriLensQueries

    // ── AuthRepository ────────────────────────────────────────────────────────

    override suspend fun login(email: String, password: String): Result<User> {
        val entity = queries.getUserByEmail(email).executeAsOneOrNull()
            ?: return Result.failure(InvalidCredentialsException())
        val hash = sha256(password)
        if (entity.passwordHash != hash) {
            return Result.failure(InvalidCredentialsException())
        }
        val user = entity.toDomain()
        sessionManager.saveSession(user.id, user.name, user.email)
        return Result.success(user)
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        if (queries.getUserByEmail(email).executeAsOneOrNull() != null) {
            return Result.failure(EmailAlreadyExistsException())
        }
        val userId = randomUUID()
        val now = currentTimeMillis()
        return try {
            queries.insertUser(
                id                  = userId,
                name                = name,
                email               = email,
                passwordHash        = sha256(password),
                goalType            = GoalType.BALANCED.name,
                dailyCaloriesTarget = 2000,
                dailyProteinTarget  = 50,
                dailyCarbsTarget    = 250,
                dailyFatTarget      = 70,
                createdAt           = now
            )
            sessionManager.saveSession(userId, name, email)
            val user = User(
                id        = userId,
                name      = name,
                email     = email,
                createdAt = now
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override suspend fun getCurrentUser(): User? {
        val userId = sessionManager.getActiveUserId() ?: return null
        return queries.getUserById(userId).executeAsOneOrNull()?.toDomain()
    }

    // ── UserProfileRepository ─────────────────────────────────────────────────

    override suspend fun getUserProfile(userId: String): Result<User> {
        val entity = queries.getUserById(userId).executeAsOneOrNull()
            ?: return Result.failure(Exception("Usuario no encontrado"))
        return Result.success(entity.toDomain())
    }

    override suspend fun updateUserGoals(userId: String, goals: UserGoal): Result<Unit> {
        val entity = queries.getUserById(userId).executeAsOneOrNull()
            ?: return Result.failure(Exception("Usuario no encontrado"))
        queries.updateUser(
            name                = entity.name,
            email               = entity.email,
            passwordHash        = entity.passwordHash,
            goalType            = goals.type.name,
            dailyCaloriesTarget = goals.dailyCaloriesTarget.toLong(),
            dailyProteinTarget  = goals.dailyProteinTarget.toLong(),
            dailyCarbsTarget    = goals.dailyCarbsTarget.toLong(),
            dailyFatTarget      = goals.dailyFatTarget.toLong(),
            id                  = userId
        )
        return Result.success(Unit)
    }
}

/**
 * Extensión para convertir la entidad SQLDelight a modelo de dominio.
 */
private fun com.focuslens.db.UserEntity.toDomain() = User(
    id        = id,
    name      = name,
    email     = email,
    createdAt = createdAt,
    goal      = UserGoal(
        type                = GoalType.valueOf(goalType),
        dailyCaloriesTarget = dailyCaloriesTarget.toInt(),
        dailyProteinTarget  = dailyProteinTarget.toInt(),
        dailyCarbsTarget    = dailyCarbsTarget.toInt(),
        dailyFatTarget      = dailyFatTarget.toInt()
    )
)
