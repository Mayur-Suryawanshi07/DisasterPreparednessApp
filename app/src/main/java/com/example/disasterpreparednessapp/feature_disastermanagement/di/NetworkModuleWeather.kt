package com.example.disasterpreparednessapp.feature_disastermanagement.di

import com.example.disasterpreparednessapp.feature_disastermanagement.core.WeatherRetrofit
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.api.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModuleWeather {

    @WeatherRetrofit
    @Provides
    @Singleton
    fun provideWeatherRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherServices(@WeatherRetrofit retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }


}