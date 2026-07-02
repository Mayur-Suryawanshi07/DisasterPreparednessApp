package com.example.disastermanagmentapp.feature_disastermanagement.presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

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