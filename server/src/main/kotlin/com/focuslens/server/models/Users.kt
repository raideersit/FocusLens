package com.focuslens.server.models

import org.jetbrains.exposed.sql.Table

/**
 * MODELO / TABLA: usuarios.
 *
 * Definición de la tabla `users` en PostgreSQL (Neon) usando Exposed.
 * La contraseña se almacena SIEMPRE como hash BCrypt, nunca en texto plano.
 */
object Users : Table("users") {
    val id = uuid("id")
    val name = varchar("name", 120)
    val email = varchar("email", 180).uniqueIndex()
    val passwordHash = varchar("password_hash", 100)

    // Metas nutricionales del usuario
    val goalType = varchar("goal_type", 30).default("BALANCED")
    val dailyCaloriesTarget = integer("daily_calories_target").default(2000)
    val dailyProteinTarget = integer("daily_protein_target").default(50)
    val dailyCarbsTarget = integer("daily_carbs_target").default(250)
    val dailyFatTarget = integer("daily_fat_target").default(70)

    val createdAt = long("created_at")

    override val primaryKey = PrimaryKey(id)
}
