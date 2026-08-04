package com.example.disasterpreparednessapp.feature_disastermanagement.data.repository

import com.example.disasterpreparednessapp.feature_disastermanagement.data.mapper.DisasterAlertMapper
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.api.SachetApiService
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.SachetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SachetRepositoryImpl @Inject constructor(
    private val apiService: SachetApiService
) : SachetRepository {

    private var cachedAlerts: List<DisasterAlert>? = null

    override fun getDisasterEvents(): Flow<List<DisasterAlert>> = flow {
        try {
            // Try to get from cache first
            cachedAlerts?.let {
                emit(it)
            }

            // Fetch fresh data
            val alerts = getDisasterEventsFromNetwork()
            emit(alerts)

        } catch (e: HttpException) {
            // Handle HTTP errors
            emit(cachedAlerts ?: emptyList())
        } catch (e: IOException) {
            // Handle network errors
            emit(cachedAlerts ?: emptyList())
        } catch (e: Exception) {
            // Handle any other errors
            emit(cachedAlerts ?: emptyList())
        }
    }

    override suspend fun getDisasterByID(id: String): DisasterAlert? {
        val alert = cachedAlerts?.find { it.id == id }
            ?: getDisasterEventsFromNetwork().find { it.id == id }
            ?: return null
        return try {
            DisasterAlertMapper.enrichWithCap(alert, apiService.getCapAlert(alert.link))
        } catch (_: Exception) {
            alert
        }
    }

    override fun searchDisasterEvents(query: String): Flow<List<DisasterAlert>> = flow {
        try {
            val allAlerts = getDisasterEventsFromNetwork()
            val filteredAlerts = if (query.isBlank()) {
                allAlerts
            } else {
                allAlerts.filter { alert ->
                    alert.title.contains(query, ignoreCase = true) ||
                    alert.description.contains(query, ignoreCase = true) ||
                    alert.category?.contains(query, ignoreCase = true) == true
                }
            }
            emit(filteredAlerts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getDisasterEventsByCategory(categoryId: String): Flow<List<DisasterAlert>> = flow {
        try {
            val allAlerts = getDisasterEventsFromNetwork()
            val filteredAlerts = allAlerts.filter { alert ->
                alert.category?.equals(categoryId, ignoreCase = true) == true
            }
            emit(filteredAlerts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    private suspend fun getDisasterEventsFromNetwork(): List<DisasterAlert> {
        return try {
            val response = apiService.getRssFeed()
            if (response.isSuccessful) {
                response.body()?.let { rssFeed ->
                    DisasterAlertMapper.mapToDomainList(rssFeed.channel?.items).also {
                        cachedAlerts = it
                    }
                } ?: emptyList()
            } else {
                // Fallback to direct parsing if response parsing fails
                try {
                    val directResponse = apiService.getRssFeedDirect()
                    DisasterAlertMapper.mapToDomainList(directResponse.channel?.items).also {
                        cachedAlerts = it
                    }
                } catch (fallbackException: Exception) {
                    cachedAlerts ?: emptyList()
                }
            }
        } catch (e: Exception) {
            // Fallback to direct parsing
            try {
                val directResponse = apiService.getRssFeedDirect()
                DisasterAlertMapper.mapToDomainList(directResponse.channel?.items).also {
                    cachedAlerts = it
                }
            } catch (fallbackException: Exception) {
                cachedAlerts ?: emptyList()
            }
        }
    }
}

