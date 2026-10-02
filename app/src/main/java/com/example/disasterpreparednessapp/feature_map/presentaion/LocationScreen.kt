package com.example.disasterpreparednessapp.feature_map.presentaion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.MyBottomNavBar
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.alertSeverityBackground
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private val MapInk = Color(0xFF142B3D)
private val AlertRed = Color(0xFFE65F60)

@Composable
fun LocationScreen(
    navController: NavHostController? = null,
    alertId: String? = null,
    viewModel: AlertMapViewModel = hiltViewModel()
) {
    val alerts by viewModel.alerts.collectAsState()
    val selectedAlertId by viewModel.selectedAlertId.collectAsState()
    val capInfo by viewModel.capInfo.collectAsState()
    val areaMarkers by viewModel.areaMarkers.collectAsState()
    val stateBounds by viewModel.stateBounds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedAlert = alerts.firstOrNull { it.id == selectedAlertId }
    val boundaryRings = capInfo?.polygons.orEmpty().mapNotNull(::parseCapPolygon)
    val boundaryBounds = boundaryRings.flatten().takeIf { it.isNotEmpty() }?.let { points ->
        LatLngBounds.builder().apply { points.forEach(::include) }.build()
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(22.5, 79.0), 4.8f)
    }

    LaunchedEffect(alertId, alerts) {
        viewModel.selectAlertById(alertId)
    }

    LaunchedEffect(selectedAlertId, boundaryBounds, areaMarkers, stateBounds) {
        if (boundaryBounds != null) {
            // 1. Zoom to official boundary polygon if available
            cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(boundaryBounds, 90))
        } else if (areaMarkers.isNotEmpty()) {
            // 2. Zoom to fit district or village markers
            if (areaMarkers.size == 1) {
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(areaMarkers.first().position, 10f))
            } else {
                val builder = LatLngBounds.builder()
                areaMarkers.forEach { builder.include(it.position) }
                val bounds = builder.build()
                cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 110))
            }
        } else if (stateBounds != null) {
            // 3. Fallback: Zoom towards state boundary
            cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(stateBounds!!, 100))
        } else {
            // 4. If no state name is given or geocoding fails, do not zoom at all
        }
    }

    Scaffold(
        bottomBar = { navController?.let { MyBottomNavBar(navController = it) } }
    ) { insets ->
        Box(Modifier.fillMaxSize().padding(insets)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = MapType.TERRAIN),
                uiSettings = MapUiSettings(compassEnabled = true, zoomControlsEnabled = false, mapToolbarEnabled = false)
            ) {
                boundaryRings.forEachIndexed { index, ring ->
                    Polygon(
                        points = ring,
                        fillColor = AlertRed.copy(alpha = 0.58f),
                        strokeColor = AlertRed.copy(alpha = 0.95f),
                        strokeWidth = 2.5f,
                        zIndex = index.toFloat()
                    )
                }

                areaMarkers.forEach { marker ->
                    Marker(
                        state = rememberMarkerState(
                            key = "${selectedAlertId}_${marker.id}",
                            position = marker.position
                        ),
                        title = marker.title,
                        snippet = capInfo?.event ?: selectedAlert?.title ?: "Affected Location"
                    )
                }
            }

            Surface(
                modifier = Modifier.align(Alignment.TopStart).fillMaxWidth().padding(14.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White.copy(alpha = 0.97f),
                shadowElevation = 8.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (navController != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MapInk)
                        }
                    } else Spacer(Modifier.width(16.dp))
                    Column(Modifier.padding(vertical = 12.dp, horizontal = 4.dp)) {
                        Text("Alert map", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = MapInk)
                        Text("${alerts.size} active alerts", style = MaterialTheme.typography.bodySmall, color = MapInk.copy(alpha = 0.68f))
                    }
                }
            }

            Card(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    Modifier
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Surface(Modifier.align(Alignment.CenterHorizontally), shape = RoundedCornerShape(4.dp), color = Color(0xFFD7DDE1)) {
                        Spacer(Modifier.width(38.dp).height(4.dp))
                    }
                    Spacer(Modifier.height(14.dp))
                    when {
                        isLoading && alerts.isEmpty() -> LoadingAlerts()
                        selectedAlert == null -> EmptyAlerts()
                        else -> AlertMapDetails(
                            alert = selectedAlert,
                            capInfo = capInfo,
                            hasBoundary = boundaryRings.isNotEmpty() || areaMarkers.isNotEmpty()
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun AlertMapDetails(alert: DisasterAlert, capInfo: CapInfo?, hasBoundary: Boolean) {
    val severity = capInfo?.severity
    val tint = alertSeverityBackground(alert.title, alert.category, severity, capInfo?.urgency)
    Column {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(capInfo?.event?.takeIf(String::isNotBlank) ?: alert.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MapInk, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text("Issued by ${alert.author?.takeIf(String::isNotBlank) ?: "Official source"}", style = MaterialTheme.typography.bodyMedium, color = MapInk.copy(alpha = 0.76f))
            }
            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = MapInk, modifier = Modifier.size(30.dp))
        }
        Spacer(Modifier.height(12.dp))
        Surface(shape = RoundedCornerShape(18.dp), color = tint) {
            Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.width(84.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(severity ?: "Alert", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Intensity", style = MaterialTheme.typography.labelSmall, color = Color.Black.copy(alpha = 0.75f))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.LocationOn, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            capInfo?.affectedAreas?.takeIf { it.isNotEmpty() }?.joinToString() ?: "Affected districts are loading…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.height(9.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, null, tint = Color.Black, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Expires: ${capInfo?.expires ?: "Not specified"}", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                    }
                }
            }
        }
        Spacer(Modifier.height(9.dp))
        Text(
            if (hasBoundary) "Affected locations are marked on the map" else "This alert has no specific map location; affected districts are shown above",
            style = MaterialTheme.typography.labelSmall,
            color = MapInk.copy(alpha = 0.65f)
        )
        Spacer(Modifier.height(9.dp))
        Text(
            capInfo?.description?.takeIf(String::isNotBlank) ?: alert.description?.takeIf(String::isNotBlank) ?: "Official alert details are being updated.",
            style = MaterialTheme.typography.bodyMedium,
            color = MapInk,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LoadingAlerts() {
    Row(Modifier.fillMaxWidth().padding(vertical = 26.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = MapInk)
        Spacer(Modifier.width(10.dp))
        Text("Loading disaster alerts…", color = MapInk)
    }
}

@Composable
private fun EmptyAlerts() {
    Text("No active alerts are available right now.", Modifier.padding(vertical = 24.dp), color = MapInk)
}

private fun parseCapPolygon(raw: String): List<LatLng>? {
    val points = raw.trim().split(Regex("\\s+")).mapNotNull { pair ->
        val values = pair.split(',')
        if (values.size != 2) return@mapNotNull null
        val latitude = values[0].toDoubleOrNull() ?: return@mapNotNull null
        val longitude = values[1].toDoubleOrNull() ?: return@mapNotNull null
        if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) return@mapNotNull null
        LatLng(latitude, longitude)
    }
    return points.takeIf { it.size >= 3 }
}
