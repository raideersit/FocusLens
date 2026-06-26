package com.focuslens.platform

import java.security.MessageDigest
import java.util.UUID

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual fun randomUUID(): String = UUID.randomUUID().toString()

actual fun sha256(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(input.toByteArray())
        .joinToString("") { "%02x".format(it) }
}

// Para CELULAR FÍSICO: IP de la PC en la red local (mismo Wi-Fi).
// (Si usas el EMULADOR, cambia a "http://10.0.2.2:8080")
// Si tu IP cambia (router/DHCP), actualiza este valor.
actual fun serverBaseUrl(): String = "http://192.168.1.13:8080"
