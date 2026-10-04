package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.weather

import javax.inject.Inject

data class WeatherUseCases @Inject constructor(
    val getWeatherByCity: GetWeatherByCity,
    val getWeatherByLatLng: GetWeatherByLatLng
)
