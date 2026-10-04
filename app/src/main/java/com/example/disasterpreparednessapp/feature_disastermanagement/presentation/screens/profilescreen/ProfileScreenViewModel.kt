package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.profilescreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        _state.update { ProfileScreenState.Loading }

        val user = auth.currentUser
        if (user == null) {
            _state.update { ProfileScreenState.Unauthorized }
            return
        }

        val email = user.email.orEmpty()
        val displayName = user.displayName?.takeIf { it.isNotBlank() }
            ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }.ifBlank { "User" }

        _state.update {
            ProfileScreenState.Success(
                user = ProfileUser(
                    displayName = displayName,
                    email = email
                )
            )
        }
    }

    fun signOut() {
        auth.signOut()
        _state.update { ProfileScreenState.Unauthorized }
    }
}
