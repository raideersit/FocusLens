package com.focuslens.server.models

import org.jetbrains.exposed.sql.Table

/**
 * MODELO / TABLA: historial de escaneos.
 *
 * Definición de la tabla `food_scans` en PostgreSQL (Neon). Cada escaneo
 * pertenece a un usuario (clave foránea `user_id` → `users.id`), de modo que el
 * historial es por usuario. Al borrar un usuario se borran sus escaneos (CASCADE).
 */
object FoodScans : Table("food_scans") {
    val id = long("id").autoIncrement()
    val userId = uuid("user_id").references(Users.id, onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE)

    val barcode = varchar("barcode", 32)
    val foodName = varchar("food_name", 200)
    val brand = varchar("brand", 160).nullable()
    val imageUrl = varchar("image_url", 500).nullable()
    val nutriScore = varchar("nutri_score", 4).nullable()

    // Información nutricional por 100 g
    val calories = double("calories")
    val proteins = double("proteins")
    val carbohydrates = double("carbohydrates")
    val fats = double("fats")
    val saturatedFats = double("saturated_fats")
    val sodium = double("sodium")
    val fiber = double("fiber")
    val sugars = double("sugars")

    val scannedAt = long("scanned_at")
    val portionGrams = double("portion_grams").default(100.0)
    val notes = varchar("notes", 500).nullable()

    override val primaryKey = PrimaryKey(id)
}
