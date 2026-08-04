package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.SachetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisaster @Inject constructor(
    private val repository: SachetRepository
) {
    operator fun invoke(): Flow<List<DisasterAlert>> {
        return repository.getDisasterEvents()
    }

}