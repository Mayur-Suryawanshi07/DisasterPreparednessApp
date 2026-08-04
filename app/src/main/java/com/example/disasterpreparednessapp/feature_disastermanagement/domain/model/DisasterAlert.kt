package com.example.disasterpreparednessapp.feature_disastermanagement.domain.model

data class DisasterAlert(
    val id: String,
    val title: String,
    val link: String,
    val description: String,
    val author: String,
    val pubDate: String,
    val category: String? = null,
    val event: String? = null,
    val urgency: String? = null,
    val severity: String? = null,
    val certainty: String? = null,
    val effective: String? = null,
    val onset: String? = null,
    val expires: String? = null,
    val instruction: String? = null,
    val affectedAreas: List<String> = emptyList(),
    val sender: String? = null,
    val status: String? = null,
    val polygonUrl: String? = null
)