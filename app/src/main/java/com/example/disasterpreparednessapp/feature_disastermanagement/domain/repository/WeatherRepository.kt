package com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather.WeatherResponse

interface WeatherRepository {
    suspend fun getWeatherByLatLng(latitude: Double, longitude: Double): WeatherResponse
    suspend fun getWeatherByCity(city: String): WeatherResponse
}
