package com.focuslens.server.routes

import com.focuslens.server.dto.RegisterRequest
import com.focuslens.server.dto.TokenRequest
import com.focuslens.server.service.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

/**
 * RUTAS de autenticación (públicas).
 *
 *   POST /auth/register → RegisterRequest → TokenResponse
 *   POST /auth/login    → TokenRequest    → TokenResponse
 */
fun Route.authRoutes(authService: AuthService) {
    route("/auth") {

        post("/register") {
            val request = call.receive<RegisterRequest>()
            val token = authService.register(request)
            call.respond(HttpStatusCode.Created, token)
        }

        post("/login") {
            val credentials = call.receive<TokenRequest>()
            val token = authService.login(credentials)
            call.respond(HttpStatusCode.OK, token)
        }
    }
}
