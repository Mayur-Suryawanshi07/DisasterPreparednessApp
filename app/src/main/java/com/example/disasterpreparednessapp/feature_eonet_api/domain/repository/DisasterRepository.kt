package com.example.disasterpreparednessapp.feature_eonet_api.domain.repository

import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterEvent
import kotlinx.coroutines.flow.Flow

interface DisasterRepository {
    suspend fun getDisasterEvents(): Flow<List<DisasterEvent>>
    suspend fun getDisasterEventById(id: String): DisasterEvent?
    suspend fun searchDisasterEvents(query: String): Flow<List<DisasterEvent>>
    suspend fun getDisasterEventsByCategory(categoryId: String): Flow<List<DisasterEvent>>
}
