package com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster

data class CapAlert(
    val identifier: String,
    val sender: String,
    val sent: String,
    val status: String,
    val infos: List<CapInfo> = emptyList()
)