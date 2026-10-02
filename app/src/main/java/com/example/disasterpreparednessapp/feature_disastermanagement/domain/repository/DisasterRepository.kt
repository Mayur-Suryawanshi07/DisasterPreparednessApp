package com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import kotlinx.coroutines.flow.Flow

interface DisasterRepository {

    fun getDisasterEvents(): Flow<List<DisasterAlert>>

    suspend fun getDisasterByID(id: String): DisasterAlert?

    fun searchDisasterEvents(query: String): Flow<List<DisasterAlert>>

    fun getDisasterEventsByCategory(categoryId: String): Flow<List<DisasterAlert>>

    // FIXED: was getDisasterDetails() with no parameter and no way to
    // pick which alert. A detail screen needs one alert's CAP info,
    // so this now takes the alertId (== DisasterAlert.id / Item.guid).
    fun getDisasterDetail(alertId: String): Flow<List<CapInfo>>

    suspend fun saveCapInfo(capInfo: CapInfo)

    suspend fun refreshDisasterEvents()

    suspend fun enrichAlertCapDetails()
}
