package com.example.disasterpreparednessapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.AppRootNav

@Composable
fun DisasterManagementApp(){
    val navController = rememberNavController()
    AppRootNav(navController)
}