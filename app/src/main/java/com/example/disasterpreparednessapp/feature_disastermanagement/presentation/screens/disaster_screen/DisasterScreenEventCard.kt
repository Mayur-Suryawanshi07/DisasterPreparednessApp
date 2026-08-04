package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes

@Composable
fun DisasterScreenEventCard(event: DisasterAlert, onClick: () -> Unit = {}) {
    val tone = severityTone(event)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = tone.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(
                        tone.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = tone.accent,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        event.author,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Ink,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    Modifier
                        .size(42.dp)
                        .background(tone.iconBackground, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.WarningAmber, null, tint = tone.accent)
                }
            }
            Spacer(Modifier.height(14.dp));
            HorizontalDivider(color = tone.divider);
            Spacer(
                Modifier.height(12.dp)
            )
            Text(
                event.link.ifBlank { "Official alert details are being updated." },
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF34485B),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color.White.copy(alpha = .72f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(event.category?.ifBlank { "General" } ?: "General",
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Ink)
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    event.pubDate,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF5B6D7C),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    Icons.Default.ArrowOutward,
                    "View alert",
                    tint = tone.accent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
@Composable
private fun AlertHeader(isRefreshing: Boolean, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Ink)
            .padding(start = 20.dp, end = 12.dp, top = 20.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Color(0xFFFCB544),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.size(46.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.NotificationsActive,
                    null,
                    tint = Ink
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "EMERGENCY WATCH",
                color = Color(0xFFB8C7D8),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Active alerts",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
        IconButton(onClick = onRefresh, enabled = !isRefreshing) {
            if (isRefreshing) CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            else Icon(Icons.Default.Refresh, "Refresh alerts", tint = Color.White)
        }
    }
}

@Composable
private fun LoadingState() = Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    CircularProgressIndicator(color = Ink)
}

@Composable
private fun EmptyState() =
    Box(Modifier
        .fillMaxSize()
        .padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "All clear for now",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "No alerts match the current view.",
                color = Color(0xFF5F6E7D),
                textAlign = TextAlign.Center
            )
        }
    }

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) =
    Box(Modifier
        .fillMaxSize()
        .padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Unable to update alerts",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp)); Text(
            message,
            textAlign = TextAlign.Center,
            color = Color(0xFFB3261E)
        )
            Spacer(Modifier.height(16.dp)); Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Ink)
        ) { Text("Try again") }
        }
    }

@Composable
private fun DisasterEventList(
    events: List<DisasterAlert>,
    searchText: String,
    navController: NavHostController
) {
    val visibleEvents = events.filter { event ->
        searchText.isBlank() || "${event.title} ${event.description} ${event.category.orEmpty()}".contains(
            searchText,
            ignoreCase = true
        )
    }
    if (visibleEvents.isEmpty()) {
        EmptyState(); return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            visibleEvents,
            key = { it.id }) { event ->
            DisasterScreenEventCard(event) {
                navController.navigate(
                    Routes.DisasterDetail(
                        event.id
                    )
                )
            }
        }
    }
}
private data class SeverityTone(
    val label: String,
    val background: Color,
    val accent: Color,
    val iconBackground: Color,
    val divider: Color
)

private fun severityTone(event: DisasterAlert): SeverityTone {
    val text = "${event.title} ${event.category.orEmpty()}".lowercase()
    return when {
        listOf(
            "cyclone",
            "fire",
            "earthquake",
            "extreme",
            "severe"
        ).any(text::contains) -> SeverityTone(
            "Critical",
            Color(0xFFFFE8E3),
            Color(0xFFC83B2A),
            Color(0xFFFFD4CC),
            Color(0xFFF4C7BE)
        )

        listOf(
            "flood",
            "storm",
            "rain",
            "weather"
        ).any(text::contains) -> SeverityTone(
            "Weather alert",
            Color(0xFFFFF3D6),
            Color(0xFF9A5B00),
            Color(0xFFFFE1A1),
            Color(0xFFF1D9A2)
        )

        else -> SeverityTone(
            "Advisory",
            Color(0xFFE3F0FA),
            Color(0xFF1A6597),
            Color(0xFFC9E5F7),
            Color(0xFFC4DFEF)
        )
    }
}
