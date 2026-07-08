package com.example.weatherapp.presentation.weather

sealed interface WeatherUiState {
    data object Idle : WeatherUiState
    data object Loading : WeatherUiState

    data class Success(
        val cityName: String,
        val country: String?,
        val temperature: Int,
        val feelsLike: Int,
        val humidity: Int,
        val windSpeed: Double,
        val description: String,
        val emoji: String,
        val isDay: Boolean
    ) : WeatherUiState

    data class Error(val message: String) : WeatherUiState
}
