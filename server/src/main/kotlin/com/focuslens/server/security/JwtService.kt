package com.focuslens.server.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.focuslens.server.config.AppConfig
import java.util.Date

/**
 * SEGURIDAD · JWT (JSON Web Token).
 *
 * Crea y verifica los tokens firmados con HMAC-256 a partir del secreto definido
 * en la configuración. El `userId` viaja como claim para identificar al usuario
 * en las rutas protegidas.
 */
object JwtService {

    const val CLAIM_USER_ID = "userId"
    const val CLAIM_EMAIL = "email"

    private val algorithm = Algorithm.HMAC256(AppConfig.jwtSecret)

    /** Verificador usado por el plugin de autenticación de Ktor. */
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(AppConfig.jwtIssuer)
        .withAudience(AppConfig.jwtAudience)
        .build()

    /** Datos de un token recién emitido. */
    data class IssuedToken(val token: String, val expiresAt: Long)

    /**
     * Genera un token firmado para el usuario indicado.
     * @return el JWT y su timestamp de expiración (epoch millis).
     */
    fun generateToken(userId: String, email: String): IssuedToken {
        val expiresAt = System.currentTimeMillis() + AppConfig.jwtExpiresInMs
        val token = JWT.create()
            .withIssuer(AppConfig.jwtIssuer)
            .withAudience(AppConfig.jwtAudience)
            .withClaim(CLAIM_USER_ID, userId)
            .withClaim(CLAIM_EMAIL, email)
            .withExpiresAt(Date(expiresAt))
            .sign(algorithm)
        return IssuedToken(token, expiresAt)
    }
}
