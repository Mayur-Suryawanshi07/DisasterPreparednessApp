package com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model

import com.google.gson.annotations.SerializedName

data class DisasterEventDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("link")
    val link: String,
    @SerializedName("categories")
    val categories: List<DisasterCategoryDto>,
    @SerializedName("geometries")
    val geometries: List<DisasterGeometryDto>
)

data class DisasterCategoryDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)

data class DisasterGeometryDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("coordinates")
    val coordinates: List<Double>
)
