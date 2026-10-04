package com.example.disasterpreparednessapp.feature_disastermanagement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notified_alerts")
data class NotifiedAlertEntity(
    @PrimaryKey val alertId: String,
    val notifiedAt: Long = System.currentTimeMillis()
)
