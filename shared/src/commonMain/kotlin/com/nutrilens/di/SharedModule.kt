package com.nutrilens.di

import com.nutrilens.data.local.SessionManager
import com.nutrilens.data.remote.api.OpenFoodFactsApi
import com.nutrilens.data.remote.mapper.FoodDtoMapper
import com.nutrilens.data.repository.FoodRepositoryImpl
import com.nutrilens.data.repository.ScanHistoryRepositoryImpl
import com.nutrilens.data.repository.UserRepositoryImpl
import com.nutrilens.db.NutriLensDb
import com.nutrilens.domain.repository.AuthRepository
import com.nutrilens.domain.repository.FoodRepository
import com.nutrilens.domain.repository.ScanHistoryRepository
import com.nutrilens.domain.repository.UserProfileRepository
import com.nutrilens.domain.usecase.food.GetFoodByBarcodeUseCase
import com.nutrilens.domain.usecase.history.AddScanToHistoryUseCase
import com.nutrilens.domain.usecase.history.DeleteScanUseCase
import com.nutrilens.domain.usecase.history.GetScanHistoryUseCase
import com.nutrilens.domain.usecase.history.UpdateScanNotesUseCase
import com.nutrilens.domain.usecase.nutrition.AnalyzeNutritionUseCase
import com.nutrilens.domain.usecase.user.GetCurrentUserUseCase
import com.nutrilens.domain.usecase.user.LoginUseCase
import com.nutrilens.domain.usecase.user.LogoutUseCase
import com.nutrilens.domain.usecase.user.RegisterUseCase
import com.nutrilens.domain.usecase.user.UpdateUserGoalsUseCase
import com.nutrilens.presentation.feature.auth.LoginViewModel
import com.nutrilens.presentation.feature.auth.RegisterViewModel
import com.nutrilens.presentation.feature.fooddetail.FoodDetailViewModel
import com.nutrilens.presentation.feature.history.HistoryViewModel
import com.nutrilens.presentation.feature.profile.ProfileViewModel
import com.nutrilens.presentation.feature.scanner.ScannerViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Módulo Koin compartido — reemplaza los módulos Hilt de Android.
 * Centraliza la creación de HttpClient, base de datos, repositorios,
 * use cases y ViewModels.
 */
val sharedModule = module {

    // ── HTTP Client (Ktor) ──────────────────────────────────────────────────
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = false
                })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
        }
    }

    // ── API ─────────────────────────────────────────────────────────────────
    single { OpenFoodFactsApi(get()) }
    single { FoodDtoMapper() }

    // ── Base de datos ───────────────────────────────────────────────────────
    single { NutriLensDb(get()) }

    // ── Sesión ──────────────────────────────────────────────────────────────
    singleOf(::SessionManager)

    // ── Repositorios ────────────────────────────────────────────────────────
    single<FoodRepository> { FoodRepositoryImpl(get(), get()) }
    single<ScanHistoryRepository> { ScanHistoryRepositoryImpl(get()) }
    single { UserRepositoryImpl(get(), get()) }
    single<AuthRepository> { get<UserRepositoryImpl>() }
    single<UserProfileRepository> { get<UserRepositoryImpl>() }

    // ── Use Cases ───────────────────────────────────────────────────────────
    factoryOf(::GetFoodByBarcodeUseCase)
    factoryOf(::AddScanToHistoryUseCase)
    factoryOf(::GetScanHistoryUseCase)
    factoryOf(::DeleteScanUseCase)
    factoryOf(::UpdateScanNotesUseCase)
    factoryOf(::AnalyzeNutritionUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::UpdateUserGoalsUseCase)

    // ── ViewModels ──────────────────────────────────────────────────────────
    viewModelOf(::ScannerViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::FoodDetailViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::ProfileViewModel)
}
