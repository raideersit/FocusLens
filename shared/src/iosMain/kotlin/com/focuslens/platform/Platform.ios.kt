package com.focuslens.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDate
import platform.Foundation.NSUUID
import platform.Foundation.timeIntervalSince1970
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH

actual fun currentTimeMillis(): Long =
    (NSDate().timeIntervalSince1970 * 1000).toLong()

actual fun randomUUID(): String =
    NSUUID().UUIDString()

@OptIn(ExperimentalForeignApi::class)
actual fun sha256(input: String): String {
    val data = input.encodeToByteArray()
    val digest = UByteArray(CC_SHA256_DIGEST_LENGTH)
    data.usePinned { pinned ->
        digest.usePinned { digestPinned ->
            CC_SHA256(pinned.addressOf(0), data.size.toUInt(), digestPinned.addressOf(0))
        }
    }
    return digest.joinToString("") { it.toString(16).padStart(2, '0') }
}

// En el simulador iOS, localhost apunta a la máquina anfitriona.
actual fun serverBaseUrl(): String = "http://localhost:8080"
