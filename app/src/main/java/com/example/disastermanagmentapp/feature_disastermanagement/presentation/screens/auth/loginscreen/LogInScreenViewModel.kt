package com.example.disastermanagmentapp.feature_disastermanagement.presentation.screens.auth.loginscreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class LogInScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Unauthorized)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    val auth = FirebaseAuth.getInstance()

    init {
        checkAuthentication()
    }

    fun checkAuthentication() {
        if (auth.currentUser == null) {
            _state.update {
                LoginUiState.Unauthorized
            }
        } else {
            _state.update {
                LoginUiState.Authorized
            }
        }
    }

    fun login(email: String, password: String) {
        _state.update {
            LoginUiState.Loading
        }
        if (email.isEmpty() || password.isEmpty()) {
            _state.update {
                LoginUiState.Error("Email and Password Cannot be empty")
            }
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.update {
                        LoginUiState.Authorized
                    }
                } else {
                    _state.update {
                        LoginUiState.Unauthorized
                    }
                    _state.update {
                        LoginUiState.Error(message = "Email is not Registered")
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