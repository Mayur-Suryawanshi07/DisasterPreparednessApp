package com.example.disasterpreparednessapp.feature_disastermanagement.data.repository

import com.example.disasterpreparednessapp.feature_disastermanagement.data.local.NotifiedAlertDao
import com.example.disasterpreparednessapp.feature_disastermanagement.data.local.NotifiedAlertEntity
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterNotificationRepository
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class DisasterNotificationRepositoryImpl @Inject constructor(
    private val disasterRepository: DisasterRepository,
    private val notifiedAlertDao: NotifiedAlertDao
) : DisasterNotificationRepository {

    override suspend fun isFirstRun(): Boolean {
        return notifiedAlertDao.getNotifiedAlertsCount() == 0
    }

    override suspend fun checkNewUnnotifiedAlerts(): List<DisasterAlert> {
        val fetchedList = runCatching {
            disasterRepository.getDisasterEvents().firstOrNull()
        }.getOrNull().orEmpty()

        if (fetchedList.isEmpty()) return emptyList()

        val isFirst = isFirstRun()
        if (isFirst) {
            // On first run: mark all existing alerts as notified WITHOUT showing them
            markAlertsAsNotified(fetchedList.map { it.id })
            return emptyList()
        }

        val notifiedIds = notifiedAlertDao.getAllNotifiedAlertIds().toSet()
        return fetchedList.filter { alert -> alert.id !in notifiedIds }
    }

    override suspend fun markAlertsAsNotified(alertIds: List<String>) {
        if (alertIds.isEmpty()) return
        val entities = alertIds.map { NotifiedAlertEntity(alertId = it) }
        notifiedAlertDao.insertNotifiedAlerts(entities)
    }
}
