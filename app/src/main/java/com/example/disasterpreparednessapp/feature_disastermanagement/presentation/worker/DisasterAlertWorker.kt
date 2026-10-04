package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.disaster.CheckNewAlertsUseCase
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.AlertNotifier
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class DisasterAlertWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerEntryPoint {
        fun checkNewAlertsUseCase(): CheckNewAlertsUseCase
        fun alertNotifier(): AlertNotifier
    }

    override suspend fun doWork(): Result {
        return try {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                WorkerEntryPoint::class.java
            )
            val checkNewAlertsUseCase = entryPoint.checkNewAlertsUseCase()
            val alertNotifier = entryPoint.alertNotifier()

            val newAlerts = checkNewAlertsUseCase()
            newAlerts.forEach { alert ->
                alertNotifier.postNotification(alert)
            }
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}
