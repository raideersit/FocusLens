package com.nutrilens.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Función de inicialización de Koin compartida.
 * Cada plataforma llama a esta función con su módulo específico.
 */
fun initKoin(
    platformModule: Module,
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        appDeclaration()
        modules(sharedModule, platformModule)
    }
}
