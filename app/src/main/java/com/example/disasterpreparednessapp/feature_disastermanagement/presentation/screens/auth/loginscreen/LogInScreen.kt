package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.loginscreen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Graphs
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.component.AuthScreenLayout
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.signupscreen.customColors
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.DisasterManagmentAppTheme
import com.example.disasterpreparednessapp.feature_location.LocationPromptPreferences
import com.example.disasterpreparednessapp.feature_location.NotificationPermissionPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<LogInScreenViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val isLoading = state is LoginUiState.Loading
    val locationPromptPreferences = remember(context) { LocationPromptPreferences(context) }
    val notificationPermissionPreferences = remember(context) { NotificationPermissionPreferences(context) }

    var pendingUserId by remember { mutableStateOf<String?>(null) }

    fun openMainScreen() {
        navController.navigate(Graphs.Main) {
            popUpTo(Graphs.Auth) { inclusive = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        scope.launch { notificationPermissionPreferences.markNotificationPermissionAsked() }
        openMainScreen()
    }

    fun proceedToMainOrRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            scope.launch {
                if (!notificationPermissionPreferences.hasAskedNotificationPermission()) {
                    notificationPermissionPreferences.markNotificationPermissionAsked()
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    openMainScreen()
                }
            }
        } else {
            openMainScreen()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        pendingUserId?.let(locationPromptPreferences::markPromptCompleted)
        pendingUserId = null
        proceedToMainOrRequestNotificationPermission()
    }

    LaunchedEffect(state) {
        when (val currentState = state) {
            is LoginUiState.Authorized -> {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                if (currentState.shouldRequestLocation && userId != null && !locationPromptPreferences.hasCompletedPrompt(userId)) {
                    pendingUserId = userId
                } else {
                    proceedToMainOrRequestNotificationPermission()
                }
            }
            is LoginUiState.Error -> {
                Toast.makeText(
                    context,
                    currentState.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> Unit
        }
    }

    pendingUserId?.let { userId ->
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Use your location for local weather?") },
            text = { Text("Your selected location lets us show the weather forecast for your area. You can change this permission later in Android settings.") },
            confirmButton = {
                TextButton(onClick = {
                    val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    if (fineGranted || coarseGranted) {
                        locationPromptPreferences.markPromptCompleted(userId)
                        pendingUserId = null
                        proceedToMainOrRequestNotificationPermission()
                    } else {
                        locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    }
                }) { Text("Allow location") }
            },
            dismissButton = {
                TextButton(onClick = {
                    locationPromptPreferences.markPromptCompleted(userId)
                    pendingUserId = null
                    proceedToMainOrRequestNotificationPermission()
                }) { Text("Not now") }
            }
        )
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
                else -> {
                    Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show()
                    viewModel.login(email.trim(), password)
                }
            }
        },
        onCreateAccount = {
            Toast.makeText(context, "Navigating to Signup", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.Signup) {
                launchSingleTop = true
            }
        },
        onContinueAsGuest = { proceedToMainOrRequestNotificationPermission() }
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
            onClick = { onSubmit(email, password) },
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
                    "Log in",
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
                "New here?",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onCreateAccount) {
                Text(
                    "Create account",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
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
