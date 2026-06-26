package com.focuslens.server.service

/**
 * Excepciones de la capa de negocio. El plugin StatusPages las traduce a códigos
 * HTTP apropiados (400, 401, 404, 409).
 */
sealed class ServiceException(message: String) : RuntimeException(message)

class EmailAlreadyExistsException : ServiceException("El correo ya está registrado")
class InvalidCredentialsException : ServiceException("Correo o contraseña incorrectos")
class UserNotFoundException : ServiceException("Usuario no encontrado")
class ScanNotFoundException : ServiceException("Escaneo no encontrado")
class ValidationException(message: String) : ServiceException(message)
