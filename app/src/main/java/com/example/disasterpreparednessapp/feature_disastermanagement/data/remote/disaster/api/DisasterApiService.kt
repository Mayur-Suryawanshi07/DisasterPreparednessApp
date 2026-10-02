package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.cap.CapAlertDto
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.rss.RssFeedDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface DisasterApiService {

    // https://sachet.ndma.gov.in/cap_public_website/rss/rss_india.xml
    // Powers the alert list screen. Wrapped in Response<> so the
    // repository can inspect HTTP errors (e.g. 5xx) instead of the
    // call throwing directly.
    @GET("cap_public_website/rss/rss_india.xml")
    suspend fun getAlertFeed(@Header("If-None-Match") eTag: String? = null): Response<RssFeedDto>

    // https://sachet.ndma.gov.in/cap_public_website/FetchXMLFile?identifier=...
    // Powers the detail screen. Called with the full Item.link URL
    // straight from an RssFeed item — no need to build the URL yourself.
    @GET
    suspend fun getCapAlertDetail(@Url url: String): Response<CapAlertDto>
}
