package com.example.disasterpreparednessapp.feature_disastermanagement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_cache_metadata")
data class FeedCacheMetadataEntity(
    @PrimaryKey val cacheKey: String = ALERT_FEED_CACHE_KEY,
    val eTag: String?
) {
    companion object {
        const val ALERT_FEED_CACHE_KEY = "alert_feed"
    }
}
