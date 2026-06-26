package com.focuslens.server.security

import org.mindrot.jbcrypt.BCrypt

/**
 * SEGURIDAD · Password Hashing.
 *
 * Usa BCrypt (con salt automático) para almacenar y verificar contraseñas.
 * BCrypt es preferible a SHA-256 porque incluye salt y es deliberadamente lento,
 * lo que dificulta los ataques de fuerza bruta.
 */
object PasswordHasher {

    /** Genera el hash de una contraseña en texto plano. */
    fun hash(plainPassword: String): String =
        BCrypt.hashpw(plainPassword, BCrypt.gensalt(12))

    /** Verifica que una contraseña en texto plano coincida con el hash almacenado. */
    fun verify(plainPassword: String, hashedPassword: String): Boolean =
        runCatching { BCrypt.checkpw(plainPassword, hashedPassword) }.getOrDefault(false)
}
