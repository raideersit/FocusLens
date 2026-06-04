package com.focuslens.di

import com.focuslens.data.local.DatabaseDriverFactory
import com.focuslens.data.local.createDataStore
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
