package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.signupscreen

import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val myRef = FirebaseDatabase.getInstance().getReference("users")

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Unauthenticated)
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    fun signup(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _state.update {
                SignUpState.Error("Name, email, and password cannot be empty")
            }
            return
        }

        _state.update { SignUpState.Loading }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val currentUser = authResult.user ?: auth.currentUser
                val uid = currentUser?.uid

                if (uid == null) {
                    _state.update { SignUpState.Error("Failed to get user details") }
                    return@addOnSuccessListener
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name.trim())
                    .build()

                currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener {
                        saveData(
                            uid = uid,
                            name = name.trim(),
                            email = email.trim()
                        )
                    }
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthUserCollisionException -> {
                        _state.update { SignUpState.UserCollision }
                    }
                    is FirebaseAuthWeakPasswordException -> {
                        _state.update {
                            SignUpState.Error("Password is too weak. Please use at least 6 characters.")
                        }
                    }
                    is FirebaseNetworkException -> {
                        _state.update {
                            SignUpState.Error("Network error. Please check your internet connection.")
                        }
                    }
                    else -> {
                        _state.update {
                            SignUpState.Error(
                                exception.localizedMessage
                                    ?: "Unable to create your account. Please try again."
                            )
                        }
                    }
                }
            }
    }

    fun saveData(uid: String, name: String, email: String) {
        val user = User(
            uid = uid,
            name = name,
            email = email
        )

        myRef.child(uid)
            .setValue(user)
            .addOnCompleteListener {
                // Since Firebase Auth account creation succeeded, transition to Authenticated and proceed
                _state.update {
                    SignUpState.Authenticated(shouldRequestLocation = true)
                }
            }
    }
}

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = ""
)
