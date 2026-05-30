package com.nutrilens.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.nutrilens.db.NutriLensDb

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(NutriLensDb.Schema, "nutrilens.db")
}
