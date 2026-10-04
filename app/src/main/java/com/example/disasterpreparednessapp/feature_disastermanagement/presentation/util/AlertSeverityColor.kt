package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util

import androidx.compose.ui.graphics.Color
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AlertCardOrange
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AlertCardYellow
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AlertErrorRed

/** Shared alert-card palette used by the alert list and alert detail views. */
fun alertSeverityBackground(
    title: String,
    category: String?,
    severity: String?,
    urgency: String?
): Color {
    val alertText = "$title ${category.orEmpty()}".lowercase()

    return when {
        severity.equals("extreme", ignoreCase = true) ||
            urgency.equals("immediate", ignoreCase = true) ||
            listOf("cyclone", "fire", "earthquake").any(alertText::contains) -> AlertErrorRed

        severity.equals("severe", ignoreCase = true) -> AlertCardOrange
        else -> AlertCardYellow
    }
}
