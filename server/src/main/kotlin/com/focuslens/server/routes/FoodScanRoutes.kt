package com.focuslens.server.routes

import com.focuslens.server.dto.CreateScanRequest
import com.focuslens.server.dto.UpdateNotesRequest
import com.focuslens.server.plugins.JWT_AUTH
import com.focuslens.server.service.FoodScanService
import com.focuslens.server.service.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

/**
 * RUTAS del historial de escaneos (protegidas con JWT).
 *
 *   GET    /scans            → historial del usuario
 *   POST   /scans            → registrar escaneo
 *   DELETE /scans/{id}       → eliminar escaneo
 *   PATCH  /scans/{id}/notes → actualizar notas
 */
fun Route.foodScanRoutes(scanService: FoodScanService) {
    authenticate(JWT_AUTH) {
        route("/scans") {

            get {
                val history = scanService.getHistory(call.userId())
                call.respond(HttpStatusCode.OK, history)
            }

            post {
                val request = call.receive<CreateScanRequest>()
                val scan = scanService.addScan(call.userId(), request)
                call.respond(HttpStatusCode.Created, scan)
            }

            delete("/{id}") {
                val id = call.scanIdParam()
                scanService.deleteScan(call.userId(), id)
                call.respond(HttpStatusCode.NoContent)
            }

            patch("/{id}/notes") {
                val id = call.scanIdParam()
                val request = call.receive<UpdateNotesRequest>()
                scanService.updateNotes(call.userId(), id, request.notes)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}

private fun io.ktor.server.application.ApplicationCall.scanIdParam(): Long =
    parameters["id"]?.toLongOrNull() ?: throw ValidationException("Id de escaneo inválido")
