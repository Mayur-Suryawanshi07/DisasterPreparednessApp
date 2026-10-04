package com.example.disasterpreparednessapp.feature_notification.domain.use_cases

import com.example.disasterpreparednessapp.feature_notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNotificationEnabledUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.observeNotificationEnabled()
    }
}
