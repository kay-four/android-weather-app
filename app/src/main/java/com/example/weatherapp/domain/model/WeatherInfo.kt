package com.example.weatherapp.domain.model

data class WeatherInfo(
    val cityName: String,
    val country: String?,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val weatherCode: Int,
    val isDay: Boolean
)
