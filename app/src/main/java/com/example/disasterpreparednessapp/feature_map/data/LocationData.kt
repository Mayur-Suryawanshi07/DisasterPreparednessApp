package com.example.disasterpreparednessapp.feature_map.data

data class LocationData(
    val latitude: Double,
    val longitude: Double
)

data class GeocodingResponse(
    val results: List<GeoCodingResult>,
    val status: String
)

data class GeoCodingResult(
    val formatted_address: String = "",
    val geometry: GeoCodingGeometry? = null
)

data class GeoCodingGeometry(
    val location: GeoCodingPoint? = null,
    val viewport: GeoCodingBounds? = null
)

data class GeoCodingPoint(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class GeoCodingBounds(
    val northeast: GeoCodingPoint? = null,
    val southwest: GeoCodingPoint? = null
)
