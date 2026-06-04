package com.focuslens.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Factory para crear DataStore según la plataforma.
 */
expect fun createDataStore(context: Any? = null): DataStore<Preferences>

internal const val DATA_STORE_FILE_NAME = "focuslens_session.preferences_pb"
