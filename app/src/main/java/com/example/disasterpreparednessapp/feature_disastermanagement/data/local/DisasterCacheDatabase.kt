package com.example.disasterpreparednessapp.feature_disastermanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DisasterAlertEntity::class, FeedCacheMetadataEntity::class, NotifiedAlertEntity::class],
    version = 3,
    exportSchema = false
)
abstract class DisasterCacheDatabase : RoomDatabase() {
    abstract fun disasterAlertDao(): DisasterAlertDao
    abstract fun notifiedAlertDao(): NotifiedAlertDao
}
