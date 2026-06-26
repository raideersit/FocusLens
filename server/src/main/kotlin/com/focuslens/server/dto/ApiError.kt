package com.focuslens.server.dto

import kotlinx.serialization.Serializable

/** Cuerpo estándar de respuesta de error de la API. */
@Serializable
data class ApiError(
    val error: String,
    val message: String
)
