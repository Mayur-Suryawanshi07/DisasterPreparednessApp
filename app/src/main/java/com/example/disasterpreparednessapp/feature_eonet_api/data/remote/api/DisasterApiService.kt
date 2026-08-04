package com.example.disasterpreparednessapp.feature_eonet_api.data.remote.api

import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model.DisasterEventDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DisasterApiService {
    
    @GET("events")
    suspend fun getDisasterEvents(
        @Query("limit") limit: Int = 50,
        @Query("days") days: Int = 30
    ): EonetResponse
    
    @GET("events")
    suspend fun searchDisasterEvents(
        @Query("query") query: String,
        @Query("limit") limit: Int = 50
    ): EonetResponse
    
    @GET("events")
    suspend fun getDisasterEventById(
        @Query("id") id: String
    ): EonetResponse
    
    @GET("events")
    suspend fun getDisasterEventsByCategory(
        @Query("category") categoryId: String,
        @Query("limit") limit: Int = 50
    ): EonetResponse
}

data class EonetResponse(
    val events: List<DisasterEventDto>
)
