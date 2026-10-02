package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.weather_screen


import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather.WeatherResponse

sealed class WeatherUiState{

    object Idle: WeatherUiState()

    data class Success(val weather: WeatherResponse): WeatherUiState()

    data class Error(val message: String): WeatherUiState()

    object Loading: WeatherUiState()

}
