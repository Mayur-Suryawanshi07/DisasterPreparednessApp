package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.loginscreen

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AppWhite
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.Border
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.Ink
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.InkMuted

sealed class LoginUiState {
    data class Authorized(
        val displayName: String,
        val email: String,
        val shouldRequestLocation: Boolean = false
    ) : LoginUiState()

    object Unauthorized : LoginUiState()
    object Loading : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

val AuthPrimary = Ink
val AuthOnPrimary = AppWhite
val AuthMuted = InkMuted
val AuthBorder = Border

@Composable
fun customeColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AuthPrimary,
    unfocusedBorderColor = AuthBorder,
    focusedLabelColor = AuthPrimary,
    cursorColor = AuthPrimary
)

