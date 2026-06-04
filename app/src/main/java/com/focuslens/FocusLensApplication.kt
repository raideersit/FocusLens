package com.focuslens

import android.app.Application
import com.focuslens.di.androidModule
import com.focuslens.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

/**
 * Application class de FocusLens.
 * Inicializa Koin con los módulos compartido + Android.
 * Reemplaza @HiltAndroidApp del proyecto original.
 */
class FocusLensApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            platformModule = androidModule
        ) {
            androidLogger()
            androidContext(this@FocusLensApplication)
        }
    }
}
