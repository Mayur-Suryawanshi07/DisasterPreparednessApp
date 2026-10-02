package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster

import javax.inject.Inject

data class AllUseCases @Inject constructor(
    val getDisaster: GetDisaster,
    val getEventByID: GetDisasterById,
    val searchDisaster: SearchDisaster,
    val getDisasterByCategory: GetDisasterByCategory,
    val getDisasterDetails: GetDisasterDetails
)
