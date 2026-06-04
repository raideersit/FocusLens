package com.focuslens.domain.exceptions

/**
 * Excepciones del dominio — desacopladas de frameworks de infraestructura.
 * El repositorio convierte excepciones de infraestructura en estas.
 */

/** El código de barras no existe en Open Food Facts */
class ProductNotFoundException(barcode: String) :
    Exception("Producto no encontrado para el código: $barcode")

/** Error de red (sin conexión, timeout, etc.) */
class NetworkException(message: String) : Exception(message)

/** Credenciales de login incorrectas */
class InvalidCredentialsException : Exception("Email o contraseña incorrectos")

/** El email ya está registrado */
class EmailAlreadyExistsException : Exception("El email ya está registrado")
