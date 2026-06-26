package com.focuslens.server.config

import io.github.cdimascio.dotenv.dotenv

/**
 * Configuración central de la aplicación.
 *
 * Lee los valores desde variables de entorno y, si existe, desde un archivo `.env`
 * en la raíz del módulo `server/`. Así NO se exponen credenciales (la connection
 * string de Neon ni el secreto JWT) en el código fuente.
 *
 * Variables esperadas (ver `.env.example`):
 *   - DATABASE_URL   → jdbc:postgresql://...neon.tech/db?sslmode=require
 *   - DATABASE_USER  → usuario de Neon
 *   - DATABASE_PASSWORD → contraseña de Neon
 *   - JWT_SECRET     → clave secreta para firmar los tokens
 *   - JWT_ISSUER / JWT_AUDIENCE / JWT_REALM
 *   - JWT_EXPIRES_IN_MS → vigencia del token en milisegundos
 *   - SERVER_PORT
 */
object AppConfig {

    private val dotenv = dotenv {
        ignoreIfMissing = true // permite correr en producción solo con env vars
    }

    private fun get(key: String, default: String? = null): String =
        dotenv[key] ?: System.getenv(key) ?: default
        ?: error("Falta la variable de configuración requerida: $key")

    // ── Servidor ──────────────────────────────────────────────────────────────
    val serverPort: Int = get("SERVER_PORT", "8080").toInt()
    val serverHost: String = get("SERVER_HOST", "0.0.0.0")

    // ── Base de datos (Neon / PostgreSQL) ──────────────────────────────────────
    val databaseUrl: String = get("DATABASE_URL")
    val databaseUser: String = get("DATABASE_USER")
    val databasePassword: String = get("DATABASE_PASSWORD")

    // ── JWT ─────────────────────────────────────────────────────────────────────
    val jwtSecret: String = get("JWT_SECRET")
    val jwtIssuer: String = get("JWT_ISSUER", "focuslens-api")
    val jwtAudience: String = get("JWT_AUDIENCE", "focuslens-app")
    val jwtRealm: String = get("JWT_REALM", "focuslens")
    val jwtExpiresInMs: Long = get("JWT_EXPIRES_IN_MS", "86400000").toLong() // 24 h
}
