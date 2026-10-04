package com.example.disasterpreparednessapp.feature_notification.di

import android.content.Context
import com.example.disasterpreparednessapp.feature_notification.data.local.NotificationSettingsDataStore
import com.example.disasterpreparednessapp.feature_notification.data.repository.NotificationRepositoryImpl
import com.example.disasterpreparednessapp.feature_notification.domain.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

        @Provides
        @Singleton
        fun provideNotificationSettingsDataStore(
            @ApplicationContext context: Context
        ): NotificationSettingsDataStore = NotificationSettingsDataStore(context)
    }
}
