package com.example.disasterpreparednessapp.feature_location.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.disasterpreparednessapp.feature_location.LocationRepository
import com.example.disasterpreparednessapp.feature_location.model.LocationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository
): ViewModel()  {

    private val _location = MutableStateFlow<LocationData?>(null)
    val location: StateFlow<LocationData?> = _location.asStateFlow()

    fun startLocationUpdates() {
        repository.requestLocationUpdates { newLocation ->
            _location.value = newLocation
        }
    }

    fun hasLocationPermission(context: Context): Boolean {
        return repository.hasLocationPermission(context)
    }
}
