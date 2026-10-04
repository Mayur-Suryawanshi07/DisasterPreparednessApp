package com.example.disasterpreparednessapp.feature_disastermanagement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo

@Entity(tableName = "disaster_alerts")
data class DisasterAlertEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String?,
    val detailUrl: String?,
    val publishedAt: String?,
    val author: String?,
    val description: String?,
    val capEvent: String? = null,
    val capSeverity: String? = null,
    val capAffectedAreas: String? = null,
    val capExpires: String? = null,
    val capEffective: String? = null
)

fun DisasterAlert.toEntity() = DisasterAlertEntity(
    id = id,
    title = title,
    category = category,
    detailUrl = detailUrl,
    publishedAt = publishedAt,
    author = author,
    description = description,
    capEvent = capInfo?.event,
    capSeverity = capInfo?.severity,
    capAffectedAreas = capInfo?.affectedAreas?.joinToString("|"),
    capExpires = capInfo?.expires,
    capEffective = capInfo?.effective
)

fun DisasterAlertEntity.toDomain() = DisasterAlert(
    id = id,
    title = title,
    category = category,
    detailUrl = detailUrl,
    publishedAt = publishedAt,
    author = author,
    description = description,
    capInfo = if (
        capEvent != null ||
            capSeverity != null ||
            !capAffectedAreas.isNullOrBlank() ||
            capExpires != null ||
            capEffective != null
    ) {
        CapInfo(
            alertId = id,
            language = null,
            event = capEvent,
            severity = capSeverity,
            certainty = null,
            urgency = null,
            headline = null,
            description = description,
            instruction = null,
            affectedAreas = capAffectedAreas
                ?.split("|")
                ?.filter { it.isNotBlank() }
                .orEmpty(),
            effective = capEffective,
            expires = capExpires
        )
    } else {
        null
    }
)
