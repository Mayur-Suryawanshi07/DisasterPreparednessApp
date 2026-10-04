package com.example.disasterpreparednessapp.feature_notification.presentation.service

import android.util.Log
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.CapInfo
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.disaster.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.AlertNotifier
import com.example.disasterpreparednessapp.feature_notification.domain.use_cases.RegisterFcmTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DisasterFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var registerFcmTokenUseCase: RegisterFcmTokenUseCase

    @Inject
    lateinit var alertNotifier: AlertNotifier

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New FCM token generated: $token")
        serviceScope.launch {
            registerFcmTokenUseCase(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        val alertId = data["alertId"] ?: data["id"] ?: "fcm_${System.currentTimeMillis()}"
        val title = data["title"] ?: remoteMessage.notification?.title ?: "Disaster Alert"
        val body = data["body"] ?: remoteMessage.notification?.body ?: "Emergency alert update."
        val severity = data["severity"] ?: "High"
        val area = data["area"] ?: data["state"] ?: "India"
        val category = data["category"] ?: "General"

        val alert = DisasterAlert(
            id = alertId,
            title = title,
            category = category,
            detailUrl = data["detailUrl"],
            publishedAt = System.currentTimeMillis().toString(),
            author = data["author"] ?: "NDMA Sachet",
            description = body,
            capInfo = CapInfo(
                alertId = alertId,
                language = "en",
                event = title,
                severity = severity,
                certainty = "Observed",
                urgency = "Immediate",
                headline = title,
                description = body,
                instruction = data["instruction"],
                affectedAreas = if (area.isNotBlank()) listOf(area) else emptyList(),
                effective = null,
                expires = null
            )
        )

        alertNotifier.postNotification(alert)
    }
}
