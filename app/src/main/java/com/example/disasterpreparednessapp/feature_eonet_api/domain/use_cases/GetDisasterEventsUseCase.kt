package com.example.disasterpreparednessapp.feature_eonet_api.domain.use_cases

import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterEvent
import com.example.disasterpreparednessapp.feature_eonet_api.domain.repository.DisasterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisasterEventsUseCase @Inject constructor(
    private val repository: DisasterRepository
) {
    suspend operator fun invoke(): Flow<List<DisasterEvent>> {
        return repository.getDisasterEvents()
    }
}
