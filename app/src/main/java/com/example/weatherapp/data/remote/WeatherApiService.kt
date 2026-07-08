package com.example.weatherapp.data.remote

import com.example.weatherapp.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Open-Meteo Forecast API (free, no API key required).
 * Returns current weather conditions for a given coordinate pair.
 */
interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String =
            "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,is_day",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponseDto
}
