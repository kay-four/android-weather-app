package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.model.WeatherInfo

interface WeatherRepository {
    /**
     * Resolves [cityName] to a location via geocoding, then fetches the current
     * weather for that location. Returns a [Result] so the caller can cleanly
     * handle both the "city not found" case and network/parsing failures.
     */
    suspend fun getWeatherForCity(cityName: String): Result<WeatherInfo>
}
