package com.example.disasterpreparednessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.AppRootNav
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.DisasterManagmentAppTheme
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.util.AlertNotifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val initialAlertId = intent?.getStringExtra(AlertNotifier.EXTRA_ALERT_ID)

        setContent {
            DisasterManagmentAppTheme {
                val navController = rememberNavController()

                LaunchedEffect(initialAlertId) {
                    if (!initialAlertId.isNullOrBlank()) {
                        navController.navigate(Routes.Map(initialAlertId))
                    }
                }

                AppRootNav(navController = navController)
            }
        }
    }
}
