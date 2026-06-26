package com.focuslens.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Gestiona la sesión activa del usuario usando DataStore (KMP).
 * Persiste el userId entre reinicios de la app.
 * PRINCIPIO S (SRP): solo se encarga de la persistencia de sesión.
 */
class SessionManager(
    private val dataStore: DataStore<Preferences>
) {
    private val KEY_USER_ID    = stringPreferencesKey("active_user_id")
    private val KEY_USER_NAME  = stringPreferencesKey("active_user_name")
    private val KEY_USER_EMAIL = stringPreferencesKey("active_user_email")
    private val KEY_TOKEN       = stringPreferencesKey("auth_token")

    val activeUserId: Flow<String?> = dataStore.data.map { it[KEY_USER_ID] }

    suspend fun saveSession(userId: String, name: String, email: String) {
        dataStore.edit { prefs ->
            prefs[KEY_USER_ID]    = userId
            prefs[KEY_USER_NAME]  = name
            prefs[KEY_USER_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    /** Guarda el token JWT emitido por el backend tras login/registro. */
    suspend fun saveToken(token: String) {
        dataStore.edit { it[KEY_TOKEN] = token }
    }

    /** Devuelve el token JWT activo, o null si no hay sesión. */
    suspend fun getToken(): String? =
        dataStore.data.first()[KEY_TOKEN]

    suspend fun getActiveUserId(): String? =
        dataStore.data.first()[KEY_USER_ID]

    suspend fun getActiveUserName(): String? =
        dataStore.data.first()[KEY_USER_NAME]

    suspend fun getActiveUserEmail(): String? =
        dataStore.data.first()[KEY_USER_EMAIL]
}
