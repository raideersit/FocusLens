package com.nutrilens.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    require(context is Context) { "Android requires a Context to create DataStore" }
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath.toPath()
        }
    )
}
