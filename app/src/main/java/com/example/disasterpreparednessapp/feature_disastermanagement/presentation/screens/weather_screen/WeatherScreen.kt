package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.weather_screen

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.disasterpreparednessapp.MainActivity
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.WeatherShimmer
import com.example.disasterpreparednessapp.feature_location.viewmodel.LocationViewModel

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    requestLocationOnStart: Boolean = true,
    viewModel: WeatherViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locationData by locationViewModel.location.collectAsState()

    var city by remember { mutableStateOf("") }
    var fetchedLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                locationViewModel.startLocationUpdates()
            } else {
                val rationaleResult = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (rationaleResult) {
                    Toast.makeText(context, "Location permission is required for local weather", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Location permission denied. Enable in settings.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (locationViewModel.hasLocationPermission(context)) {
            locationViewModel.startLocationUpdates()
        } else if (requestLocationOnStart) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(locationData) {
        locationData?.let { loc ->
            val prev = fetchedLocation
            val latChanged = prev == null || Math.abs(prev.first - loc.latitude) > 0.01
            val lngChanged = prev == null || Math.abs(prev.second - loc.longitude) > 0.01
            if (latChanged || lngChanged) {
                fetchedLocation = Pair(loc.latitude, loc.longitude)
                viewModel.searchWeatherByLatLong(loc.latitude, loc.longitude)
            }
        }
    }

    Column(
        modifier = Modifier
            .then(modifier)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("City") },
            placeholder = { Text("Enter city name") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { viewModel.searchWeather(city) }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        WeatherResult(uiState)
    }
}

@Composable
fun WeatherResult(uiState: WeatherUiState) {
    when (uiState) {
        is WeatherUiState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Search for a city or allow location for weather updates")
            }
        }
        is WeatherUiState.Loading -> {
            WeatherShimmer()
        }
        is WeatherUiState.Success -> {
            WeatherSuccessContent(uiState.weather)
        }
        is WeatherUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
