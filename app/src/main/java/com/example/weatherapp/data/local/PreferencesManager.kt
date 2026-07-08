package com.example.weatherapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "weather_prefs")

/**
 * Persists the last successfully searched city so the app can restore it
 * on the next launch, giving the user an "offline-friendly" first impression.
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val lastCityKey = stringPreferencesKey("last_searched_city")

    val lastSearchedCity: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[lastCityKey]
    }

    suspend fun saveLastSearchedCity(cityName: String) {
        context.dataStore.edit { prefs ->
            prefs[lastCityKey] = cityName
        }
    }
}
