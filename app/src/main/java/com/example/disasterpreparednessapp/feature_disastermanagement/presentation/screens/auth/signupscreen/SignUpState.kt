package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.signupscreen

sealed class SignUpState {
    data class Authenticated(val shouldRequestLocation: Boolean = false) : SignUpState()
    data object Unauthenticated : SignUpState()
    data object Loading : SignUpState()
    data object UserCollision : SignUpState()
    data class Error(val message: String) : SignUpState()
}
