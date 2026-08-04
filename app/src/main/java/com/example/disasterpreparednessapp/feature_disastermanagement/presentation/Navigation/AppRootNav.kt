package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppRootNav( navController: NavHostController) {


    NavHost(
        navController = navController,
        startDestination = Graphs.Auth
    ) {
        mainNavGraph(navController = navController)
        authNavScreen(navController = navController)
    }
}