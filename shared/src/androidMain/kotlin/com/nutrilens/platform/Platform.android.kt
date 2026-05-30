package com.nutrilens.platform

import java.security.MessageDigest
import java.util.UUID

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual fun randomUUID(): String = UUID.randomUUID().toString()

actual fun sha256(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(input.toByteArray())
        .joinToString("") { "%02x".format(it) }
}
