package com.nutrilens.data.remote.api

import com.nutrilens.data.remote.dto.ProductResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Cliente HTTP para la API de Open Food Facts usando Ktor (KMP).
 * Reemplaza la interfaz Retrofit del proyecto Android original.
 *
 * Documentación: https://world.openfoodfacts.org/api/v2
 */
class OpenFoodFactsApi(private val client: HttpClient) {

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/"
    }

    /**
     * Obtiene los datos de un producto por su código de barras EAN/UPC.
     *
     * @param barcode Código de barras del producto.
     * @param fields  Campos a solicitar (reduce el tamaño de la respuesta).
     */
    suspend fun getProduct(
        barcode: String,
        fields: String = "code,product_name,brands,image_url,quantity,nutriscore_grade,nutriments"
    ): ProductResponseDto {
        return client.get("${BASE_URL}api/v2/product/$barcode") {
            parameter("fields", fields)
        }.body()
    }
}
