package com.example.weatherapp.domain.model

data class LocationInfo(
    val name: String,
    val country: String?,
    val adminArea: String?,
    val latitude: Double,
    val longitude: Double
)
