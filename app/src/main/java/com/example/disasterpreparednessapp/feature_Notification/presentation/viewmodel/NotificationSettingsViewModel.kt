package com.example.disasterpreparednessapp.feature_notification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.feature_notification.domain.use_cases.ObserveNotificationEnabledUseCase
import com.example.disasterpreparednessapp.feature_notification.domain.use_cases.ToggleNotificationEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    observeNotificationEnabledUseCase: ObserveNotificationEnabledUseCase,
    private val toggleNotificationEnabledUseCase: ToggleNotificationEnabledUseCase
) : ViewModel() {

    val isNotificationEnabled: StateFlow<Boolean> = observeNotificationEnabledUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            toggleNotificationEnabledUseCase(enabled)
        }
    }
}
