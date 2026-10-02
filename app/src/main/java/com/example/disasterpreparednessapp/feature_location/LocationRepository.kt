package com.example.disasterpreparednessapp.feature_location

import android.content.Context
import com.example.disasterpreparednessapp.feature_location.model.LocationData
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationUtils: LocationUtils
) {
    fun requestLocationUpdates(onLocationReceived: (LocationData) -> Unit) {
        locationUtils.requestLocationUpdates(onLocationReceived)
    }

    fun hasLocationPermission(context: Context): Boolean {
        return locationUtils.hasLocationPermission(context)
    }
}
