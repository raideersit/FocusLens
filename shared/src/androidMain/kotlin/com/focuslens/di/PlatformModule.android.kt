package com.focuslens.di

import com.focuslens.data.local.DatabaseDriverFactory
import com.focuslens.data.local.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Módulo Koin específico de Android.
 * Provee las implementaciones platform-specific: driver de SQLite y DataStore.
 */
val androidModule = module {
    single { DatabaseDriverFactory(get()) }
    single { DatabaseDriverFactory(get()).createDriver() }
    single { createDataStore(context = androidContext()) }
}
