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

private val Context.searchDataStore: DataStore<Preferences> by preferencesDataStore(name = "search_prefs")

@Singleton
class SearchDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.searchDataStore

    companion object {
        val RECENT_SEARCHES_KEY = stringSetPreferencesKey("recent_searches")
        private const val MAX_RECENT_SEARCHES = 10
    }

    val recentSearches: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[RECENT_SEARCHES_KEY] ?: emptySet()
    }

    suspend fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        
        dataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCHES_KEY]?.toMutableSet() ?: mutableSetOf()
            // Remove if already exists to avoid duplicates
            currentSearches.remove(query)
            // Add to the beginning
            val newSearches = mutableSetOf(query)
            newSearches.addAll(currentSearches.take(MAX_RECENT_SEARCHES - 1))
            preferences[RECENT_SEARCHES_KEY] = newSearches
        }
    }

    suspend fun removeRecentSearch(query: String) {
        dataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCHES_KEY]?.toMutableSet() ?: mutableSetOf()
            currentSearches.remove(query)
            preferences[RECENT_SEARCHES_KEY] = currentSearches
        }
    }

    suspend fun clearRecentSearches() {
        dataStore.edit { preferences ->
            preferences.remove(RECENT_SEARCHES_KEY)
        }
    }
}
