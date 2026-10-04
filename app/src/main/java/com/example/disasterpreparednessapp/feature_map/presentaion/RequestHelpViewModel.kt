package com.example.disasterpreparednessapp.feature_map.presentaion

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.feature_map.domain.model.HelpRequest
import com.example.disasterpreparednessapp.feature_map.domain.repository.HelpRequestRepository
import com.example.disasterpreparednessapp.feature_location.LocationRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RequestState {
    data object Idle : RequestState
    data class Active(val request: HelpRequest) : RequestState
    data object Loading : RequestState
    data class Error(val message: String) : RequestState
}

@HiltViewModel
class RequestHelpViewModel @Inject constructor(
    private val helpRequestRepository: HelpRequestRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    val currentUserId: String get() = auth.currentUser?.uid.orEmpty()
    val currentUserName: String
        get() = auth.currentUser?.displayName?.takeIf { it.isNotBlank() }
            ?: auth.currentUser?.email?.substringBefore("@")
            ?: "User"

    private val _activeRequests = MutableStateFlow<List<HelpRequest>>(emptyList())
    val activeRequests: StateFlow<List<HelpRequest>> = _activeRequests.asStateFlow()

    private val _myRequestState = MutableStateFlow<RequestState>(RequestState.Loading)
    val myRequestState: StateFlow<RequestState> = _myRequestState.asStateFlow()

    private val _userLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val userLocation: StateFlow<Pair<Double, Double>?> = _userLocation.asStateFlow()

    init {
        // Observe all active requests via snapshotListener wrapped in callbackFlow
        viewModelScope.launch {
            helpRequestRepository.observeActiveRequests().collect { requests ->
                _activeRequests.value = requests
            }
        }

        // Observe current user's active request
        viewModelScope.launch {
            val uid = currentUserId
            if (uid.isNotBlank()) {
                helpRequestRepository.observeUserActiveRequest(uid).collect { req ->
                    _myRequestState.value = if (req != null) RequestState.Active(req) else RequestState.Idle
                }
            } else {
                _myRequestState.value = RequestState.Idle
            }
        }
    }

    fun startLocationUpdates() {
        locationRepository.requestLocationUpdates { locData ->
            _userLocation.value = Pair(locData.latitude, locData.longitude)
        }
    }

    fun hasLocationPermission(context: Context): Boolean {
        return locationRepository.hasLocationPermission(context)
    }

    fun sendHelpRequest(message: String = "Emergency! Need immediate help.") {
        val uid = currentUserId
        if (uid.isBlank()) {
            _myRequestState.value = RequestState.Error("Please log in to request help.")
            return
        }

        val (lat, lng) = _userLocation.value ?: run {
            _myRequestState.value = RequestState.Error("Fetching your location... Please try again in a moment.")
            return
        }

        _myRequestState.value = RequestState.Loading
        viewModelScope.launch {
            val request = HelpRequest(
                id = uid,
                userId = uid,
                userName = currentUserName,
                latitude = lat,
                longitude = lng,
                message = message,
                timestamp = System.currentTimeMillis(),
                status = "active"
            )

            helpRequestRepository.sendHelpRequest(request)
                .onSuccess {
                    _myRequestState.value = RequestState.Active(request)
                }
                .onFailure { err ->
                    _myRequestState.value = RequestState.Error(err.localizedMessage ?: "Failed to send help request")
                }
        }
    }

    fun removeHelpRequest() {
        val uid = currentUserId
        if (uid.isBlank()) return

        _myRequestState.value = RequestState.Loading
        viewModelScope.launch {
            helpRequestRepository.removeHelpRequest(uid)
                .onSuccess {
                    _myRequestState.value = RequestState.Idle
                }
                .onFailure { err ->
                    _myRequestState.value = RequestState.Error(err.localizedMessage ?: "Failed to remove help request")
                }
        }
    }
}
