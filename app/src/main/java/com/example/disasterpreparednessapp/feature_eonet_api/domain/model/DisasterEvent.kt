package com.example.disasterpreparednessapp.feature_eonet_api.domain.model

data class DisasterEvent(
    val id: String,
    val title: String,
    val description: String?,
    val link: String,
    val categories: List<DisasterCategory>,
    val geometries: List<DisasterGeometry>
)

data class DisasterCategory(
    val id: String,
    val title: String
)

data class DisasterGeometry(
    val date: String,
    val coordinates: List<Double>
)
