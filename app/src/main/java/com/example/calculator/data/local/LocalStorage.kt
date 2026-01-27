package com.example.calculator.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "calculator_prefs")

class LocalStorage(private val context: Context) {

    private val HISTORY_KEY = stringSetPreferencesKey("history")

    val history: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[HISTORY_KEY] ?: emptySet()
        }

    suspend fun addToHistory(entry: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[HISTORY_KEY] ?: emptySet()
            preferences[HISTORY_KEY] = current + entry
        }
    }

    suspend fun clearHistory() {
        context.dataStore.edit { preferences ->
            preferences[HISTORY_KEY] = emptySet()
        }
    }
}
