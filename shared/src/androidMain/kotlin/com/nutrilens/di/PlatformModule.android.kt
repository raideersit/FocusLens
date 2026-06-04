package com.nutrilens.di

import com.nutrilens.data.local.DatabaseDriverFactory
import com.nutrilens.data.local.createDataStore
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
