package com.example.disasterpreparednessapp.feature_disastermanagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface DisasterAlertDao {

    @Query("SELECT * FROM disaster_alerts WHERE capEvent IS NULL AND detailUrl IS NOT NULL")
    suspend fun getAlertsMissingCap(): List<DisasterAlertEntity>

    @Query("SELECT * FROM disaster_alerts ORDER BY publishedAt DESC")
    fun observeAlerts(): Flow<List<DisasterAlertEntity>>

    @Query("SELECT * FROM disaster_alerts")
    suspend fun getAlerts(): List<DisasterAlertEntity>

    @Query("SELECT * FROM disaster_alerts WHERE id = :id LIMIT 1")
    suspend fun getAlertById(id: String): DisasterAlertEntity?

    @Query("SELECT eTag FROM feed_cache_metadata WHERE cacheKey = :cacheKey LIMIT 1")
    suspend fun getETag(cacheKey: String = FeedCacheMetadataEntity.ALERT_FEED_CACHE_KEY): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<DisasterAlertEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: FeedCacheMetadataEntity)

    @Query("DELETE FROM disaster_alerts")
    suspend fun clearAlerts()

    @Transaction
    suspend fun replaceAlerts(alerts: List<DisasterAlertEntity>, eTag: String?) {
        val existingCapById = getAlerts().associateBy({ it.id }) { entity ->
            CapSnapshot(
                capEvent = entity.capEvent,
                capSeverity = entity.capSeverity,
                capAffectedAreas = entity.capAffectedAreas,
                capExpires = entity.capExpires,
                capEffective = entity.capEffective
            )
        }
        val mergedAlerts = alerts.map { alert ->
            existingCapById[alert.id]?.let { snapshot ->
                alert.copy(
                    capEvent = snapshot.capEvent,
                    capSeverity = snapshot.capSeverity,
                    capAffectedAreas = snapshot.capAffectedAreas,
                    capExpires = snapshot.capExpires,
                    capEffective = snapshot.capEffective
                )
            } ?: alert
        }
        clearAlerts()
        insertAlerts(mergedAlerts)
        insertMetadata(FeedCacheMetadataEntity(eTag = eTag))
    }

    @Transaction
    suspend fun updateCapInfo(capInfo: CapInfo) {
        val existing = getAlertById(capInfo.alertId) ?: return
        insertAlerts(
            listOf(
                existing.copy(
                    capEvent = capInfo.event,
                    capSeverity = capInfo.severity,
                    capAffectedAreas = capInfo.affectedAreas.joinToString("|"),
                    capExpires = capInfo.expires,
                    capEffective = capInfo.effective
                )
            )
        )
    }
}

private data class CapSnapshot(
    val capEvent: String?,
    val capSeverity: String?,
    val capAffectedAreas: String?,
    val capExpires: String?,
    val capEffective: String?
)
