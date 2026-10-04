package com.example.disasterpreparednessapp.feature_notification.domain.use_cases

import com.example.disasterpreparednessapp.feature_notification.domain.repository.NotificationRepository
import javax.inject.Inject

class ToggleNotificationEnabledUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.setNotificationEnabled(enabled)
        if (enabled) {
            repository.subscribeToTopic("disaster_alerts")
        } else {
            repository.unsubscribeFromTopic("disaster_alerts")
        }
    }
}
