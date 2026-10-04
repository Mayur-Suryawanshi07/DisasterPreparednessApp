package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.disasterpreparednessapp.MainActivity
import com.example.disasterpreparednessapp.R
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "disaster_alerts"
        const val CHANNEL_NAME = "Disaster Alerts"
        const val EXTRA_ALERT_ID = "extra_alert_id"

        private val indianStates = listOf(
            "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
            "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
            "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
            "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
            "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
            "Delhi", "Jammu and Kashmir", "Ladakh", "Puducherry", "Andaman and Nicobar",
            "Chandigarh", "Dadra and Nagar Haveli", "Daman and Diu", "Lakshadweep"
        )
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High priority notifications for new disaster alerts in India"
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun postNotification(alert: DisasterAlert) {
        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_ALERT_ID, alert.id)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            alert.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val categoryOrTitle = alertDisplayName(alert.title, alert.capInfo?.event)
        val disasterType = when {
            categoryOrTitle.contains("flood", ignoreCase = true) -> "Flood"
            categoryOrTitle.contains("cyclone", ignoreCase = true) -> "Cyclone"
            categoryOrTitle.contains("rain", ignoreCase = true) -> "Heavy Rain"
            categoryOrTitle.contains("earthquake", ignoreCase = true) -> "Earthquake"
            categoryOrTitle.contains("fire", ignoreCase = true) -> "Fire"
            categoryOrTitle.contains("landslide", ignoreCase = true) -> "Landslide"
            else -> alert.category?.replaceFirstChar { it.uppercase() } ?: "Disaster"
        }

        val notificationTitle = "$disasterType Alert"

        // Extract State Name or default to "India"
        val fullText = "${alert.title} ${alert.capInfo?.affectedAreas?.joinToString(" ")} ${alert.description}"
        val detectedState = indianStates.firstOrNull { state ->
            fullText.contains(state, ignoreCase = true)
        } ?: "India"

        val shortDescription = alert.description?.takeIf { it.isNotBlank() }
            ?: alert.title.takeIf { it.isNotBlank() }
            ?: "New disaster alert issued."
        val notificationBody = "$detectedState: $shortDescription"

        val largeIconRes = getDisasterLargeIconRes(categoryOrTitle)
        val largeIconBitmap = BitmapFactory.decodeResource(context.resources, largeIconRes)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setLargeIcon(largeIconBitmap)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationBody))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(alert.id.hashCode(), notification)
        } catch (_: SecurityException) {
            // Permission denied on runtime check
        }
    }
}
