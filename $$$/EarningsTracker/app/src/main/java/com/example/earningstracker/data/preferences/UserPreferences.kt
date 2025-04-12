package com.example.earningstracker.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "earnings_preferences")

class UserPreferences(private val context: Context) {
    
    private val hourlyWageKey = doublePreferencesKey("hourly_wage")
    
    // Get the saved hourly wage
    val hourlyWage: Flow<Double> = context.dataStore.data
        .map { preferences ->
            preferences[hourlyWageKey] ?: 0.0
        }
    
    // Save the hourly wage
    suspend fun saveHourlyWage(wage: Double) {
        context.dataStore.edit { preferences ->
            preferences[hourlyWageKey] = wage
        }
    }
} 