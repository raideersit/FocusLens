package com.focuslens.server.plugins

import com.focuslens.server.Dependencies
import com.focuslens.server.routes.authRoutes
import com.focuslens.server.routes.foodScanRoutes
import com.focuslens.server.routes.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

/**
 * Registra todas las rutas de la API bajo el prefijo `/api/v1`.
 */
fun Application.configureRouting(deps: Dependencies) {
    routing {
        // Health check (público) para verificar que el servidor está vivo.
        get("/health") {
            call.respond(mapOf("status" to "ok"))
        }

        // API v1
        route("/api/v1") {
            authRoutes(deps.authService)
            userRoutes(deps.userService)
            foodScanRoutes(deps.foodScanService)
        }
    }
}
