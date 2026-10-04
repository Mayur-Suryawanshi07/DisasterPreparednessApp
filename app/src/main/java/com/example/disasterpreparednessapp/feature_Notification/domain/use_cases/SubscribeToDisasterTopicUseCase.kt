package com.example.disasterpreparednessapp.feature_notification.domain.use_cases

import com.example.disasterpreparednessapp.feature_notification.domain.repository.NotificationRepository
import javax.inject.Inject

class SubscribeToDisasterTopicUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(topic: String = "disaster_alerts"): Result<Unit> {
        return repository.subscribeToTopic(topic)
    }
}
