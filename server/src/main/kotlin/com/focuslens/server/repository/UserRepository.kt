package com.focuslens.server.repository

import com.focuslens.server.db.DatabaseFactory.dbQuery
import com.focuslens.server.dto.GoalDto
import com.focuslens.server.dto.UserResponse
import com.focuslens.server.models.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

/**
 * REPOSITORIO de usuarios.
 *
 * Única capa que conoce Exposed/SQL para la tabla `users`. Expone operaciones de
 * acceso a datos; la lógica de negocio vive en los servicios.
 */
class UserRepository {

    suspend fun findByEmail(email: String): UserRecord? = dbQuery {
        Users.selectAll()
            .where { Users.email eq email }
            .map { it.toRecord() }
            .singleOrNull()
    }

    suspend fun findById(id: UUID): UserRecord? = dbQuery {
        Users.selectAll()
            .where { Users.id eq id }
            .map { it.toRecord() }
            .singleOrNull()
    }

    suspend fun create(
        id: UUID,
        name: String,
        email: String,
        passwordHash: String,
        createdAt: Long
    ): UserRecord = dbQuery {
        Users.insert {
            it[Users.id] = id
            it[Users.name] = name
            it[Users.email] = email
            it[Users.passwordHash] = passwordHash
            it[Users.createdAt] = createdAt
        }
        // Se relee con los valores por defecto aplicados por la BD.
        Users.selectAll().where { Users.id eq id }.single().toRecord()
    }

    suspend fun updateGoals(
        id: UUID,
        type: String,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int
    ): Boolean = dbQuery {
        Users.update({ Users.id eq id }) {
            it[goalType] = type
            it[dailyCaloriesTarget] = calories
            it[dailyProteinTarget] = protein
            it[dailyCarbsTarget] = carbs
            it[dailyFatTarget] = fat
        } > 0
    }

    private fun ResultRow.toRecord() = UserRecord(
        id = this[Users.id],
        name = this[Users.name],
        email = this[Users.email],
        passwordHash = this[Users.passwordHash],
        goalType = this[Users.goalType],
        dailyCaloriesTarget = this[Users.dailyCaloriesTarget],
        dailyProteinTarget = this[Users.dailyProteinTarget],
        dailyCarbsTarget = this[Users.dailyCarbsTarget],
        dailyFatTarget = this[Users.dailyFatTarget],
        createdAt = this[Users.createdAt]
    )
}

/**
 * Registro interno de usuario (incluye el hash). NO se serializa hacia el cliente;
 * los servicios lo convierten a [UserResponse] antes de exponerlo.
 */
data class UserRecord(
    val id: UUID,
    val name: String,
    val email: String,
    val passwordHash: String,
    val goalType: String,
    val dailyCaloriesTarget: Int,
    val dailyProteinTarget: Int,
    val dailyCarbsTarget: Int,
    val dailyFatTarget: Int,
    val createdAt: Long
) {
    fun toResponse() = UserResponse(
        id = id.toString(),
        name = name,
        email = email,
        goal = GoalDto(
            type = goalType,
            dailyCaloriesTarget = dailyCaloriesTarget,
            dailyProteinTarget = dailyProteinTarget,
            dailyCarbsTarget = dailyCarbsTarget,
            dailyFatTarget = dailyFatTarget
        ),
        createdAt = createdAt
    )
}
