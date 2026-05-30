plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    // ── Android target ──────────────────────────────────────────────────────
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    // ── iOS targets ─────────────────────────────────────────────────────────
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        // ── Código compartido (Android + iOS) ───────────────────────────────
        commonMain.dependencies {
            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)

            // Navigation (Compose Multiplatform)
            implementation(libs.androidx.navigation.compose)

            // Lifecycle ViewModel (KMP)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Ktor (HTTP client)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)

            // Koin (DI)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // kotlinx
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            // DataStore
            implementation(libs.androidx.datastore.preferences.core)
        }

        // ── Android-specific ────────────────────────────────────────────────
        androidMain.dependencies {
            // Ktor Android engine
            implementation(libs.ktor.client.android)

            // SQLDelight Android driver
            implementation(libs.sqldelight.android.driver)

            // Koin Android
            implementation(libs.koin.android)

            // Coroutines Android
            implementation(libs.kotlinx.coroutines.android)

            // CameraX (barcode scanner)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.view)

            // ML Kit Barcode
            implementation(libs.mlkit.barcode.scanning)

            // Coil (image loading)
            implementation(libs.coil.compose)

            // DataStore Android
            implementation(libs.androidx.datastore.preferences)
        }

        // ── iOS-specific ────────────────────────────────────────────────────
        iosMain.dependencies {
            // Ktor Darwin engine
            implementation(libs.ktor.client.darwin)

            // SQLDelight Native driver
            implementation(libs.sqldelight.native.driver)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.nutrilens.shared"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// ── SQLDelight configuration ────────────────────────────────────────────────
sqldelight {
    databases {
        create("NutriLensDb") {
            packageName.set("com.nutrilens.db")
            srcDirs("src/commonMain/sqldelight")
        }
    }
}
