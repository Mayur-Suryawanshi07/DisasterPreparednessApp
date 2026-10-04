package com.example.disasterpreparednessapp.feature_notification.domain.model

data class DisasterNotification(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val alertId: String = "",
    val severity: String? = null,
    val area: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
