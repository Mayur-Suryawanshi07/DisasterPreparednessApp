package com.example.disasterpreparednessapp.feature_disastermanagement.di

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.api.SachetApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

//https://sachet.ndma.gov.in/cap_public_website/rss/rss_india.xml

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSachetRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://sachet.ndma.gov.in/")
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
            .build()
    }

    @Provides
    @Singleton
    fun provideSachetApiService(retrofit: Retrofit): SachetApiService {
        return retrofit.create(SachetApiService::class.java)
    }

}
