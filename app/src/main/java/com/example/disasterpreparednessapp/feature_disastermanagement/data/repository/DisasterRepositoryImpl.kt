package com.example.disasterpreparednessapp.feature_disastermanagement.data.repository

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.DisasterApiService
import com.example.disasterpreparednessapp.feature_disastermanagement.data.local.DisasterAlertDao
import com.example.disasterpreparednessapp.feature_disastermanagement.data.local.toDomain
import com.example.disasterpreparednessapp.feature_disastermanagement.data.local.toEntity
import com.example.disasterpreparednessapp.feature_disastermanagement.data.mapper.toDisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.data.mapper.toDomainCapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import javax.inject.Inject

/**
 * Room is the single source of truth for the alert list.
 * A background refresh uses the stored ETag so unchanged feeds return 304
 * and skip re-writing the database.
 */
class

DisasterRepositoryImpl @Inject constructor(
    private val api: DisasterApiService,
    private val cache: DisasterAlertDao
) : DisasterRepository {

    private val capEnrichmentMutex = Mutex()

    override fun getDisasterEvents(): Flow<List<DisasterAlert>> = callbackFlow {
        val refreshJob = launch {
            try {
                refreshAlerts()
            } catch (e: IOException) {
                if (cache.getAlerts().isEmpty()) {
                    close(e)
                }
            }
            launch { enrichMissingCapDetails() }
        }

        val collectJob = launch {
            cache.observeAlerts()
                .map { alerts -> alerts.map { it.toDomain() } }
                .collect { trySend(it) }
        }

        awaitClose {
            refreshJob.cancel()
            collectJob.cancel()
        }
    }

    override suspend fun getDisasterByID(id: String): DisasterAlert? {
        return cache.getAlertById(id)?.toDomain()
            ?: try {
                refreshAlerts()?.firstOrNull { it.id == id }
            } catch (_: IOException) {
                null
            }
    }

    override fun searchDisasterEvents(query: String): Flow<List<DisasterAlert>> =
        getDisasterEvents().map { alerts ->
            alerts.filter { alert ->
                query.isBlank() || alertSearchText(alert).contains(query, ignoreCase = true)
            }
        }

    override fun getDisasterEventsByCategory(categoryId: String): Flow<List<DisasterAlert>> =
        getDisasterEvents().map { alerts ->
            alerts.filter { alert ->
                alert.category.equals(categoryId, ignoreCase = true)
            }
        }

    override fun getDisasterDetail(alertId: String): Flow<List<CapInfo>> = flow {
        val alert = getDisasterByID(alertId)
            ?: throw NoSuchElementException("No alert found with id $alertId")
        val url = alert.detailUrl
            ?: throw IllegalStateException("Alert $alertId has no detail link")

        val response = api.getCapAlertDetail(url)
        if (!response.isSuccessful) {
            throw IOException("Failed to load alert detail: HTTP ${response.code()}")
        }
        val infos = response.body()?.infos.orEmpty().map { it.toDomainCapInfo(alertId) }
        pickPreferredInfo(infos)?.let { cache.updateCapInfo(it) }
        emit(infos)
    }

    override suspend fun saveCapInfo(capInfo: CapInfo) {
        cache.updateCapInfo(capInfo)
    }

    override suspend fun refreshDisasterEvents() {
        try {
            refreshAlerts()
        } catch (e: IOException) {
            if (cache.getAlerts().isEmpty()) throw e
        }
    }

    override suspend fun enrichAlertCapDetails() {
        enrichMissingCapDetails()
    }

    private suspend fun enrichMissingCapDetails() {
        capEnrichmentMutex.withLock {
            cache.getAlertsMissingCap().forEach { alert ->
                val url = alert.detailUrl ?: return@forEach
                runCatching { fetchAndStoreCap(alert.id, url) }
                delay(120)
            }
        }
    }

    private suspend fun fetchAndStoreCap(alertId: String, url: String) {
        val response = api.getCapAlertDetail(url)
        if (!response.isSuccessful) return
        val infos = response.body()?.infos.orEmpty().map { it.toDomainCapInfo(alertId) }
        pickPreferredInfo(infos)?.let { cache.updateCapInfo(it) }
    }

    private suspend fun refreshAlerts(): List<DisasterAlert>? {
        val response = api.getAlertFeed(cache.getETag())
        if (response.code() == 304) return null
        if (!response.isSuccessful) {
            throw IOException("Failed to load alert feed: HTTP ${response.code()}")
        }
        return response.body()?.channelDto?.itemDtos.orEmpty()
            .map { it.toDisasterAlert() }
            .also { alerts ->
                cache.replaceAlerts(alerts.map { it.toEntity() }, response.headers()["ETag"])
            }
    }

    private fun alertSearchText(alert: DisasterAlert): String {
        val cap = alert.capInfo
        return listOfNotNull(
            cap?.event,
            alert.title,
            alert.author,
            alert.category,
            cap?.affectedAreas?.joinToString(" ")
        ).joinToString(" ")
    }

    private fun pickPreferredInfo(infos: List<CapInfo>): CapInfo? =
        infos.firstOrNull { it.language?.startsWith("en", ignoreCase = true) == true }
            ?: infos.firstOrNull()
}
