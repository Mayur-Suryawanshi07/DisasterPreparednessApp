package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.signupscreen

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Graphs
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.component.AuthScreenLayout
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.DisasterManagmentAppTheme

@Composable
fun SignUpScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<SignUpViewModel>()
    val state by viewModel.state.collectAsState()
    val isLoading = state is SignUpState.Loading

    fun openMainScreen() {
        navController.navigate(Graphs.Main) {
            popUpTo(Graphs.Auth) { inclusive = true }
            launchSingleTop = true
        }
    }

    LaunchedEffect(state) {
        when (val currentState = state) {
            is SignUpState.Authenticated -> {
                Toast.makeText(context, "Signup successful! Welcome!", Toast.LENGTH_SHORT).show()
                openMainScreen()
            }
            is SignUpState.UserCollision -> {
                Toast.makeText(
                    context,
                    "An account with this email already exists. Please log in instead.",
                    Toast.LENGTH_LONG
                ).show()
                val popped = navController.popBackStack(Routes.Login, inclusive = false)
                if (!popped) {
                    navController.navigate(Routes.Login) {
                        launchSingleTop = true
                    }
                }
            }
            is SignUpState.Error -> {
                Toast.makeText(
                    context,
                    currentState.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> Unit
        }
    }

    SignUpContent(
        isLoading = isLoading,
        onSubmit = { name, email, password ->
            when {
                name.isBlank() -> Toast.makeText(
                    context, "Enter your name", Toast.LENGTH_SHORT
                ).show()
                !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> Toast.makeText(
                    context, "Enter a valid email address", Toast.LENGTH_SHORT
                ).show()
                password.length < 6 -> Toast.makeText(
                    context, "Password must be at least 6 characters", Toast.LENGTH_SHORT
                ).show()
                else -> {
                    Toast.makeText(context, "Creating account...", Toast.LENGTH_SHORT).show()
                    viewModel.signup(name.trim(), email.trim(), password)
                }
            }
        },
        onSignIn = {
            Toast.makeText(context, "Navigating to Login", Toast.LENGTH_SHORT).show()
            val popped = navController.popBackStack(Routes.Login, inclusive = false)
            if (!popped) {
                navController.navigate(Routes.Login) {
                    launchSingleTop = true
                }
            }
        },
        onContinueAsGuest = {
            openMainScreen()
        }
    )
}

@Composable
private fun SignUpContent(
    isLoading: Boolean,
    onSubmit: (name: String, email: String, password: String) -> Unit,
    onSignIn: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    AuthScreenLayout(
        title = "Create your account",
        subtitle = "Join to receive trusted updates when they matter most."
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            label = { Text("Your name") },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = customColors(),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            label = { Text("Email address") },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = customColors(),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            label = { Text("Password") },
            supportingText = {
                Text(
                    "Use at least 6 characters",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff
                        else Icons.Outlined.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            colors = customColors(),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { onSubmit(name, email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    "Create account",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        TextButton(
            onClick = onContinueAsGuest,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(
                "Continue as guest",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Already have an account?",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onSignIn) {
                Text(
                    "Log in",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(name = "Sign-up screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SignUpScreenPreview() {
    DisasterManagmentAppTheme(darkTheme = false) {
        SignUpContent(
            isLoading = false,
            onSubmit = { _, _, _ -> },
            onSignIn = {},
            onContinueAsGuest = {}
        )
    }
}

@Composable
fun customColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface
)
