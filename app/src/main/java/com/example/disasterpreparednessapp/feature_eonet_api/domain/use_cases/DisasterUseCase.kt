package com.example.disasterpreparednessapp.feature_eonet_api.domain.use_cases

data class DisasterUseCase (
    val getEventByID : GetDisasterEventByIdUseCase,
    val getDisasterUseCase : GetDisasterEventsUseCase,
    val searchDisaster : SearchDisasterEventsUseCase
)