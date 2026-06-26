package com.focuslens.server.routes

import com.focuslens.server.security.JwtService
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal

/**
 * Extrae el `userId` del token JWT validado.
 * Solo se invoca dentro de rutas protegidas por el plugin de autenticación.
 */
fun ApplicationCall.userId(): String =
    authentication.principal<JWTPrincipal>()!!
        .payload.getClaim(JwtService.CLAIM_USER_ID).asString()
