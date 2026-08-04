package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.loginscreen

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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Graphs
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.component.AuthScreenLayout
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.component.customeColors
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AuthMuted
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AuthOnPrimary
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AuthPrimary
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.DisasterManagmentAppTheme

@Composable
fun LogInScreen(navController: NavHostController) {
    val viewModel = viewModel<LogInScreenViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val isLoading = state is LoginUiState.Loading

    LaunchedEffect(state) {
        when (val currentState = state) {
            is LoginUiState.Authorized -> navController.navigate(Graphs.Main) {
                popUpTo(Graphs.Auth) { inclusive = true }
                launchSingleTop = true
                restoreState = true
            }
            is LoginUiState.Error -> Toast.makeText(
                context,
                currentState.message,
                Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }

    LoginContent(
        isLoading = isLoading,
        onSubmit = { email, password ->
            when {
                !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> Toast.makeText(
                    context, "Enter a valid email address", Toast.LENGTH_SHORT
                ).show()
                password.length < 6 -> Toast.makeText(
                    context, "Password must be at least 6 characters", Toast.LENGTH_SHORT
                ).show()
                else -> viewModel.login(email.trim(), password)
            }
        },
        onCreateAccount = { navController.navigate(Routes.Signup) },
        onContinueAsGuest = {
            navController.navigate(Graphs.Main) {
                popUpTo(Graphs.Auth) { inclusive = true }
                launchSingleTop = true
            }
        }
    )
}

@Composable
private fun LoginContent(
    isLoading: Boolean,
    onSubmit: (email: String, password: String) -> Unit,
    onCreateAccount: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    AuthScreenLayout(
        title = "Welcome back",
        subtitle = "Sign in to access alerts and emergency resources."
    ) {
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
            colors = customeColors()


        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            label = { Text("Password") },
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
            colors = customeColors()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { onSubmit(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = AuthPrimary)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = AuthOnPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text("Sign in", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(14.dp))

        TextButton(
            onClick = onContinueAsGuest,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Continue as guest", color = AuthPrimary, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("New here?", color = AuthMuted)
            TextButton(onClick = onCreateAccount) {
                Text("Create account", color = AuthPrimary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}


@Preview(name = "Login screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LogInScreenPreview() {
    DisasterManagmentAppTheme(darkTheme = false) {
        LoginContent(
            isLoading = false,
            onSubmit = { _, _ -> },
            onCreateAccount = {},
            onContinueAsGuest = {}
        )
    }
}
