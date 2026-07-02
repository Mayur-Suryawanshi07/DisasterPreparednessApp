package com.example.disastermanagmentapp.feature_disastermanagement.domain.use_cases

import com.example.disastermanagmentapp.feature_disastermanagement.domain.model.DisasterAlert
import com.example.disastermanagmentapp.feature_disastermanagement.domain.repository.SachetRepository
import javax.inject.Inject

class GetDisasterById @Inject constructor(
    private val repository : SachetRepository
) {
    suspend operator fun invoke(id: String): DisasterAlert? {
        return repository.getDisasterByID(id)
    }
}