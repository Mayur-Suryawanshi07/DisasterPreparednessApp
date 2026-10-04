package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.profilescreen

data class ProfileUser(
    val displayName: String,
    val email: String
)

sealed class ProfileScreenState {
    object Loading : ProfileScreenState()
    object Unauthorized : ProfileScreenState()
    data class Success(val user: ProfileUser) : ProfileScreenState()
}
