package com.focuslens.server.dto

import kotlinx.serialization.Serializable

/**
 * DTOs de autenticación y seguridad (flujo JWT).
 *
 * Flujo:  Credenciales (TokenRequest) ──► AuthService valida ──► TokenResponse (JWT)
 */

/** Credenciales de inicio de sesión. Entrada de POST /auth/login. */
@Serializable
data class TokenRequest(
    val email: String,
    val password: String
)

/** Datos de registro de un nuevo usuario. Entrada de POST /auth/register. */
@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

/**
 * Respuesta con el token de acceso. Salida de /auth/login y /auth/register.
 *
 * @param accessToken JWT firmado.
 * @param tokenType   Esquema de autorización ("Bearer").
 * @param expiresAt   Timestamp (epoch millis) de expiración del token.
 * @param user        Datos públicos del usuario autenticado.
 */
@Serializable
data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresAt: Long,
    val user: UserResponse
)
