package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.api

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.dto.CapAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.dto.RssFeed
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface SachetApiService {
    ////https://sachet.ndma.gov.in/cap_public_website/rss/rss_india.xml
    @GET("cap_public_website/rss/rss_india.xml")
    suspend fun getRssFeed(): Response<RssFeed>

    @GET("cap_public_website/rss/rss_india.xml")
    suspend fun getRssFeedDirect(): RssFeed

    @GET
    suspend fun getCapAlert(
        @Url url: String
    ): CapAlert
}