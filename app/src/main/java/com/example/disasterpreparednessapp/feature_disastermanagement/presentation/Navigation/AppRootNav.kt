package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppRootNav(navController: NavHostController) {
    val isLoggedIn = remember { FirebaseAuth.getInstance().currentUser != null }
    val initialDestination = if (isLoggedIn) Graphs.Main else Graphs.Auth

    NavHost(
        navController = navController,
        startDestination = initialDestination,
        enterTransition = { fadeIn(tween(100)) },
        exitTransition = { fadeOut(tween(100)) },
        popEnterTransition = { fadeIn(tween(100)) },
        popExitTransition = { fadeOut(tween(100)) }
    ) {
        mainNavGraph(navController = navController)
        authNavScreen(navController = navController)
    }
}
