package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.weather_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.weather.GetWeatherByCity
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.weather.GetWeatherByLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherByCity: GetWeatherByCity,
    private val getWeatherByLatLng: GetWeatherByLatLng
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun searchWeather(city: String) {
        if (city.isBlank()) return

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val response = getWeatherByCity(city.trim())
                _uiState.value = WeatherUiState.Success(
                    weather = response
                )
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    message = e.localizedMessage ?: "Something went wrong"
                )
            }
        }
    }

    fun searchWeatherByLatLong(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val response = getWeatherByLatLng(latitude, longitude)
                _uiState.value = WeatherUiState.Success(
                    weather = response
                )
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    message = e.localizedMessage ?: "Something went wrong"
                )
            }
        }
    }
}
