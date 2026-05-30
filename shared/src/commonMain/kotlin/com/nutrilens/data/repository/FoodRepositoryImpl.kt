package com.nutrilens.data.repository

import com.nutrilens.data.remote.api.OpenFoodFactsApi
import com.nutrilens.data.remote.mapper.FoodDtoMapper
import com.nutrilens.domain.exceptions.NetworkException
import com.nutrilens.domain.exceptions.ProductNotFoundException
import com.nutrilens.domain.model.Food
import com.nutrilens.domain.repository.FoodRepository
import io.ktor.client.plugins.ResponseException

/**
 * Implementación del repositorio de alimentos usando Ktor (KMP).
 * Convierte errores de red e HTTP en excepciones de dominio
 * para que las capas superiores no dependan del cliente HTTP.
 */
class FoodRepositoryImpl(
    private val api: OpenFoodFactsApi,
    private val mapper: FoodDtoMapper
) : FoodRepository {

    override suspend fun getFoodByBarcode(barcode: String): Result<Food> {
        return try {
            val response = api.getProduct(barcode)
            when {
                response.status == 1 ->
                    Result.success(mapper.toDomain(response.product))
                else ->
                    Result.failure(ProductNotFoundException(barcode))
            }
        } catch (e: ResponseException) {
            Result.failure(NetworkException("Error HTTP ${e.response.status.value}: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(NetworkException("Sin conexión a internet. Verifica tu red."))
        }
    }
}
