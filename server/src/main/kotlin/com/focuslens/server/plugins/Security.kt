package com.focuslens.server.plugins

import com.focuslens.server.config.AppConfig
import com.focuslens.server.security.JwtService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond

const val JWT_AUTH = "auth-jwt"

/**
 * Configura la autenticación JWT (Bearer).
 *
 * Valida la firma, el issuer, la audiencia y la expiración del token. Si es válido,
 * el `userId` queda disponible en el [JWTPrincipal] para las rutas protegidas.
 */
fun Application.configureSecurity() {
    install(Authentication) {
        jwt(JWT_AUTH) {
            realm = AppConfig.jwtRealm
            verifier(JwtService.verifier)
            validate { credential ->
                val userId = credential.payload.getClaim(JwtService.CLAIM_USER_ID).asString()
                if (!userId.isNullOrBlank()) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token inválido o expirado"))
            }
        }
    }
}
