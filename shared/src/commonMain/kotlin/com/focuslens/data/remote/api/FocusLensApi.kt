package com.focuslens.data.remote.api

import com.focuslens.data.local.SessionManager
import com.focuslens.data.remote.dto.CreateScanRequestDto
import com.focuslens.data.remote.dto.RegisterRequestDto
import com.focuslens.data.remote.dto.ScanResponseDto
import com.focuslens.data.remote.dto.TokenRequestDto
import com.focuslens.data.remote.dto.TokenResponseDto
import com.focuslens.data.remote.dto.UpdateGoalsRequestDto
import com.focuslens.data.remote.dto.UpdateNotesRequestDto
import com.focuslens.data.remote.dto.UserResponseDto
import com.focuslens.domain.exceptions.EmailAlreadyExistsException
import com.focuslens.domain.exceptions.InvalidCredentialsException
import com.focuslens.domain.exceptions.NetworkException
import com.focuslens.platform.serverBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

/**
 * Cliente HTTP del backend FocusLens (módulo :server).
 *
 * Reutiliza el [HttpClient] de Ktor (el mismo que OpenFoodFacts) y adjunta el
 * token JWT del [SessionManager] en las peticiones autenticadas. Mapea los
 * códigos HTTP de error a excepciones de dominio.
 */
class FocusLensApi(
    private val client: HttpClient,
    private val session: SessionManager
) {
    private val baseUrl = serverBaseUrl() + "/api/v1"

    // ── Autenticación ───────────────────────────────────────────────────────────

    suspend fun register(name: String, email: String, password: String): TokenResponseDto {
        val response = client.post("$baseUrl/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequestDto(name, email, password))
        }
        when (response.status) {
            HttpStatusCode.Created, HttpStatusCode.OK -> return response.body()
            HttpStatusCode.Conflict -> throw EmailAlreadyExistsException()
            else -> throw response.toError()
        }
    }

    suspend fun login(email: String, password: String): TokenResponseDto {
        val response = client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequestDto(email, password))
        }
        when (response.status) {
            HttpStatusCode.OK -> return response.body()
            HttpStatusCode.Unauthorized -> throw InvalidCredentialsException()
            else -> throw response.toError()
        }
    }

    // ── Usuario (autenticado) ─────────────────────────────────────────────────────

    suspend fun getMe(): UserResponseDto {
        val token = requireToken()
        val response = client.get("$baseUrl/users/me") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        return response.requireSuccess().body()
    }

    suspend fun updateGoals(request: UpdateGoalsRequestDto): UserResponseDto {
        val token = requireToken()
        val response = client.put("$baseUrl/users/me/goals") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.requireSuccess().body()
    }

    // ── Escaneos (autenticado) ─────────────────────────────────────────────────────

    suspend fun getScans(): List<ScanResponseDto> {
        val token = requireToken()
        val response = client.get("$baseUrl/scans") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        return response.requireSuccess().body()
    }

    suspend fun createScan(request: CreateScanRequestDto): ScanResponseDto {
        val token = requireToken()
        val response = client.post("$baseUrl/scans") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.requireSuccess().body()
    }

    suspend fun deleteScan(id: Long) {
        val token = requireToken()
        val response = client.delete("$baseUrl/scans/$id") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        response.requireSuccess()
    }

    suspend fun updateNotes(id: Long, notes: String) {
        val token = requireToken()
        val response = client.patch("$baseUrl/scans/$id/notes") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(UpdateNotesRequestDto(notes))
        }
        response.requireSuccess()
    }

    // ── Helpers ───────────────────────────────────────────────────────────────────

    /** Lee el token JWT activo (suspend) antes de construir la petición. */
    private suspend fun requireToken(): String =
        session.getToken() ?: throw NetworkException("Sesión no iniciada")

    private fun HttpResponse.requireSuccess(): HttpResponse {
        if (status.value !in 200..299) throw toError()
        return this
    }

    private fun HttpResponse.toError(): Exception = when (status) {
        HttpStatusCode.Unauthorized -> InvalidCredentialsException()
        else -> NetworkException("Error del servidor (${status.value})")
    }
}
