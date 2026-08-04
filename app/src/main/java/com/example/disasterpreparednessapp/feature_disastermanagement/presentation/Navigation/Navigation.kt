package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.loginscreen.LogInScreen
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.auth.signupscreen.SignUpScreen


fun NavGraphBuilder.authNavScreen( navController: NavHostController) {
    navigation<Graphs.Auth>(startDestination = Routes.Login){
        composable<Routes.Login> {
            LogInScreen(navController = navController)
        }
        composable<Routes.Signup> {
            SignUpScreen(navController = navController)
        }

    }
}

