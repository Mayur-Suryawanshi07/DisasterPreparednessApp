package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.alertDisplayName
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.alertSeverityBackground
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.alertWeatherImage
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.formatAlertListDate
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.formatIntensityLabel
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.formatIssuedBy

@Composable
fun DisasterScreenCard(
    events: List<DisasterAlert>,
    searchText: String,
    navController: NavHostController
) {

    val visibleEvents = events.filter { event ->
        searchText.isBlank() ||
            alertDisplayName(event.title, event.capInfo?.event)
                .contains(searchText, ignoreCase = true) ||
            formatIssuedBy(event.author).contains(searchText, ignoreCase = true)
    }

    if (visibleEvents.isEmpty()) {
        EmptyState()
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = visibleEvents,
            key = { event -> event.id }
        ) { event ->
            DisasterScreenEventCard(
                disaster = event,
                capInfo = event.capInfo,
                onClick = {
                    navController.navigate(Routes.Map(event.id))
                }
            )
        }
    }
}
@Composable
fun DisasterScreenEventCard(
    disaster: DisasterAlert,
    capInfo: CapInfo?,
    onClick: () -> Unit
) {
    val eventName = alertDisplayName(disaster.title, capInfo?.event)
    val issuedBy = formatIssuedBy(disaster.author)
    val issuedAt = formatAlertListDate(disaster.publishedAt)
    val intensity = formatIntensityLabel(capInfo?.severity)
    val affectedAreas = capInfo?.affectedAreas?.takeIf { it.isNotEmpty() }
    val validTill = formatAlertListDate(capInfo?.expires)
    val bgColor = alertSeverityBackground(
        title = disaster.title,
        category = disaster.category,
        severity = capInfo?.severity,
        urgency = capInfo?.urgency
    )
    val intensityDrawable = when (capInfo?.severity) {
        "Severe" -> com.example.disasterpreparednessapp.R.drawable.moderate_severity
        "Moderate" -> com.example.disasterpreparednessapp.R.drawable.low_severity
        else -> com.example.disasterpreparednessapp.R.drawable.high_severity
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text = eventName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 20.sp,
                            lineHeight = 24.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Issued By $issuedBy",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.88f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = issuedAt,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.88f)
                    )
                }
                Image(
                    painter = painterResource(alertWeatherImage(eventName)),
                    contentDescription = null,
                    modifier = Modifier.size(34.dp)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 14.dp, bottom = 12.dp),
                color = Color.Black.copy(alpha = 0.14f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.width(92.dp)
                ) {


                    Image(
                        painter = painterResource(intensityDrawable),
                        contentDescription = intensity
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = intensity,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp
                    )
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(78.dp)
                        .padding(horizontal = 10.dp),
                    color = Color.Black.copy(alpha = 0.18f)
                )

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.Black
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = affectedAreas?.joinToString(", ")
                                ?: "Loading affected areas…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 20.sp
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.Black
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Valid till $validTill",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

