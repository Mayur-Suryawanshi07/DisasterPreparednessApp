package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util

import com.example.disasterpreparednessapp.R
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val rssDateParser = DateTimeFormatter.RFC_1123_DATE_TIME
private val alertListDateFormat = DateTimeFormatter.ofPattern("d MMM, HH:mm", Locale.ENGLISH)

fun formatAlertListDate(raw: String?): String {
    if (raw.isNullOrBlank()) return "--"
    return runCatching {
        val instant = runCatching {
            OffsetDateTime.parse(raw).toInstant()
        }.getOrElse {
            ZonedDateTime.parse(raw, rssDateParser).toInstant()
        }
        instant.atZone(ZoneId.systemDefault()).format(alertListDateFormat)
    }.getOrElse { raw }
}

fun formatIntensityLabel(severity: String?): String {
    return when (severity?.trim()?.lowercase(Locale.ENGLISH)) {
        "minor" -> "Low Intensity"
        "moderate" -> "Low Intensity"
        "severe" -> "Medium Intensity"
        "extreme" -> "Extreme Intensity"
        null, "" -> "Unknown"
        else -> "${severity.replaceFirstChar { it.titlecase(Locale.ENGLISH) }} Intensity"
    }
}

fun alertDisplayName(title: String, capEvent: String?): String {
    return capEvent?.takeIf { it.isNotBlank() } ?: deriveShortTitle(title)
}

// Best-effort mapping of IMD Regional/Meteorological Centre cities to their primary state.
// IMD centres sometimes serve more than one state — extend as you see mismatches.
private val AGENCY_CITY_TO_STATE = mapOf(
    "dehradun" to "Uttarakhand",
    "lucknow" to "Uttar Pradesh",
    "kanpur" to "Uttar Pradesh",
    "kolkata" to "West Bengal",
    "guwahati" to "Assam",
    "bhopal" to "Madhya Pradesh",
    "bhubaneswar" to "Odisha",
    "chennai" to "Tamil Nadu",
    "hyderabad" to "Telangana",
    "bengaluru" to "Karnataka",
    "thiruvananthapuram" to "Kerala",
    "mumbai" to "Maharashtra",
    "nagpur" to "Maharashtra",
    "pune" to "Maharashtra",
    "ahmedabad" to "Gujarat",
    "jaipur" to "Rajasthan",
    "patna" to "Bihar",
    "ranchi" to "Jharkhand",
    "raipur" to "Chhattisgarh",
    "chandigarh" to "Punjab",
    "shimla" to "Himachal Pradesh",
    "srinagar" to "Jammu and Kashmir",
    "jammu" to "Jammu and Kashmir",
    "agartala" to "Tripura",
    "imphal" to "Manipur",
    "aizawl" to "Mizoram",
    "kohima" to "Nagaland",
    "shillong" to "Meghalaya",
    "itanagar" to "Arunachal Pradesh",
    "gangtok" to "Sikkim",
    "panaji" to "Goa",
    "port blair" to "Andaman and Nicobar Islands",
    "delhi" to "Delhi",
    "new delhi" to "Delhi"
)

private val INDIAN_STATES_AND_UTS = listOf(
    "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
    "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
    "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
    "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
    "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
    "Andaman and Nicobar Islands", "Chandigarh",
    "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Jammu and Kashmir",
    "Ladakh", "Lakshadweep", "Puducherry"
)

private fun containsWord(text: String, word: String): Boolean =
    Regex("\\b${Regex.escape(word)}\\b", RegexOption.IGNORE_CASE).containsMatchIn(text)

/** Priority 1: pull a state name straight out of the affected-area description. */
private fun stateFromAffectedAreas(affectedAreas: List<String>?): String? {
    if (affectedAreas.isNullOrEmpty()) return null
    val combined = affectedAreas.joinToString(" ")
    return INDIAN_STATES_AND_UTS.firstOrNull { state -> containsWord(combined, state) }
}

/** Priority 2: map a known regional-centre city inside the sender name to its state. */
private fun stateFromAgencyCity(agency: String): String? =
    AGENCY_CITY_TO_STATE.entries.firstOrNull { (city, _) -> containsWord(agency, city) }?.value

fun formatIssuedBy(author: String?, affectedAreas: List<String>? = null): String {
    stateFromAffectedAreas(affectedAreas)?.let { state -> return "$state Government" }

    if (author.isNullOrBlank()) return "Unknown source"

    val agency = Regex("\\(([^)]+)\\)").find(author)?.groupValues?.getOrNull(1)?.trim()
        ?: author.trim()

    stateFromAgencyCity(agency)?.let { state -> return "$state Government" }

    return agency
}


fun deriveShortTitle(rssTitle: String): String {
    val title = rssTitle.trim()
    if (title.isEmpty()) return "Weather Alert"

    if (title.count { it.code > 127 } > title.length / 3) {
        return "Weather Alert"
    }

    val normalized = title.replace(Regex("^\\d{4}/\\d{2}/\\d{2}\\s+\\d{2}:\\d{2}\\s+"), "")

    val stopPatterns = listOf(
        " is likely",
        " is very likely",
        " are very likely",
        " are likely",
        " accompanied",
        " with ",
        " very likely",
        " in next",
        " in the next",
        " over ",
        " at isolated",
        " at one or two"
    )
    for (pattern in stopPatterns) {
        val index = normalized.indexOf(pattern, ignoreCase = true)
        if (index > 0) {
            return normalized.substring(0, index).trim().trimEnd(',')
        }
    }

    return normalized
        .substringBefore('.')
        .take(48)
        .trim()
        .ifBlank { "Weather Alert" }
}

fun alertWeatherImage(eventName: String): Int {

    val text = eventName.lowercase(Locale.ENGLISH)

    return when {
        listOf("thunder", "lightning", "storm").any(text::contains) -> {
            R.drawable.ic_thunder_storm
        }

        listOf("rain", "shower", "drizzle").any(text::contains) -> {
            R.drawable.ic_rain
        }

        listOf("heat", "hot", "sun", "dry").any(text::contains) -> {
            R.drawable.ic_sunny
        }

        else -> {
            R.drawable.ic_flood // default image
        }
    }
}

fun intensityMeter(eventName: String) : Int{
    val text = eventName.lowercase(Locale.ENGLISH)

    return when {
        text == "Severe" ->{
            R.drawable.ic_thunder_storm
        }
        text == "Moderate" ->{

        }

        else -> {

        }
    } as Int

}
