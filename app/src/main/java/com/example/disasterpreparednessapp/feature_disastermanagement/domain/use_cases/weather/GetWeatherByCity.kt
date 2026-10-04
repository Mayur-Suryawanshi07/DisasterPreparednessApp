package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.weather

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather.WeatherResponse
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherByCity @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): WeatherResponse {
        return repository.getWeatherByCity(city)
    }
}
