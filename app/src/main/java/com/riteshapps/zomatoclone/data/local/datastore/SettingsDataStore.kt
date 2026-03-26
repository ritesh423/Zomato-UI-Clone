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

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.settingsDataStore

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val ONBOARDING_SHOWN_KEY = booleanPreferencesKey("onboarding_shown")
        val NOTIFICATION_ENABLED_KEY = booleanPreferencesKey("notification_enabled")
        val VEG_MODE_KEY = booleanPreferencesKey("veg_mode")
    }

    val darkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }

    val onboardingShown: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_SHOWN_KEY] ?: false
    }

    val notificationEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATION_ENABLED_KEY] ?: true
    }

    val vegMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[VEG_MODE_KEY] ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun setOnboardingShown(shown: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_SHOWN_KEY] = shown
        }
    }

    suspend fun setNotificationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED_KEY] = enabled
        }
    }

    suspend fun setVegMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[VEG_MODE_KEY] = enabled
        }
    }
}
