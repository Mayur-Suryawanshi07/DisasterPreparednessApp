package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisasterDetails @Inject constructor(
    private val repository: DisasterRepository
) {
    operator fun invoke(alertId: String): Flow<List<CapInfo>> {
        return repository.getDisasterDetail(alertId)
    }
}
