package com.uvg.mypokedex.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extensión para crear el DataStore
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * Clase que maneja las preferencias del usuario usando DataStore
 */
class UserPreferences(private val context: Context) {

    companion object {
        // Keys para las preferencias
        private val SORT_TYPE_KEY = stringPreferencesKey("sort_type")
        private val IS_ASCENDING_KEY = booleanPreferencesKey("is_ascending")

        // Valores por defecto
        const val DEFAULT_SORT_TYPE = "NUMBER"
        const val DEFAULT_IS_ASCENDING = true
    }

    /**
     * Flow que emite el tipo de ordenamiento seleccionado
     */
    val sortType: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[SORT_TYPE_KEY] ?: DEFAULT_SORT_TYPE
    }

    /**
     * Flow que emite si el orden es ascendente
     */
    val isAscending: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_ASCENDING_KEY] ?: DEFAULT_IS_ASCENDING
    }

    /**
     * Guarda el tipo de ordenamiento
     */
    suspend fun saveSortType(sortType: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_TYPE_KEY] = sortType
        }
    }

    /**
     * Guarda si el orden es ascendente
     */
    suspend fun saveIsAscending(isAscending: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ASCENDING_KEY] = isAscending
        }
    }

    /**
     * Guarda ambas preferencias de ordenamiento
     */
    suspend fun saveSortPreferences(sortType: String, isAscending: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SORT_TYPE_KEY] = sortType
            preferences[IS_ASCENDING_KEY] = isAscending
        }
    }
}
