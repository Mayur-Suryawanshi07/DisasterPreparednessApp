package com.example.disasterpreparednessapp.feature_map.presentaion

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val SosRed = Color(0xFFD32F2F)
private val SosBlue = Color(0xFF1976D2)
private val DarkInk = Color(0xFF142B3D)

@Composable
fun RequestHelpScreen(
    navController: NavHostController? = null,
    viewModel: RequestHelpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val activeRequests by viewModel.activeRequests.collectAsState()
    val myRequestState by viewModel.myRequestState.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    val currentUserId = viewModel.currentUserId
    var sosNote by remember { mutableStateOf("Help me I am under the ground") }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(20.5937, 78.9629), 5f)
    }

    // Permission handling
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                viewModel.startLocationUpdates()
            } else {
                Toast.makeText(context, "Location permission required for SOS", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (viewModel.hasLocationPermission(context)) {
            viewModel.startLocationUpdates()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Animate camera to user location when first retrieved
    LaunchedEffect(userLocation) {
        userLocation?.let { (lat, lng) ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 14f)
            )
        }
    }

    Scaffold { insets ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = MapType.NORMAL),
                uiSettings = MapUiSettings(compassEnabled = true, zoomControlsEnabled = false)
            ) {
                // Render all active SOS requests
                activeRequests.forEach { req ->
                    val isMyRequest = req.userId == currentUserId
                    val iconHue = if (isMyRequest) BitmapDescriptorFactory.HUE_AZURE else BitmapDescriptorFactory.HUE_RED
                    val timeStr = remember(req.timestamp) {
                        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(req.timestamp))
                    }

                    Marker(
                        state = rememberMarkerState(
                            key = "${req.id}_${req.timestamp}",
                            position = LatLng(req.latitude, req.longitude)
                        ),
                        title = if (isMyRequest) "My SOS: ${req.message}" else "SOS: ${req.userName}",
                        snippet = "${req.message} ($timeStr)",
                        icon = BitmapDescriptorFactory.defaultMarker(iconHue)
                    )
                }
            }

            // Top Header Card
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(14.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White.copy(alpha = 0.97f),
                shadowElevation = 8.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    if (navController != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkInk)
                        }
                    } else Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "Emergency Help (SOS)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkInk
                        )
                        Text(
                            text = "${activeRequests.size} active SOS request(s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkInk.copy(alpha = 0.65f)
                        )
                    }

                    // My Status Badge
                    val statusText = when (myRequestState) {
                        is RequestState.Active -> "SOS ACTIVE"
                        is RequestState.Loading -> "Updating..."
                        else -> "Ready"
                    }
                    val statusColor = when (myRequestState) {
                        is RequestState.Active -> SosRed
                        is RequestState.Loading -> Color(0xFFFFA000)
                        else -> SosBlue
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = statusColor.copy(alpha = 0.12f),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Re-center My Location FAB
            FloatingActionButton(
                onClick = {
                    userLocation?.let { (lat, lng) ->
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15f))
                        }
                    } ?: run {
                        if (viewModel.hasLocationPermission(context)) {
                            viewModel.startLocationUpdates()
                        } else {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 220.dp),
                containerColor = Color.White
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location", tint = DarkInk)
            }

            // Bottom Action Card for Request / Cancel Help
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (val activeState = myRequestState) {
                        is RequestState.Active -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SosRed)
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Your SOS request is active on the map!",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkInk
                                    )
                                    Text(
                                        text = "Note: \"${activeState.request.message}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DarkInk.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = { viewModel.removeHelpRequest() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DarkInk),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Cancel, contentDescription = null, tint = Color.White)
                                Spacer(Modifier.width(8.dp))
                                Text("Remove Help Request", fontWeight = FontWeight.Bold)
                            }
                        }

                        is RequestState.Loading -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                CircularProgressIndicator(strokeWidth = 2.dp)
                                Spacer(Modifier.width(12.dp))
                                Text("Updating help request...", color = DarkInk)
                            }
                        }

                        else -> {
                            // Custom Emergency Note TextField
                            OutlinedTextField(
                                value = sosNote,
                                onValueChange = { sosNote = it },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                label = { Text("Emergency Note / Situation") },
                                placeholder = { Text("e.g. Help me I am under the ground") },
                                leadingIcon = {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = DarkInk)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SosRed,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedLabelColor = SosRed,
                                    cursorColor = SosRed,
                                    focusedTextColor = DarkInk,
                                    unfocusedTextColor = DarkInk
                                )
                            )

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    viewModel.sendHelpRequest(sosNote.ifBlank { "Help me I am under the ground" })
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SosRed),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White)
                                Spacer(Modifier.width(8.dp))
                                Text("REQUEST HELP (SOS)", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (myRequestState is RequestState.Error) {
                        val errMsg = (myRequestState as RequestState.Error).message
                        Spacer(Modifier.height(8.dp))
                        Text(errMsg, color = SosRed, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
