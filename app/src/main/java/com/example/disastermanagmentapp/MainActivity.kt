package com.example.disastermanagmentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.disastermanagmentapp.feature_disastermanagement.presentation.Navigation.AppRootNav
import com.example.disastermanagmentapp.feature_disastermanagement.presentation.theme.DisasterManagmentAppTheme
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