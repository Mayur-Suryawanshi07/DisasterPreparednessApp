package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterRepository
import javax.inject.Inject

class RefreshDisasters @Inject constructor(
    private val repository: DisasterRepository
) {
    suspend operator fun invoke() {
        repository.refreshDisasterEvents()
    }
}
