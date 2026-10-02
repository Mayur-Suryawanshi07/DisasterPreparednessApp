package com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster

/**
 * List-screen representation of a single RSS <item>.
 * Deliberately thin — anything that requires the per-alert CAP document
 * (severity, headline, area, etc.) lives on [CapInfo] instead, fetched
 * lazily for the detail screen via [detailUrl].
 */

data class DisasterAlert(
    val id: String,           // from Item.guid — used as the key for getDisasterByID
    val title: String,        // from Item.title
    val category: String?,    // from Item.category, e.g. "Met"
    val detailUrl: String?,   // from Item.link — pass straight to getCapAlertDetail
    val publishedAt: String?, // from Item.pubDate, raw string; parse at the UI layer if needed
    val author: String?,      // from Item.author, e.g. "CWC"
    val description: String?,  // from Item.description — usually blank in this feed, but real when present
    val capInfo: CapInfo? = null
)