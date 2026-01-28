package com.example.calculator.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "calculator_prefs")

class LocalStorage(private val context: Context) {

    private val HISTORY_KEY = stringSetPreferencesKey("history")


    suspend fun getHistory(): List<String> {
        val prefs = context.dataStore.data.map { it[HISTORY_KEY] ?: emptySet() }.first()
        return prefs.toList()
    }


    suspend fun saveEntry(entry: String) {
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
