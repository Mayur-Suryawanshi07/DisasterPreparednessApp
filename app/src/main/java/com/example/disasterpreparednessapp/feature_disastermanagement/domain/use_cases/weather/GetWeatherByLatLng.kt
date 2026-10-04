package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.weather

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather.WeatherResponse
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherByLatLng @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): WeatherResponse {
        return repository.getWeatherByLatLng(latitude, longitude)
    }
}
