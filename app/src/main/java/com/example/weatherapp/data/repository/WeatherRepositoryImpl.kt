package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.GeocodingApiService
import com.example.weatherapp.data.remote.WeatherApiService
import com.example.weatherapp.domain.model.WeatherInfo
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val geocodingApiService: GeocodingApiService,
    private val weatherApiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getWeatherForCity(cityName: String): Result<WeatherInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val geocodingResponse = geocodingApiService.searchLocation(cityName)
                val location = geocodingResponse.results?.firstOrNull()
                    ?: return@withContext Result.failure(
                        NoSuchElementException(
                            "No results found for \"$cityName\". Check the spelling and try again."
                        )
                    )

                val weatherResponse = weatherApiService.getCurrentWeather(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                val current = weatherResponse.current

                Result.success(
                    WeatherInfo(
                        cityName = location.name,
                        country = location.country,
                        temperature = current.temperature,
                        feelsLike = current.feelsLike,
                        humidity = current.humidity,
                        windSpeed = current.windSpeed,
                        weatherCode = current.weatherCode,
                        isDay = current.isDay == 1
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
