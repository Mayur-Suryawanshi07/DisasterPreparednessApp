package com.example.disasterpreparednessapp.feature_eonet_api.domain.use_cases

import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterEvent
import com.example.disasterpreparednessapp.feature_eonet_api.domain.repository.DisasterRepository
import javax.inject.Inject

class GetDisasterEventByIdUseCase @Inject constructor(
    private val repository: DisasterRepository
) {
    suspend operator fun invoke(id: String): DisasterEvent? {
        // Business logic: validate ID
        if (id.isBlank()) {
            throw IllegalArgumentException("Event ID cannot be empty")
        }
        
        return repository.getDisasterEventById(id)
    }
}
