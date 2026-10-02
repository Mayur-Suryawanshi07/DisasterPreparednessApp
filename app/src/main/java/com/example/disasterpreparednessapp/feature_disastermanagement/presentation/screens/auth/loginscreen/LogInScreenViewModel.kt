package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.loginscreen

import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LogInScreenViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Unauthorized)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    val auth = FirebaseAuth.getInstance()

    init {
        checkAuthentication()
    }

    fun checkAuthentication() {
        _state.update {
            if (auth.currentUser == null) LoginUiState.Unauthorized else authorizedState()
        }
    }

    private fun authorizedState(shouldRequestLocation: Boolean = false): LoginUiState.Authorized {
        val user = auth.currentUser
        val email = user?.email.orEmpty()
        val displayName = user?.displayName?.takeIf { it.isNotBlank() }
            ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }.ifBlank { "User" }

        return LoginUiState.Authorized(
            displayName = displayName,
            email = email,
            shouldRequestLocation = shouldRequestLocation
        )
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update {
                LoginUiState.Error("Email and Password cannot be empty")
            }
            return
        }

        _state.update { LoginUiState.Loading }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _state.update { authorizedState(shouldRequestLocation = true) }
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        _state.update {
                            LoginUiState.Error("Incorrect password or email. Please try again.")
                        }
                    }
                    is FirebaseAuthInvalidUserException -> {
                        _state.update {
                            LoginUiState.Error("No account found with this email. Please sign up.")
                        }
                    }
                    is FirebaseNetworkException -> {
                        _state.update {
                            LoginUiState.Error("Network error. Please check your internet connection.")
                        }
                    }
                    else -> {
                        _state.update {
                            LoginUiState.Error(
                                exception.localizedMessage
                                    ?: "Unable to sign in. Please try again."
                            )
                        }
                    }
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _state.update {
            LoginUiState.Unauthorized
        }
    }
}
