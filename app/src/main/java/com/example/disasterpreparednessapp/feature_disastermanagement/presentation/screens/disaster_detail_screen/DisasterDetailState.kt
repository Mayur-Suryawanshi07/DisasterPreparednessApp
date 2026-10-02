package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail_screen

import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert

sealed class DisasterDetailState {
    data object Loading : DisasterDetailState()
    data class Success(val alert: DisasterAlert,val capInfo: CapInfo?=null) : DisasterDetailState()
    data class Error(val message: String) : DisasterDetailState()
}
