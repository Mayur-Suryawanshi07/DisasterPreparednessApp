package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterRepository
import javax.inject.Inject

class GetDisasterById @Inject constructor(
    private val repository : DisasterRepository
) {
    suspend operator fun invoke(id: String): DisasterAlert? {
        return repository.getDisasterByID(id)
    }
}