package com.example.disasterpreparednessapp.feature_location

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.notificationDataStore by preferencesDataStore(name = "notification_permission_prefs")

class NotificationPermissionPreferences(private val context: Context) {

    companion object {
        private val NOTIFICATION_PERMISSION_ASKED = booleanPreferencesKey("notification_permission_asked")
    }

    suspend fun hasAskedNotificationPermission(): Boolean {
        return context.notificationDataStore.data.map { prefs ->
            prefs[NOTIFICATION_PERMISSION_ASKED] ?: false
        }.first()
    }

    suspend fun markNotificationPermissionAsked() {
        context.notificationDataStore.edit { prefs ->
            prefs[NOTIFICATION_PERMISSION_ASKED] = true
        }
    }
}
