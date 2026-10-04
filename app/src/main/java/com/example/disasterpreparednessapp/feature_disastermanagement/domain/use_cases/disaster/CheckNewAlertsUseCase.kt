package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterNotificationRepository
import javax.inject.Inject

class CheckNewAlertsUseCase @Inject constructor(
    private val notificationRepository: DisasterNotificationRepository
) {
    suspend operator fun invoke(): List<DisasterAlert> {
        val newAlerts = notificationRepository.checkNewUnnotifiedAlerts()
        if (newAlerts.isNotEmpty()) {
            notificationRepository.markAlertsAsNotified(newAlerts.map { it.id })
        }
        return newAlerts
    }
}
