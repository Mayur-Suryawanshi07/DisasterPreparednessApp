package com.example.disasterpreparednessapp.feature_location

import android.content.Context

/** Stores completion of the optional location setup separately for every signed-in user. */
class LocationPromptPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun hasCompletedPrompt(userId: String): Boolean =
        preferences.getBoolean("$PROMPT_COMPLETED_PREFIX$userId", false)

    fun markPromptCompleted(userId: String) {
        preferences.edit().putBoolean("$PROMPT_COMPLETED_PREFIX$userId", true).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "location_setup"
        const val PROMPT_COMPLETED_PREFIX = "prompt_completed_"
    }
}
