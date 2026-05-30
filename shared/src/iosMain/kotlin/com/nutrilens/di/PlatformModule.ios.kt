package com.nutrilens.di

import com.nutrilens.data.local.DatabaseDriverFactory
import com.nutrilens.data.local.createDataStore
import org.koin.dsl.module

/**
 * Módulo Koin específico de iOS.
 * Provee las implementaciones platform-specific: driver de SQLite y DataStore.
 */
val iosModule = module {
    single { DatabaseDriverFactory() }
    single { DatabaseDriverFactory().createDriver() }
    single { createDataStore() }
}
