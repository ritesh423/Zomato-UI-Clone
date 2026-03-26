package com.riteshapps.zomatoclone.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.authDataStore

    companion object {
        val USER_ID_KEY = longPreferencesKey("user_id")
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val IS_ADMIN_KEY = booleanPreferencesKey("is_admin")
    }

    val userId: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    val userName: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY] ?: ""
    }

    val userEmail: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY] ?: ""
    }

    val isAdmin: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_ADMIN_KEY] ?: false
    }

    suspend fun saveUserSession(userId: Long, userName: String, email: String, isAdmin: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[IS_LOGGED_IN_KEY] = true
            preferences[USER_NAME_KEY] = userName
            preferences[USER_EMAIL_KEY] = email
            preferences[IS_ADMIN_KEY] = isAdmin
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun updateUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }
}
