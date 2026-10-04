package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util

import androidx.annotation.DrawableRes
import com.example.disasterpreparednessapp.R

/**
 * Single Kotlin mapper: DisasterType -> @DrawableRes Int
 * Returns a large drawable icon matching the disaster type with `feature_notification` as fallback.
 */
@DrawableRes
fun getDisasterLargeIconRes(disasterTypeOrTitle: String?): Int {
    if (disasterTypeOrTitle.isNullOrBlank()) return R.drawable.feature_notification
    val text = disasterTypeOrTitle.lowercase()
    return when {
        text.contains("flood") -> R.drawable.ic_flood
        text.contains("rain") -> R.drawable.ic_rain
        text.contains("fire") -> R.drawable.ic_sunny
        text.contains("thunder") || text.contains("storm") || text.contains("cyclone") -> R.drawable.ic_thunder_storm
        text.contains("cloud") || text.contains("weather") -> R.drawable.ic_cloud
        text.contains("sun") || text.contains("heat") -> R.drawable.ic_sunny
        else -> R.drawable.feature_notification
    }
}
