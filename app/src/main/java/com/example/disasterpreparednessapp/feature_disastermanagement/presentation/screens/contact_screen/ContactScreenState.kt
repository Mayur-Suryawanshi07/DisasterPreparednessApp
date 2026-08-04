package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.contact_screen

data class EmergencyContact(
    val id: Int,
    val imageResId: Int,
    val name: String,
    val phoneNumber: String? = null,
    val actionType: ContactActionType
)

enum class ContactActionType {
    PHONE_CALL,
    OPEN_CONTACTS,
    OPEN_URL,
    OPEN_WEATHER,
    OPEN_FIRST_AID,
    OPEN_SAFETY_TIPS,
    OPEN_NDMA,
    OPEN_REPORT
}

data class ContactScreenState(
    val emergencyContacts: List<EmergencyContact> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
