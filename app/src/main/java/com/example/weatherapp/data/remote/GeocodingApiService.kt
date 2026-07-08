package com.example.weatherapp.data.remote

import com.example.weatherapp.data.remote.dto.GeocodingResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Open-Meteo Geocoding API (free, no API key required).
 * Converts a human-readable city name into latitude/longitude coordinates.
 */
interface GeocodingApiService {
    @GET("v1/search")
    suspend fun searchLocation(
        @Query("name") cityName: String,
        @Query("count") count: Int = 1,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json"
    ): GeocodingResponseDto
}
