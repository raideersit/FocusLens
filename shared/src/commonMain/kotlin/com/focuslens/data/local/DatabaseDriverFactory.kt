package com.focuslens.data.local

import app.cash.sqldelight.db.SqlDriver

/**
 * Factory para crear el driver de SQLDelight según la plataforma.
 * Android usa AndroidSqliteDriver, iOS usa NativeSqliteDriver.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
