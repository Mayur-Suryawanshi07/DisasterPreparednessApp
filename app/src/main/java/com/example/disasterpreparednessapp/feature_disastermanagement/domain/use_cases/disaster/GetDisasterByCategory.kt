package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisasterByCategory @Inject constructor(
    private val repository: DisasterRepository
) {
    operator fun invoke(categoryId: String): Flow<List<DisasterAlert>> {
        return repository.getDisasterEventsByCategory(categoryId)
    }
}