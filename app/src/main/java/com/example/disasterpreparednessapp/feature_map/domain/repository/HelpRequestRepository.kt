package com.example.disasterpreparednessapp.feature_map.domain.repository

import com.example.disasterpreparednessapp.feature_map.domain.model.HelpRequest
import kotlinx.coroutines.flow.Flow

interface HelpRequestRepository {
    fun observeActiveRequests(ttlMillis: Long = 12 * 60 * 60 * 1000L): Flow<List<HelpRequest>>
    fun observeUserActiveRequest(userId: String): Flow<HelpRequest?>
    suspend fun sendHelpRequest(request: HelpRequest): Result<Unit>
    suspend fun removeHelpRequest(userId: String): Result<Unit>
}
