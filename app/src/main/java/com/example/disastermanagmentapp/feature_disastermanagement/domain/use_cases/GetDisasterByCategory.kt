package com.example.disastermanagmentapp.feature_disastermanagement.domain.use_cases

import com.example.disastermanagmentapp.feature_disastermanagement.domain.model.DisasterAlert
import com.example.disastermanagmentapp.feature_disastermanagement.domain.repository.SachetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisasterByCategory @Inject constructor(
    private val repository: SachetRepository
) {
    operator fun invoke(categoryId: String): Flow<List<DisasterAlert>> {
        return repository.getDisasterEventsByCategory(categoryId)
    }
}