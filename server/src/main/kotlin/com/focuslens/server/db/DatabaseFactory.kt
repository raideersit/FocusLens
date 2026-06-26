package com.focuslens.server.db

import com.focuslens.server.config.AppConfig
import com.focuslens.server.models.FoodScans
import com.focuslens.server.models.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Fábrica de conexión a la base de datos serverless **Neon (PostgreSQL)**.
 *
 * Usa un pool de conexiones HikariCP (recomendado para Neon, que cierra
 * conexiones inactivas) y crea automáticamente las tablas en el primer arranque
 * mediante `SchemaUtils.create`.
 */
object DatabaseFactory {

    fun init() {
        val dataSource = hikariDataSource()
        Database.connect(dataSource)

        // Creación de tablas (modelos) en Neon si aún no existen.
        transaction {
            SchemaUtils.create(Users, FoodScans)
        }
    }

    private fun hikariDataSource(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = AppConfig.databaseUrl
            username = AppConfig.databaseUser
            password = AppConfig.databasePassword
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 5            // Neon free-tier: pocas conexiones
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            // Neon exige SSL; ya viene en la URL (?sslmode=require)
            validate()
        }
        return HikariDataSource(config)
    }

    /**
     * Ejecuta un bloque dentro de una transacción suspendida (no bloquea el hilo
     * de la corrutina). Lo usan los repositorios.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
