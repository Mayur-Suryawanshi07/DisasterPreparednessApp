package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.contact_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.R
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.AllUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    private val allUseCases: AllUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactScreenState())
    val uiState: StateFlow<ContactScreenState> = _uiState.asStateFlow()

    init {
        loadEmergencyContacts()
    }

    private fun loadEmergencyContacts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val contacts = listOf(
                    EmergencyContact(
                        id = 4,
                        imageResId = R.drawable.ic_national_agency_logo,
                        name = "NDMA Website",
                        actionType = ContactActionType.OPEN_NDMA
                    ),
                    EmergencyContact(
                        id = 5,
                        imageResId = R.drawable.ic_weater_forecast,
                        name = "Weather Updates",
                        actionType = ContactActionType.OPEN_WEATHER
                    ),
                    EmergencyContact(
                        id = 6,
                        imageResId = R.drawable.ic_medical_tips,
                        name = "First Aid Guide",
                        actionType = ContactActionType.OPEN_FIRST_AID
                    ),
                    EmergencyContact(
                        id = 7,
                        imageResId = R.drawable.ic_tips_logo,
                        name = "Safety Tips",
                        actionType = ContactActionType.OPEN_SAFETY_TIPS
                    ),
                    EmergencyContact(
                        id = 8,
                        imageResId = R.drawable.ic_report_logo,
                        name = "Report Incident",
                        actionType = ContactActionType.OPEN_REPORT
                    )
                )
                
                _uiState.update { 
                    it.copy(
                        emergencyContacts = contacts,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load emergency contacts"
                    )
                }
            }
        }
    }

    fun getPhoneNumber(contactName: String): String {
        return when (contactName) {
            "Call Police" -> "100"
            "Call Ambulance" -> "102"
            "Call Fire Service" -> "101"
            else -> ""
        }
    }

    fun retry() {
        loadEmergencyContacts()
    }
}
