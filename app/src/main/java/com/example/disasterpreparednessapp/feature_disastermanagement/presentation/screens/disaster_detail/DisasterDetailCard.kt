package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WarningAmber
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
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert
import kotlin.text.isNotBlank

@Composable
fun AlertBrief(alert: DisasterAlert, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val type = alert.category?.takeIf { it.isNotBlank() } ?: "Emergency advisory"
    val description = alert.description.ifBlank { "Official details for this alert are currently being updated." }

    Column(
        modifier.fillMaxSize().
        verticalScroll(rememberScrollState()).
        padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp))
    {
        Surface(shape = RoundedCornerShape(26.dp), color = AlertGold, tonalElevation = 0.dp) {

            Column(Modifier.padding(20.dp)) {

                Row(verticalAlignment = Alignment.Top) {

                    Column(Modifier.weight(1f)) {

                        Text("ACTIVE â€¢ $type".uppercase(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold, color = DetailInk)
                        Spacer(Modifier.height(8.dp))
                        Text(alert.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF111B27))
                    }
                    Box(Modifier.size(48.dp).background(Color.White.copy(alpha = .42f), RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.WarningAmber, null, tint = DetailInk) }
                }
                Spacer(Modifier.height(18.dp)); HorizontalDivider(color = DetailInk.copy(alpha = .16f)); Spacer(Modifier.height(14.dp))
                DetailMeta("Published", alert.pubDate)
                Spacer(Modifier.height(8.dp)); DetailMeta("Status", "Official alert")
            }
        }

        SectionCard(title = "What is happening", icon = Icons.Default.Info) {
            Text(description, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF314354))
        }
        SectionCard(title = "Affected area", icon = Icons.Default.LocationOn) {
            Text(alert.affectedAreas.takeIf { it.isNotEmpty() }?.joinToString() ?: "Affected locations were not provided in this alert.", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF314354))
        }
        SectionCard(title = "Recommended actions", icon = Icons.Default.WarningAmber) {
            GuidanceRow("Stay aware", "Monitor official local updates and weather conditions.")
            GuidanceRow("Plan ahead", "Avoid unnecessary travel through affected areas.")
            GuidanceRow("Ask for help", "Contact local emergency services if you are in immediate danger.")
        }
        Button(
            onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply { this.type = "text/plain"; putExtra(Intent.EXTRA_TEXT, "${alert.title}\n\n$description") }
                context.startActivity(Intent.createChooser(sendIntent, "Share alert"))
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DetailInk)
        ) { Icon(Icons.Default.Share, null); Spacer(Modifier.width(8.dp)); Text("Share this alert") }
    }
}

@Composable
fun DetailMeta(label: String, value: String) = Row {
    Text(label, modifier = Modifier.width(82.dp), style = MaterialTheme.typography.labelLarge, color = DetailInk.copy(alpha = .68f))
    Text(value, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = DetailInk, maxLines = 2, overflow = TextOverflow.Ellipsis)
}

@Composable
fun SectionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(Modifier.padding(18.dp), content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(36.dp).background(Color(0xFFE8F0F7), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = DetailInk, modifier = Modifier.size(20.dp)) }
                Spacer(Modifier.width(10.dp)); Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = DetailInk)
            }
            Spacer(Modifier.height(14.dp)); content()
        })
    }
}

@Composable
fun GuidanceRow(title: String, text: String) {
    Row(Modifier.padding(bottom = 12.dp)) {
        Box(Modifier.padding(top = 7.dp).size(7.dp).background(AlertGold, RoundedCornerShape(50)))
        Spacer(Modifier.width(10.dp)); Column { Text(title, fontWeight = FontWeight.Bold, color = DetailInk); Text(text, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF526373)) }
    }
}

fun extractArea(description: String): String = description.substringAfter("over ", description).substringBefore(" in next").ifBlank { "See the official alert description for affected locations." }

@Composable
fun DetailError(message: String, modifier: Modifier, onBack: () -> Unit) = Box(modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Alert unavailable", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = DetailInk); Spacer(Modifier.height(8.dp)); Text(message, color = Color(0xFF526373)); Spacer(Modifier.height(20.dp)); Button(onClick = onBack) { Text("Back to alerts") } }
}
