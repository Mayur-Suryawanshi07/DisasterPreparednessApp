package com.example.disasterpreparednessapp.feature_notification.data.repository

import com.example.disasterpreparednessapp.feature_notification.data.local.NotificationSettingsDataStore
import com.example.disasterpreparednessapp.feature_notification.domain.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val notificationSettingsDataStore: NotificationSettingsDataStore
) : NotificationRepository {

    override suspend fun registerFcmToken(token: String): Result<Unit> = runCatching {
        // Log / handle token registration
        Result.success(Unit)
    }

    override suspend fun subscribeToTopic(topic: String): Result<Unit> = runCatching {
        firebaseMessaging.subscribeToTopic(topic).await()
    }

    override suspend fun unsubscribeFromTopic(topic: String): Result<Unit> = runCatching {
        firebaseMessaging.unsubscribeFromTopic(topic).await()
    }

    override fun observeNotificationEnabled(): Flow<Boolean> {
        return notificationSettingsDataStore.isNotificationsEnabledFlow
    }

    override suspend fun setNotificationEnabled(enabled: Boolean) {
        notificationSettingsDataStore.setNotificationsEnabled(enabled)
    }
}
