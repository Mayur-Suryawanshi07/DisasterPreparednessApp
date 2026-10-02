package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.contact_screen.ContactScreen
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail_screen.DisasterDetailScreen
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen.DisasterScreen
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.profilescreen.ProfileScreen
import com.example.disasterpreparednessapp.feature_map.presentaion.LocationScreen
import com.example.disasterpreparednessapp.feature_map.presentaion.RequestHelpScreen

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation<Graphs.Main>(startDestination = Routes.Home) {
        composable<Routes.Home> { DisasterScreen(navController = navController) }
        composable<Routes.Map> { entry ->
            val route = entry.toRoute<Routes.Map>()
            LocationScreen(navController = navController, alertId = route.alertId)
        }
        composable<Routes.RequestHelp> { RequestHelpScreen(navController = navController) }
        composable<Routes.Contact> { ContactScreen(navController = navController) }
        composable<Routes.Profile> { ProfileScreen(navController = navController) }
        composable<Routes.DisasterDetail> { entry ->
            val route = entry.toRoute<Routes.DisasterDetail>()
            DisasterDetailScreen(alertId = route.alertId, onBack = navController::popBackStack)
        }
    }
}
