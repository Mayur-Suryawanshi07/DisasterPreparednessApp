package com.example.disasterpreparednessapp.feature_disastermanagement.di

import android.content.Context
import androidx.room.Room
import com.example.disasterpreparednessapp.feature_disastermanagement.core.DisasterRetrofit
import com.example.disasterpreparednessapp.feature_disastermanagement.data.local.DisasterAlertDao
import com.example.disasterpreparednessapp.feature_disastermanagement.data.local.DisasterCacheDatabase
import com.example.disasterpreparednessapp.feature_disastermanagement.data.local.NotifiedAlertDao
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.DisasterApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModuleDisaster {

    @Provides
    @Singleton
    fun provideDisasterCacheDatabase(
        @ApplicationContext context: Context
    ): DisasterCacheDatabase = Room.databaseBuilder(
        context,
        DisasterCacheDatabase::class.java,
        "disaster_cache.db"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideDisasterAlertDao(database: DisasterCacheDatabase): DisasterAlertDao =
        database.disasterAlertDao()

    @Provides
    fun provideNotifiedAlertDao(database: DisasterCacheDatabase): NotifiedAlertDao =
        database.notifiedAlertDao()

    @DisasterRetrofit
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
    fun provideSachetApiService(@DisasterRetrofit retrofit: Retrofit): DisasterApiService {
        return retrofit.create(DisasterApiService::class.java)
    }
}
