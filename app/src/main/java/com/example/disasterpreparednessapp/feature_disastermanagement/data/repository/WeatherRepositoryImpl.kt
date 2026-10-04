package com.example.disasterpreparednessapp.feature_disastermanagement.data.repository

import com.example.disasterpreparednessapp.BuildConfig
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.api.WeatherService
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather.WeatherResponse
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRepository {

    private val apiKey: String
        get() = BuildConfig.WEATHER_API_KEY.ifBlank { BuildConfig.MY_API_KEY }

    override suspend fun getWeatherByLatLng(
        latitude: Double,
        longitude: Double
    ): WeatherResponse {
        return weatherService.getWeatherDetails(
            latitude = latitude,
            longitude = longitude,
            apiKey = apiKey
        )
    }

    override suspend fun getWeatherByCity(city: String): WeatherResponse {
        val locations = weatherService.getCoordinatesByCity(
            cityName = city,
            limit = 1,
            apiKey = apiKey
        )

        if (locations.isEmpty()) {
            throw Exception("City '$city' not found")
        }

        val location = locations.first()

        return weatherService.getWeatherDetails(
            latitude = location.lat,
            longitude = location.lon,
            apiKey = apiKey
        )
    }
}
