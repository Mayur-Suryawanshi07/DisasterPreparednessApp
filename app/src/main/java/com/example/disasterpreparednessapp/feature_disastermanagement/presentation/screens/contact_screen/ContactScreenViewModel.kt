package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.contact_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterpreparednessapp.R
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.AllUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    private val SatchUseCase: AllUseCases
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
                        id = 1,
                        imageResId = R.drawable.police,
                        name = "Call Police",
                        phoneNumber = "100",
                        actionType = ContactActionType.PHONE_CALL
                    ),
                    EmergencyContact(
                        id = 2,
                        imageResId = R.drawable.docter,
                        name = "Call Doctor",
                        actionType = ContactActionType.OPEN_CONTACTS
                    ),
                    EmergencyContact(
                        id = 3,
                        imageResId = R.drawable.ambulance,
                        name = "Call Ambulance",
                        phoneNumber = "102",
                        actionType = ContactActionType.PHONE_CALL
                    ),
                    EmergencyContact(
                        id = 4,
                        imageResId = R.drawable.fire,
                        name = "Call Fire Service",
                        phoneNumber = "101",
                        actionType = ContactActionType.PHONE_CALL
                    ),
                    EmergencyContact(
                        id = 5,
                        imageResId = R.drawable.weater_forecast,
                        name = "Check Weather",
                        actionType = ContactActionType.OPEN_WEATHER
                    ),
                    EmergencyContact(
                        id = 6,
                        imageResId = R.drawable.first_aid,
                        name = "First Aid Instruction",
                        actionType = ContactActionType.OPEN_FIRST_AID
                    ),
                    EmergencyContact(
                        id = 7,
                        imageResId = R.drawable.tips_logo,
                        name = "Safety tips",
                        actionType = ContactActionType.OPEN_SAFETY_TIPS
                    ),
                    EmergencyContact(
                        id = 8,
                        imageResId = R.drawable.national_agency_logo,
                        name = "National Disaster Management Authority",
                        actionType = ContactActionType.OPEN_NDMA
                    ),
                    EmergencyContact(
                        id = 9,
                        imageResId = R.drawable.report_logo2,
                        name = "Report A Disaster",
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
