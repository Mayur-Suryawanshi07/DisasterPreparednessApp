package com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert
import kotlinx.coroutines.flow.Flow

interface SachetRepository {
    fun getDisasterEvents(): Flow<List<DisasterAlert>>
    suspend fun getDisasterByID(id: String): DisasterAlert?
    fun searchDisasterEvents(query: String): Flow<List<DisasterAlert>>
    fun getDisasterEventsByCategory(categoryId: String): Flow<List<DisasterAlert>>
}