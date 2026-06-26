package com.focuslens.server

import com.focuslens.server.repository.FoodScanRepository
import com.focuslens.server.repository.UserRepository
import com.focuslens.server.service.AuthService
import com.focuslens.server.service.FoodScanService
import com.focuslens.server.service.UserService

/**
 * Contenedor simple de dependencias (composición manual).
 *
 * Conecta la cadena Repositorio → Servicio → Ruta sin necesidad de un framework
 * de inyección de dependencias en el servidor.
 */
class Dependencies {
    // Repositorios (acceso a datos)
    private val userRepository = UserRepository()
    private val scanRepository = FoodScanRepository()

    // Servicios (lógica de negocio)
    val authService = AuthService(userRepository)
    val userService = UserService(userRepository)
    val foodScanService = FoodScanService(scanRepository)
}
