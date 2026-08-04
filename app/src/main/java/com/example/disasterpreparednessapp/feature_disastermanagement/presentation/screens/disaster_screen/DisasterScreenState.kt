package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert

sealed class DisasterScreenUiState {
    object Loading : DisasterScreenUiState()
    data class Success(val events: List<DisasterAlert>) : DisasterScreenUiState()
    data class Error(val message: String) : DisasterScreenUiState()
    object Empty : DisasterScreenUiState()
}

data class DisasterScreenState(
    val uiState: DisasterScreenUiState = DisasterScreenUiState.Loading,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val isRefreshing: Boolean = false
)
