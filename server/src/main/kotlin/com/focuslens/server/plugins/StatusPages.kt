package com.focuslens.server.plugins

import com.focuslens.server.dto.ApiError
import com.focuslens.server.service.EmailAlreadyExistsException
import com.focuslens.server.service.InvalidCredentialsException
import com.focuslens.server.service.ScanNotFoundException
import com.focuslens.server.service.UserNotFoundException
import com.focuslens.server.service.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

/**
 * Traduce las excepciones de negocio a códigos HTTP y un cuerpo [ApiError] uniforme.
 */
fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<ValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiError("validation_error", cause.message ?: "Datos inválidos"))
        }
        exception<InvalidCredentialsException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, ApiError("invalid_credentials", cause.message ?: ""))
        }
        exception<EmailAlreadyExistsException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, ApiError("email_exists", cause.message ?: ""))
        }
        exception<UserNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ApiError("user_not_found", cause.message ?: ""))
        }
        exception<ScanNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ApiError("scan_not_found", cause.message ?: ""))
        }
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Error no controlado", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiError("internal_error", "Ocurrió un error inesperado")
            )
        }
    }
}
