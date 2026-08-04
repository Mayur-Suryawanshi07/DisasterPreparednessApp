package com.example.disasterpreparednessapp.feature_disastermanagement.di

import com.example.disasterpreparednessapp.feature_disastermanagement.data.repository.SachetRepositoryImpl
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.SachetRepository
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
        sachetRepositoryImpl: SachetRepositoryImpl
    ): SachetRepository
}
