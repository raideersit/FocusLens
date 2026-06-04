package com.focuslens.di

import com.focuslens.data.local.SessionManager
import com.focuslens.data.remote.api.OpenFoodFactsApi
import com.focuslens.data.remote.mapper.FoodDtoMapper
import com.focuslens.data.repository.FoodRepositoryImpl
import com.focuslens.data.repository.ScanHistoryRepositoryImpl
import com.focuslens.data.repository.UserRepositoryImpl
import com.focuslens.db.FocusLensDb
import com.focuslens.domain.repository.AuthRepository
import com.focuslens.domain.repository.FoodRepository
import com.focuslens.domain.repository.ScanHistoryRepository
import com.focuslens.domain.repository.UserProfileRepository
import com.focuslens.domain.usecase.food.GetFoodByBarcodeUseCase
import com.focuslens.domain.usecase.history.AddScanToHistoryUseCase
import com.focuslens.domain.usecase.history.DeleteScanUseCase
import com.focuslens.domain.usecase.history.GetScanHistoryUseCase
import com.focuslens.domain.usecase.history.UpdateScanNotesUseCase
import com.focuslens.domain.usecase.nutrition.AnalyzeNutritionUseCase
import com.focuslens.domain.usecase.user.GetCurrentUserUseCase
import com.focuslens.domain.usecase.user.LoginUseCase
import com.focuslens.domain.usecase.user.LogoutUseCase
import com.focuslens.domain.usecase.user.RegisterUseCase
import com.focuslens.domain.usecase.user.UpdateUserGoalsUseCase
import com.focuslens.presentation.feature.auth.LoginViewModel
import com.focuslens.presentation.feature.auth.RegisterViewModel
import com.focuslens.presentation.feature.fooddetail.FoodDetailViewModel
import com.focuslens.presentation.feature.history.HistoryViewModel
import com.focuslens.presentation.feature.profile.ProfileViewModel
import com.focuslens.presentation.feature.scanner.ScannerViewModel
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
    single { FocusLensDb(get()) }

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
