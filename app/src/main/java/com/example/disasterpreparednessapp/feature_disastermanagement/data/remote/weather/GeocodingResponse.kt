package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather

data class GeoLocationDto(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)
