package com.focuslens.server

import com.focuslens.server.config.AppConfig
import com.focuslens.server.db.DatabaseFactory
import com.focuslens.server.plugins.configureRouting
import com.focuslens.server.plugins.configureSecurity
import com.focuslens.server.plugins.configureSerialization
import com.focuslens.server.plugins.configureStatusPages
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

/**
 * Punto de entrada del backend FocusLens (Ktor + Netty).
 *
 * Arranque:
 *   1. Conecta a Neon (PostgreSQL) y crea las tablas si no existen.
 *   2. Instala los plugins (JSON, JWT, errores, CORS, logging).
 *   3. Registra las rutas de la API.
 */
fun main() {
    // 1. Base de datos (Neon) — crea las tablas en el primer arranque.
    DatabaseFactory.init()

    // 2. Servidor HTTP.
    embeddedServer(
        Netty,
        port = AppConfig.serverPort,
        host = AppConfig.serverHost,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    val deps = Dependencies()

    install(DefaultHeaders)
    install(CallLogging)
    install(CORS) {
        anyHost() // En producción, restringir a los dominios de la app.
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }

    configureSerialization()
    configureSecurity()
    configureStatusPages()
    configureRouting(deps)
}
