package com.focuslens.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Respuesta raíz de GET /api/v2/product/{barcode} */
@Serializable
data class ProductResponseDto(
    @SerialName("status")  val status: Int,
    @SerialName("code")    val code: String = "",
    @SerialName("product") val product: ProductDto = ProductDto()
)

/** Datos del producto */
@Serializable
data class ProductDto(
    @SerialName("code")             val code: String? = null,
    @SerialName("product_name")     val productName: String? = null,
    @SerialName("brands")           val brands: String? = null,
    @SerialName("image_url")        val imageUrl: String? = null,
    @SerialName("quantity")         val quantity: String? = null,
    @SerialName("nutriscore_grade") val nutriscoreGrade: String? = null,
    @SerialName("nutriments")       val nutriments: NutrimentsDto? = null
)

/** Tabla de valores nutricionales (por 100g) */
@Serializable
data class NutrimentsDto(
    @SerialName("energy-kcal_100g")   val energyKcal: Double? = null,
    @SerialName("proteins_100g")       val proteins: Double? = null,
    @SerialName("carbohydrates_100g")  val carbohydrates: Double? = null,
    @SerialName("fat_100g")            val fat: Double? = null,
    @SerialName("saturated-fat_100g")  val saturatedFat: Double? = null,
    @SerialName("sodium_100g")         val sodium: Double? = null,
    @SerialName("fiber_100g")          val fiber: Double? = null,
    @SerialName("sugars_100g")         val sugars: Double? = null
)
