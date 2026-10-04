package com.example.disasterpreparednessapp.feature_disastermanagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotifiedAlertDao {
    @Query("SELECT alertId FROM notified_alerts")
    suspend fun getAllNotifiedAlertIds(): List<String>

    @Query("SELECT COUNT(*) FROM notified_alerts")
    suspend fun getNotifiedAlertsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifiedAlerts(alerts: List<NotifiedAlertEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifiedAlert(alert: NotifiedAlertEntity)
}
