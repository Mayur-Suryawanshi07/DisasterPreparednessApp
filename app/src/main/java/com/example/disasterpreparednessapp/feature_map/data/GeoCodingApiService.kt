package com.example.disasterpreparednessapp.feature_map.data

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApiService {

    @GET("maps/api/geocode/json")
    suspend fun getAddressFromCoordinates(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): GeocodingResponse

    @GET("maps/api/geocode/json")
    suspend fun getCoordinatesFromAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodingResponse
}
