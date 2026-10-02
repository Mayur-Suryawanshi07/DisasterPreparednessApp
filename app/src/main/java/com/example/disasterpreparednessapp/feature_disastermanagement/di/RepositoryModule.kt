package com.example.disasterpreparednessapp.feature_disastermanagement.di

import com.example.disasterpreparednessapp.feature_disastermanagement.data.repository.DisasterNotificationRepositoryImpl
import com.example.disasterpreparednessapp.feature_disastermanagement.data.repository.DisasterRepositoryImpl
import com.example.disasterpreparednessapp.feature_disastermanagement.data.repository.WeatherRepositoryImpl
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterNotificationRepository
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterRepository
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSachetRepository(
        sachetRepositoryImpl: DisasterRepositoryImpl
    ): DisasterRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindDisasterNotificationRepository(
        disasterNotificationRepositoryImpl: DisasterNotificationRepositoryImpl
    ): DisasterNotificationRepository
}
