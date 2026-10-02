package com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.weather

data class WeatherResponse(
    val base: String = "",
    val clouds: Clouds = Clouds(),
    val cod: Int = 0,
    val coord: Coord = Coord(),
    val dt: Long = 0L,
    val id: Int = 0,
    val main: Main = Main(),
    val name: String = "",
    val rain: Rain? = null,
    val sys: Sys? = null,
    val timezone: Int = 0,
    val visibility: Int = 0,
    val weather: List<Weather> = emptyList(),
    val wind: Wind = Wind()
)

data class Main(
    val feels_like: Double = 0.0,
    val grnd_level: Int = 0,
    val humidity: Int = 0,
    val pressure: Int = 0,
    val sea_level: Int = 0,
    val temp: Double = 0.0,
    val temp_max: Double = 0.0,
    val temp_min: Double = 0.0
)

data class Sys(
    val country: String = "",
    val id: Int = 0,
    val sunrise: Long = 0L,
    val sunset: Long = 0L,
    val type: Int = 0
)

data class Rain(
    val `1h`: Double? = null
)

data class Wind(
    val deg: Int = 0,
    val gust: Double? = null,
    val speed: Double = 0.0
)

data class Clouds(
    val all: Int = 0
)

data class Coord(
    val lat: Double = 0.0,
    val lon: Double = 0.0
)
