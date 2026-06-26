package com.focuslens.server.service

import com.focuslens.server.dto.UpdateGoalsRequest
import com.focuslens.server.dto.UserResponse
import com.focuslens.server.repository.UserRepository
import java.util.UUID

/**
 * SERVICIO de perfil de usuario.
 */
class UserService(
    private val userRepository: UserRepository
) {

    suspend fun getProfile(userId: String): UserResponse {
        val user = userRepository.findById(userId.toUUID()) ?: throw UserNotFoundException()
        return user.toResponse()
    }

    suspend fun updateGoals(userId: String, request: UpdateGoalsRequest): UserResponse {
        val id = userId.toUUID()
        userRepository.findById(id) ?: throw UserNotFoundException()
        userRepository.updateGoals(
            id = id,
            type = request.type,
            calories = request.dailyCaloriesTarget,
            protein = request.dailyProteinTarget,
            carbs = request.dailyCarbsTarget,
            fat = request.dailyFatTarget
        )
        return userRepository.findById(id)!!.toResponse()
    }

    private fun String.toUUID(): UUID =
        runCatching { UUID.fromString(this) }.getOrElse { throw UserNotFoundException() }
}
