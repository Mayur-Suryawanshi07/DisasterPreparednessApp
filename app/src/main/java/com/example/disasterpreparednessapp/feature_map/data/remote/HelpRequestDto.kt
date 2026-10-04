package com.example.disasterpreparednessapp.feature_map.data.remote

import com.example.disasterpreparednessapp.feature_map.domain.model.HelpRequest

data class HelpRequestDto(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val message: String = "",
    val timestamp: Long = 0L,
    val status: String = "active"
)

fun HelpRequestDto.toDomain(): HelpRequest = HelpRequest(
    id = id,
    userId = userId,
    userName = userName,
    latitude = latitude,
    longitude = longitude,
    message = message,
    timestamp = timestamp,
    status = status
)

fun HelpRequest.toDto(): HelpRequestDto = HelpRequestDto(
    id = id,
    userId = userId,
    userName = userName,
    latitude = latitude,
    longitude = longitude,
    message = message,
    timestamp = timestamp,
    status = status
)
