package com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert

interface DisasterNotificationRepository {
    suspend fun checkNewUnnotifiedAlerts(): List<DisasterAlert>
    suspend fun markAlertsAsNotified(alertIds: List<String>)
    suspend fun isFirstRun(): Boolean
}
