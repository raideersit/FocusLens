package com.focuslens.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.focuslens.db.FocusLensDb

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(FocusLensDb.Schema, "focuslens.db")
}
