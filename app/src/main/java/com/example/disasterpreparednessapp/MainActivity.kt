package com.example.disasterpreparednessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.DisasterManagmentAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            DisasterManagmentAppTheme() {
                DisasterManagementApp()
            }
        }
    }
}