package com.example.disasterpreparednessapp

import android.app.Application
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.AlertNotifier
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.worker.DisasterWorkerScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DisasterManagementApplication : Application() {

    @Inject
    lateinit var alertNotifier: AlertNotifier

    override fun onCreate() {
        super.onCreate()
        alertNotifier.createNotificationChannel()
        DisasterWorkerScheduler.schedule(this)
    }
}
