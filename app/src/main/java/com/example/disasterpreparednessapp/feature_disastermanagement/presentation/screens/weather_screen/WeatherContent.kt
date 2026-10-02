package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.weather_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.weather.WeatherResponse

@Composable
fun WeatherSuccessContent(weather: WeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = weather.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${weather.main.temp.toInt()}°C",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = weather.weather.firstOrNull()?.description ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                val iconCode = weather.weather.firstOrNull()?.icon
                if (iconCode != null) {
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/$iconCode@4x.png",
                        contentDescription = "Weather Icon",
                        modifier = Modifier.height(120.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val items = listOf(
                WeatherItem("Humidity", "${weather.main.humidity}%", Icons.Default.WaterDrop),
                WeatherItem("Wind", "${weather.wind.speed} m/s", Icons.Default.WindPower),
                WeatherItem("Pressure", "${weather.main.pressure} hPa", Icons.Default.Cloud),
                WeatherItem("Feels Like", "${weather.main.feels_like.toInt()}°C", Icons.Default.DeviceThermostat)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(220.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items.size) { index ->
                    WeatherCard(item = items[index])
                }
            }
        }
    }
}

data class WeatherItem(
    val label: String,
    val value: String,
    val icon: ImageVector
)

@Composable
fun WeatherCard(item: WeatherItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = item.icon, contentDescription = item.label)
            Spacer(Modifier.height(4.dp))
            Text(item.label, style = MaterialTheme.typography.labelMedium)
            Text(item.value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}