package com.example.weatherapp.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.PreferencesManager
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.util.WeatherCodeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        // Restore the last successful search so returning users see something
        // useful immediately instead of a blank screen.
        viewModelScope.launch {
            val lastCity = preferencesManager.lastSearchedCity.first()
            if (!lastCity.isNullOrBlank()) {
                _searchQuery.value = lastCity
                fetchWeather(lastCity)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSearchTriggered() {
        val city = _searchQuery.value.trim()
        if (city.isNotEmpty()) {
            fetchWeather(city)
        }
    }

    private fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            repository.getWeatherForCity(cityName)
                .onSuccess { weather ->
                    val appearance = WeatherCodeMapper.map(weather.weatherCode, weather.isDay)
                    _uiState.value = WeatherUiState.Success(
                        cityName = weather.cityName,
                        country = weather.country,
                        temperature = weather.temperature.roundToInt(),
                        feelsLike = weather.feelsLike.roundToInt(),
                        humidity = weather.humidity,
                        windSpeed = weather.windSpeed,
                        description = appearance.description,
                        emoji = appearance.emoji,
                        isDay = weather.isDay
                    )
                    preferencesManager.saveLastSearchedCity(weather.cityName)
                }
                .onFailure { error ->
                    _uiState.value = WeatherUiState.Error(
                        error.message ?: "Something went wrong. Please try again."
                    )
                }
        }
    }
}
