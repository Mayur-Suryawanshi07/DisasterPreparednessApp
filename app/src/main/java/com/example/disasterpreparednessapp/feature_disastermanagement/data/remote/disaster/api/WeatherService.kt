package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.api

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather.GeoLocationDto
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    //http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key}

    @GET("data/2.5/weather")
    suspend fun getWeatherDetails(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("geo/1.0/direct")
    suspend fun getCoordinatesByCity(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<GeoLocationDto>
}