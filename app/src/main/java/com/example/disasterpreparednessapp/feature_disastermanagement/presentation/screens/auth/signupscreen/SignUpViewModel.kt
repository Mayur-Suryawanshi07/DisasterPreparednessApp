package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.signupscreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Unauthenticated)
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    init {
        checkAuthentication()
    }

    fun checkAuthentication() {
        _state.update {
            if (auth.currentUser == null) {
                SignUpState.Unauthenticated
            } else {
                SignUpState.Authenticated
            }
        }
    }

    fun signup(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update {
                SignUpState.Error("Email and password cannot be empty")
            }
            return
        }

        _state.update { SignUpState.Loading }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _state.update {
                    if (task.isSuccessful) {
                        SignUpState.Authenticated
                    } else {
                        SignUpState.Error(
                            task.exception?.localizedMessage ?: "Unable to create your account"
                        )
                    }
                }
            }
    }
}
