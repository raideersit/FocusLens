package com.focuslens.server.service

import com.focuslens.server.dto.RegisterRequest
import com.focuslens.server.dto.TokenRequest
import com.focuslens.server.dto.TokenResponse
import com.focuslens.server.repository.UserRepository
import com.focuslens.server.security.JwtService
import com.focuslens.server.security.PasswordHasher
import java.util.UUID

/**
 * SERVICIO de autenticación.
 *
 * Orquesta el flujo de seguridad:
 *   1. Valida credenciales / unicidad del correo.
 *   2. Hashea o verifica la contraseña (BCrypt).
 *   3. Emite el JWT (TokenResponse).
 */
class AuthService(
    private val userRepository: UserRepository
) {

    /** Registra un usuario nuevo y devuelve su token. */
    suspend fun register(request: RegisterRequest): TokenResponse {
        validateRegister(request)
        if (userRepository.findByEmail(request.email.trim()) != null) {
            throw EmailAlreadyExistsException()
        }

        val user = userRepository.create(
            id = UUID.randomUUID(),
            name = request.name.trim(),
            email = request.email.trim().lowercase(),
            passwordHash = PasswordHasher.hash(request.password),
            createdAt = System.currentTimeMillis()
        )
        return user.toTokenResponse()
    }

    /** Valida credenciales (TokenRequest) y devuelve un token si son correctas. */
    suspend fun login(request: TokenRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email.trim().lowercase())
            ?: throw InvalidCredentialsException()

        if (!PasswordHasher.verify(request.password, user.passwordHash)) {
            throw InvalidCredentialsException()
        }
        return user.toTokenResponse()
    }

    private fun com.focuslens.server.repository.UserRecord.toTokenResponse(): TokenResponse {
        val issued = JwtService.generateToken(id.toString(), email)
        return TokenResponse(
            accessToken = issued.token,
            expiresAt = issued.expiresAt,
            user = toResponse()
        )
    }

    private fun validateRegister(request: RegisterRequest) {
        if (request.name.isBlank()) throw ValidationException("El nombre es obligatorio")
        if (!request.email.contains("@")) throw ValidationException("Correo inválido")
        if (request.password.length < 6) {
            throw ValidationException("La contraseña debe tener al menos 6 caracteres")
        }
    }
}
