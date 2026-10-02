package com.example.disasterpreparednessapp.feature_map.presentaion

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.feature_map.API
import com.example.disasterpreparednessapp.feature_map.data.GeoCodingApiService
import com.example.disasterpreparednessapp.feature_map.data.LocationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val geoCodingApiService: GeoCodingApiService
) : ViewModel() {

    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf<String?>(null)
    val address: State<String?> = _address

    private val _isLoadingAddress = mutableStateOf(false)
    val isLoadingAddress: State<Boolean> = _isLoadingAddress

    fun updateLocation(newLocationData: LocationData) {
        _location.value = newLocationData
        fetchAddress(newLocationData.latitude, newLocationData.longitude)
    }

    fun fetchAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoadingAddress.value = true
            try {
                val response = geoCodingApiService.getAddressFromCoordinates(
                    latlng = "$latitude,$longitude",
                    apiKey = API.MY_API
                )
                if (response.results.isNotEmpty()) {
                    _address.value = response.results[0].formatted_address
                } else {
                    _address.value = "No address found for this location"
                }
            } catch (e: Exception) {
                _address.value = "Unable to fetch address details"
            } finally {
                _isLoadingAddress.value = false
            }
        }
    }
}
