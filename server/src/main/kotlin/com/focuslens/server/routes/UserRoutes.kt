package com.focuslens.server.routes

import com.focuslens.server.dto.UpdateGoalsRequest
import com.focuslens.server.plugins.JWT_AUTH
import com.focuslens.server.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route

/**
 * RUTAS de usuario (protegidas con JWT).
 *
 *   GET /users/me        → perfil del usuario autenticado
 *   PUT /users/me/goals  → actualizar metas nutricionales
 */
fun Route.userRoutes(userService: UserService) {
    authenticate(JWT_AUTH) {
        route("/users/me") {

            get {
                val profile = userService.getProfile(call.userId())
                call.respond(HttpStatusCode.OK, profile)
            }

            put("/goals") {
                val request = call.receive<UpdateGoalsRequest>()
                val updated = userService.updateGoals(call.userId(), request)
                call.respond(HttpStatusCode.OK, updated)
            }
        }
    }
}
