package com.example.disasterpreparednessapp.feature_notification.domain.use_cases

import com.example.disasterpreparednessapp.feature_notification.domain.repository.NotificationRepository
import javax.inject.Inject

class RegisterFcmTokenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> {
        return repository.registerFcmToken(token)
    }
}
