package com.focuslens.domain.usecase.user

import com.focuslens.domain.model.User
import com.focuslens.domain.model.UserGoal
import com.focuslens.domain.repository.AuthRepository
import com.focuslens.domain.repository.UserProfileRepository

/** Inicia sesión verificando credenciales en la base de datos local. */
class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email y contraseña requeridos"))
        }
        return repository.login(email.trim(), password)
    }
}

class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Result<User> {
        if (name.isBlank() || email.isBlank() || password.length < 6) {
            return Result.failure(
                IllegalArgumentException("Nombre, email válido y contraseña de mínimo 6 caracteres requeridos")
            )
        }
        return repository.register(name.trim(), email.trim(), password)
    }
}

class GetCurrentUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): User? = repository.getCurrentUser()
}

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.logout()
}

class UpdateUserGoalsUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(userId: String, goals: UserGoal): Result<Unit> =
        repository.updateUserGoals(userId, goals)
}
