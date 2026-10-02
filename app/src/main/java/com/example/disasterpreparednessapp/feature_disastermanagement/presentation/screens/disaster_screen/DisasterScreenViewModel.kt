package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.EnrichDisasterAlerts
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.GetDisaster
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.RefreshDisasters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisasterScreenViewModel @Inject constructor(
    private val getDisaster: GetDisaster,
    private val refreshDisasters: RefreshDisasters,
    private val enrichDisasterAlerts: EnrichDisasterAlerts
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DisasterScreenUiState>(DisasterScreenUiState.Loading)

    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private var loadJob: Job? = null
    private var enrichJob: Job? = null

    init {
        loadInitialData()
    }

    fun retry() {
        loadInitialData()
    }

    fun refresh() {
        if (_isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                refreshDisasters()
            } catch (_: Exception) {
                // Keep cached alerts visible when refresh fails offline.
            } finally {
                _isRefreshing.value = false
            }
            startCapEnrichment()
        }
    }

    private fun loadInitialData() {
        loadJob?.cancel()
        if (_uiState.value !is DisasterScreenUiState.Success) {
            _uiState.value = DisasterScreenUiState.Loading
        }
        loadJob = viewModelScope.launch {
            getDisaster()
                .catch {
                    if (_uiState.value is DisasterScreenUiState.Loading) {
                        _uiState.value = DisasterScreenUiState.Error(
                            "Unable to load alerts. Check your connection and try again."
                        )
                    }
                }
                .collect { events ->
                    _uiState.value = DisasterScreenUiState.Success(events)
                }
        }
    }

    private fun startCapEnrichment() {
        enrichJob?.cancel()
        enrichJob = viewModelScope.launch {
            runCatching { enrichDisasterAlerts() }
        }
    }
}
