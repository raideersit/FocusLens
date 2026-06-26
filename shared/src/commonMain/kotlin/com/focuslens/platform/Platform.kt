package com.focuslens.platform

/**
 * Obtiene el timestamp actual en milisegundos.
 * Reemplaza System.currentTimeMillis() que no es multiplataforma.
 */
expect fun currentTimeMillis(): Long

/**
 * Genera un UUID único como String.
 * Reemplaza java.util.UUID.randomUUID().toString()
 */
expect fun randomUUID(): String

/**
 * Calcula el hash SHA-256 de un String.
 * Reemplaza java.security.MessageDigest
 */
expect fun sha256(input: String): String

/**
 * URL base del backend FocusLens según la plataforma.
 * - Android (emulador): 10.0.2.2 es el alias del `localhost` de la máquina anfitriona.
 * - iOS (simulador): localhost apunta directo a la máquina.
 *
 * Para probar en un dispositivo físico, reemplazar por la IP LAN del PC (p. ej. http://192.168.1.20:8080).
 */
expect fun serverBaseUrl(): String
