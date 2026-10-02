package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppRootNav( navController: NavHostController) {


    NavHost(
        navController = navController,
        startDestination = Graphs.Auth,
        enterTransition = { fadeIn(tween(100)) },
        exitTransition = { fadeOut(tween(100)) },
        popEnterTransition = { fadeIn(tween(100)) },
        popExitTransition = { fadeOut(tween(100)) }
    ) {
        mainNavGraph(navController = navController)
        authNavScreen(navController = navController)
    }
}