package com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster

import com.google.android.gms.maps.model.LatLng

data class CapInfo(
    val alertId: String,
    val language: String?,
    val event: String?,
    val severity: String?,
    val certainty: String?,
    val urgency: String?,
    val headline: String?,
    val description: String?,
    val instruction: String?,
    val affectedAreas: List<String>,
    val effective: String?,
    val expires: String?,
    val polygons: List<String> = emptyList(),
    val markerPosition: LatLng? = null
)
