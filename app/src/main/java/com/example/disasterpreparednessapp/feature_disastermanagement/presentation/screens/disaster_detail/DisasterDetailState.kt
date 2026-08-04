package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert

sealed class DisasterDetailState() {
    data object Loading : DisasterDetailState()
    data class Success(val alert: DisasterAlert) : DisasterDetailState()
    data class Error(val message: String) : DisasterDetailState()
}
