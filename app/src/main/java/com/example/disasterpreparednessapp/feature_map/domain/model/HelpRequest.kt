package com.example.disasterpreparednessapp.feature_map.domain.model

data class HelpRequest(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val message: String = "Need emergency assistance!",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "active"
)
