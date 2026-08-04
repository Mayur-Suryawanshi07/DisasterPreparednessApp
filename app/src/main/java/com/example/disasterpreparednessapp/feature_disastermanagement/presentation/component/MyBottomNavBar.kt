package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ContactEmergency
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ContactEmergency
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Graphs
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes

@Composable
fun MyBottomNavBar(
    navController: NavHostController,
    items: List<NavigationBottomTheme> = defaultBottomNavItems()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route::class.qualifiedName } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route::class.qualifiedName!!) {
                        popUpTo(Graphs.Main) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true


                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.SelectedItems else item.UnSelectedItems,
                        contentDescription = item.route::class.simpleName
                    )
                }
            )
        }
    }
}

data class NavigationBottomTheme(
    val SelectedItems: ImageVector,
    val UnSelectedItems: ImageVector,
    val route: Routes
)

fun defaultBottomNavItems() = listOf(
    NavigationBottomTheme(
        Icons.Filled.Home,
        Icons.Outlined.Home,
        route = Routes.Home
    ),
    NavigationBottomTheme(
        Icons.Filled.ContactEmergency,
        Icons.Outlined.ContactEmergency,
        route = Routes.Contact
    ),
    NavigationBottomTheme(
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle,
        route = Routes.Profile
    )
)