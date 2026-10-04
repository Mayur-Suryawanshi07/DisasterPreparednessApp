package com.example.disasterpreparednessapp.feature_notification.domain.repository

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun registerFcmToken(token: String): Result<Unit>
    suspend fun subscribeToTopic(topic: String = "disaster_alerts"): Result<Unit>
    suspend fun unsubscribeFromTopic(topic: String = "disaster_alerts"): Result<Unit>
    fun observeNotificationEnabled(): Flow<Boolean>
    suspend fun setNotificationEnabled(enabled: Boolean)
}
