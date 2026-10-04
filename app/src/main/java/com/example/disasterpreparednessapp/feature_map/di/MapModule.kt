package com.example.disasterpreparednessapp.feature_map.di

import com.example.disasterpreparednessapp.feature_map.data.GeoCodingApiService
import com.example.disasterpreparednessapp.feature_map.data.repository.HelpRequestRepositoryImpl
import com.example.disasterpreparednessapp.feature_map.domain.repository.HelpRequestRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MapRetrofit

@Module
@InstallIn(SingletonComponent::class)
object MapModule {

    @MapRetrofit
    @Provides
    @Singleton
    fun provideMapRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGeoCodingApiService(@MapRetrofit retrofit: Retrofit): GeoCodingApiService {
        return retrofit.create(GeoCodingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideHelpRequestRepository(
        impl: HelpRequestRepositoryImpl
    ): HelpRequestRepository = impl
}
