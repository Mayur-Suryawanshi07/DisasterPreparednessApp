package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.GetDisasterById
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.GetDisasterDetails
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.repository.DisasterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisasterDetailScreenViewModel @Inject constructor(
    private val getDisasterById: GetDisasterById,
    private val getDisasterDetails: GetDisasterDetails,
    private val repository: DisasterRepository
) : ViewModel() {
    private val _state = MutableStateFlow<DisasterDetailState>(DisasterDetailState.Loading)
    val state: StateFlow<DisasterDetailState> = _state.asStateFlow()

    fun loadAlert(id: String) {
        viewModelScope.launch {
            _state.value = DisasterDetailState.Loading

            val alert = getDisasterById(id)
            if (alert == null) {
                _state.value = DisasterDetailState.Error(
                    "This alert is no longer available. Please return to the alert feed and refresh."
                )
                return@launch
            }

            // Render immediately with what we have — capInfo backfills below.
            _state.value = DisasterDetailState.Success(alert = alert, capInfo = null)

            getDisasterDetails(id)
                .catch {
                    // Detail failed to load — leave the alert on screen with
                    // its placeholder text rather than erroring the whole screen.
                }
                .collect { infos ->
                    val capInfo = pickPreferredInfo(infos)
                    capInfo?.let { repository.saveCapInfo(it) }
                    _state.value = DisasterDetailState.Success(
                        alert = alert,
                        capInfo = capInfo
                    )
                }
        }
    }

    // Alerts on this feed commonly ship an English + a regional-language
    // <info> block for the same event; prefer English, else take whatever's there.
    private fun pickPreferredInfo(infos: List<CapInfo>): CapInfo? =
        infos.firstOrNull { it.language?.startsWith("en", ignoreCase = true) == true }
            ?: infos.firstOrNull()
}