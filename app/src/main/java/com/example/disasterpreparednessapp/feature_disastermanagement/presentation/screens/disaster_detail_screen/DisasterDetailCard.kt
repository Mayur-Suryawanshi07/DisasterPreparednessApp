package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail_screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.Ink
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.formatIssuedBy
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.alertSeverityBackground

@Composable
fun DisasterDetailCard(
    alert: DisasterAlert,
    modifier: Modifier = Modifier,
    // Nullable: the CAP document loads after the RSS list, so this screen
    // can render with just `alert` first and fill in the rest once it arrives.
    capInfo: CapInfo?
) {
    val context = LocalContext.current

    // event/description have a real RSS-level fallback (title, Item.description).
    // severity/affectedAreas/expires do not — alert never had those fields to
    // begin with, so they stay null (→ "loading" text) until capInfo arrives.
    val event = capInfo?.event?.takeIf { it.isNotBlank() } ?: alert.title
    val description = capInfo?.description?.takeIf { it.isNotBlank() }
        ?: alert.description?.takeIf { it.isNotBlank() }
        ?: "Official details for this alert are currently being updated."
    val affectedAreas = capInfo?.affectedAreas?.takeIf { it.isNotEmpty() }
    val severity = capInfo?.severity
    // CAP "effective" is when the warning takes effect — distinct from the
    // RSS publish date. Prefer it, fall back to publish date only while
    // capInfo hasn't loaded yet.
    val effectiveText = capInfo?.effective ?: alert.publishedAt
    val expiresText = capInfo?.expires

    val bgColor = alertSeverityBackground(
        title = alert.title,
        category = alert.category,
        severity = severity,
        urgency = capInfo?.urgency
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            // Header Yellow Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "$event Alert",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Issued By ${formatIssuedBy(alert.author)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Effective: ${effectiveText ?: "--"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.Black.copy(alpha = 0.1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(70.dp)
                        ) {
                            Icon(Icons.Default.Speed, null, tint = Color.Black, modifier = Modifier.size(24.dp))
                            Text(
                                text = severity ?: "Pending",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Intensity",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Black.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.Black
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = affectedAreas?.joinToString() ?: "Loading affected areas…",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.Black
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Expiry: ${expiresText ?: "--"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = description,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                lineHeight = 22.sp
            )
        }

        // Bottom Action Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = bgColor,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomActionItem(Icons.Outlined.Share, "Share") {
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "${alert.title}\n\n$description")
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share alert"))
                }
                // TODO: wire this up to capInfo?.instruction once a Dos & Don'ts
                // destination exists — that's exactly the field it maps to.
                BottomActionItem(Icons.AutoMirrored.Outlined.MenuBook, "Dos & Don'ts") {}
            }
        }
    }
}

@Composable
fun BottomActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = label, tint = Color.Black, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Black, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun DetailError(message: String, modifier: Modifier, onBack: () -> Unit) =
    Box(
        modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Alert unavailable",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Spacer(Modifier.height(8.dp))
            Text(message, color = Color.Gray)
            Spacer(Modifier.height(20.dp))
            Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = Ink)) { Text("Back to alerts") }
        }
    }
