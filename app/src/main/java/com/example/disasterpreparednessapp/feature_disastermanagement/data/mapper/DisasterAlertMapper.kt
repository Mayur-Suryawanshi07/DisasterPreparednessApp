package com.example.disasterpreparednessapp.feature_disastermanagement.data.mapper

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.cap.toLatLng
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.cap.CapInfoDto as CapInfoDto
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.rss.ItemDto
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert

fun ItemDto.toDisasterAlert(): DisasterAlert = DisasterAlert(
    id = guid.orEmpty(),
    title = title.orEmpty(),
    category = category,
    detailUrl = link,
    publishedAt = pubDate,
    author = author,
    description = description
)

fun CapInfoDto.toDomainCapInfo(alertId: String): CapInfo = CapInfo(
    alertId = alertId,
    language = language,
    event = event,
    severity = severity,
    certainty = certainty,
    urgency = urgency,
    headline = headline,
    description = description,
    instruction = instruction,
    affectedAreas = areas.orEmpty().mapNotNull { it.areaDesc },
    effective = effective,
    expires = expires,
    polygons = areas.orEmpty().flatMap { it.polygons.orEmpty() },
    markerPosition = areas.orEmpty().firstNotNullOfOrNull { it.toLatLng() }
)
