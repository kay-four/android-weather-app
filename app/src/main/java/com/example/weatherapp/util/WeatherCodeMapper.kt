package com.example.weatherapp.util

/**
 * Maps WMO weather codes (as returned by the Open-Meteo API) to a short
 * human-readable description and an emoji icon, avoiding the need to bundle
 * custom weather icon assets.
 */
object WeatherCodeMapper {

    data class WeatherAppearance(val description: String, val emoji: String)

    fun map(weatherCode: Int, isDay: Boolean): WeatherAppearance = when (weatherCode) {
        0 -> if (isDay) WeatherAppearance("Clear sky", "☀️") else WeatherAppearance("Clear sky", "🌙")
        1, 2 -> if (isDay) WeatherAppearance("Partly cloudy", "⛅") else WeatherAppearance("Partly cloudy", "☁️")
        3 -> WeatherAppearance("Overcast", "☁️")
        45, 48 -> WeatherAppearance("Fog", "🌫️")
        51, 53, 55 -> WeatherAppearance("Drizzle", "🌦️")
        56, 57 -> WeatherAppearance("Freezing drizzle", "🌧️")
        61, 63, 65 -> WeatherAppearance("Rain", "🌧️")
        66, 67 -> WeatherAppearance("Freezing rain", "🌧️")
        71, 73, 75 -> WeatherAppearance("Snow fall", "❄️")
        77 -> WeatherAppearance("Snow grains", "🌨️")
        80, 81, 82 -> WeatherAppearance("Rain showers", "🌧️")
        85, 86 -> WeatherAppearance("Snow showers", "🌨️")
        95 -> WeatherAppearance("Thunderstorm", "⛈️")
        96, 99 -> WeatherAppearance("Thunderstorm with hail", "⛈️")
        else -> WeatherAppearance("Unknown", "🌡️")
    }
}
