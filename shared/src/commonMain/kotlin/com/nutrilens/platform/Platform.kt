package com.nutrilens.platform

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
