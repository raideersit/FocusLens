plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    application
}

group = "com.focuslens"
version = "1.0.0"

application {
    // Punto de entrada del servidor Ktor
    mainClass.set("com.focuslens.server.ApplicationKt")
}

// Asegura que `./gradlew :server:run` busque el archivo .env dentro de server/
tasks.named<JavaExec>("run") {
    workingDir = projectDir
}

dependencies {
    // ── Ktor server ───────────────────────────────────────────────────────────
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.default.headers)

    // ── Base de datos: Exposed + PostgreSQL (Neon) + pool de conexiones ────────
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.hikaricp)

    // ── Seguridad: hashing de contraseñas ─────────────────────────────────────
    implementation(libs.jbcrypt)

    // ── Configuración (.env) + logging ────────────────────────────────────────
    implementation(libs.dotenv.kotlin)
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.datetime)

    // ── Testing ───────────────────────────────────────────────────────────────
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.junit)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
