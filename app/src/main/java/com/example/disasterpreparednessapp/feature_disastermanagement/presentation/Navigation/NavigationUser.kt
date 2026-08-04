package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.contact_screen.ContactScreen
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail.DisasterDetailScreen
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen.DisasterScreen
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.profilescreen.ProfileScreen

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation<Graphs.Main>(startDestination = Routes.Home) {
        composable<Routes.Home> { DisasterScreen(navController = navController) }
        composable<Routes.Contact> { ContactScreen(navController = navController) }
        composable<Routes.Profile> { ProfileScreen(navController = navController) }
        composable<Routes.DisasterDetail> { entry ->
            val route = entry.toRoute<Routes.DisasterDetail>()
            DisasterDetailScreen(alertId = route.alertId, onBack = navController::popBackStack)
        }
    }
}
