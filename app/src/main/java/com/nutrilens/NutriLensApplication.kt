package com.nutrilens

import android.app.Application
import com.nutrilens.di.androidModule
import com.nutrilens.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

/**
 * Application class de NutriLens.
 * Inicializa Koin con los módulos compartido + Android.
 * Reemplaza @HiltAndroidApp del proyecto original.
 */
class NutriLensApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            platformModule = androidModule
        ) {
            androidLogger()
            androidContext(this@NutriLensApplication)
        }
    }
}
