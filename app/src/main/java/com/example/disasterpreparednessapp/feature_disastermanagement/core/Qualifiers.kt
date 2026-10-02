package com.example.disasterpreparednessapp.feature_disastermanagement.core
import javax.inject.Qualifier

// core/di/Qualifiers.kt

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DisasterRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherRetrofit