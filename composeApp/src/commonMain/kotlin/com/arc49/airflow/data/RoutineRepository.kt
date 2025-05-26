package com.arc49.airflow.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arc49.airflow.presentations.tabs.Exercise
import com.arc49.airflow.presentations.tabs.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoutineRepository(private val dataStore: DataStore<Preferences>) {
    private val ROUTINES_KEY = stringPreferencesKey("routines")

    val routines: Flow<List<Routine>> = dataStore.data.map { preferences ->
        val routinesJson = preferences[ROUTINES_KEY] ?: "[]"
        try {
            Json.decodeFromString<List<Routine>>(routinesJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveRoutines(routines: List<Routine>) {
        dataStore.edit { preferences ->
            preferences[ROUTINES_KEY] = Json.encodeToString(routines)
        }
    }

    suspend fun addRoutine(routine: Routine) {
        dataStore.edit { preferences ->
            val currentRoutines = preferences[ROUTINES_KEY]?.let {
                Json.decodeFromString<List<Routine>>(it)
            } ?: emptyList()
            preferences[ROUTINES_KEY] = Json.encodeToString(currentRoutines + routine)
        }
    }

    suspend fun updateRoutine(routine: Routine) {
        dataStore.edit { preferences ->
            val currentRoutines = preferences[ROUTINES_KEY]?.let {
                Json.decodeFromString<List<Routine>>(it)
            } ?: emptyList()
            val updatedRoutines = currentRoutines.map {
                if (it.id == routine.id) routine else it
            }
            preferences[ROUTINES_KEY] = Json.encodeToString(updatedRoutines)
        }
    }

    suspend fun deleteRoutine(routineId: String) {
        dataStore.edit { preferences ->
            val currentRoutines = preferences[ROUTINES_KEY]?.let {
                Json.decodeFromString<List<Routine>>(it)
            } ?: emptyList()
            val updatedRoutines = currentRoutines.filter { it.id != routineId }
            preferences[ROUTINES_KEY] = Json.encodeToString(updatedRoutines)
        }
    }
} 