package com.example.disasterpreparednessapp.feature_location.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.disasterpreparednessapp.MainActivity
import com.example.disasterpreparednessapp.feature_location.viewmodel.LocationViewModel

@Composable
fun LocationDisplay(
    viewModel: LocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val locationState by viewModel.location.collectAsState()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                // Permission granted
                viewModel.startLocationUpdates()
            } else {
                // Permission denied
                val rationaleResult = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (rationaleResult) {
                    Toast.makeText(context, "Location permission is required for this feature", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Location permission denied. Please enable it in settings.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        locationState?.let {
            Text(
                text = "Latitude: ${it.latitude}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Longitude: ${it.longitude}",
                style = MaterialTheme.typography.bodyLarge
            )
        } ?: Text(text = "Location not available")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (viewModel.hasLocationPermission(context)) {
                    // Permission already granted
                    viewModel.startLocationUpdates()
                } else {
                    // Request permission
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        ) {
            Text(text = "Get Location Updates")
        }
    }
}
