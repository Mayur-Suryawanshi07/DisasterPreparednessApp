package com.example.disasterpreparednessapp.feature_map.presentaion

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val density = LocalDensity.current.density
    var isCardExpanded by remember { mutableStateOf(true) }
    var isDragging by remember { mutableStateOf(false) }
    var cardHeightDp by remember { mutableStateOf(420.dp) }

    val animatedCardHeight by animateDpAsState(
        targetValue = if (isDragging) cardHeightDp else (if (isCardExpanded) 420.dp else 150.dp),
        animationSpec = tween(durationMillis = 200),
        label = "cardHeightAnimation"
    )

    val dragGestureModifier = Modifier.pointerInput(Unit) {
        detectVerticalDragGestures(
            onDragStart = {
                isDragging = true
                cardHeightDp = if (isCardExpanded) 420.dp else 150.dp
            },
            onVerticalDrag = { change, dragAmount ->
                change.consume()
                val dragAmountDp = (dragAmount / density).dp
                cardHeightDp = (cardHeightDp - dragAmountDp).coerceIn(140.dp, 500.dp)
            },
            onDragEnd = {
                isDragging = false
                isCardExpanded = cardHeightDp > 280.dp
            },
            onDragCancel = {
                isDragging = false
            }
        )
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

            // Top bar
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

            // Retractable Bottom Detail Card with Finger Drag Tracking
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    Modifier
                        .navigationBarsPadding()
                        .height(animatedCardHeight)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    // Drag Handle (Slidable by finger gestures or clickable)
                    Surface(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .then(dragGestureModifier)
                            .clickable { isCardExpanded = !isCardExpanded }
                            .padding(vertical = 10.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFD7DDE1)
                    ) {
                        Spacer(Modifier.width(44.dp).height(5.dp))
                    }

                    Spacer(Modifier.height(4.dp))

                    when {
                        isLoading && alerts.isEmpty() -> LoadingAlerts()
                        selectedAlert == null -> EmptyAlerts()
                        else -> AlertMapDetails(
                            alert = selectedAlert,
                            capInfo = capInfo,
                            hasBoundary = boundaryRings.isNotEmpty() || areaMarkers.isNotEmpty(),
                            isExpanded = isCardExpanded,
                            onToggleExpand = { isCardExpanded = !isCardExpanded },
                            headerModifier = dragGestureModifier
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun AlertMapDetails(
    alert: DisasterAlert,
    capInfo: CapInfo?,
    hasBoundary: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    headerModifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val severity = capInfo?.severity ?: "Alert"
    val tint = alertSeverityBackground(alert.title, alert.category, severity, capInfo?.urgency)
    val eventTitle = capInfo?.event?.takeIf(String::isNotBlank) ?: alert.title
    val descriptionText = capInfo?.description?.takeIf(String::isNotBlank)
        ?: alert.description?.takeIf(String::isNotBlank)
        ?: "Official alert details are currently being updated."
    val instructionText = capInfo?.instruction?.takeIf(String::isNotBlank)

    Column(Modifier.fillMaxWidth()) {
        // Header Row (Supports vertical finger drag gestures & click toggle)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(headerModifier)
                .clickable { onToggleExpand() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = eventTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MapInk,
                    maxLines = if (isExpanded) 2 else 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Issued by ${alert.author?.takeIf(String::isNotBlank) ?: "Official source"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MapInk.copy(alpha = 0.7f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = tint,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        text = severity,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                IconButton(onClick = onToggleExpand) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = if (isExpanded) "Minimize" else "Expand",
                        tint = MapInk
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Intensity & Affected Area Card
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = tint,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(84.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = severity,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Intensity",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black.copy(alpha = 0.75f)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = capInfo?.affectedAreas?.takeIf { it.isNotEmpty() }?.joinToString()
                                ?: "Affected districts are loading…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            maxLines = if (isExpanded) 6 else 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Expires: ${capInfo?.expires ?: "Not specified"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Expanded Section (Full Description, Safety Instructions, Share Option)
        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "DISASTER DESCRIPTION",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MapInk.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MapInk,
                    lineHeight = 20.sp
                )

                if (instructionText != null) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "SAFETY INSTRUCTIONS / ADVICE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MapInk.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE3F2FD),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = MapInk,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = instructionText,
                                style = MaterialTheme.typography.bodySmall,
                                color = MapInk,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                OutlinedButton(
                    onClick = {
                        val shareText = "$eventTitle\n\n$descriptionText"
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share disaster alert"))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Share Alert Details")
                }
            }
        }
    }
}

@Composable
private fun LoadingAlerts() {
    Row(Modifier.fillMaxWidth().padding(vertical = 26.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = MapInk)
        Spacer(Modifier.width(10.dp))
        Text("Loading disaster alert details…", color = MapInk)
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
