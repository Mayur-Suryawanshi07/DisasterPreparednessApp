package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ContactEmergency
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ContactEmergency
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Graphs
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes

import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AppWhite
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.InkMuted

@Composable
fun MyBottomNavBar(
    navController: NavHostController,
    items: List<NavigationBottomTheme> = defaultBottomNavItems()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = Color(0xFF002F6C), // Matches screenshot dark blue
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true

            NavigationBarItem(
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.2f)
                ),
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(Graphs.Main) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Crossfade(
                        targetState = selected,
                        animationSpec = tween(durationMillis = 200),
                        label = "navIconCrossfade"
                    ) { isSelected ->
                        Icon(
                            imageVector = if (isSelected) item.SelectedItems else item.UnSelectedItems,
                            contentDescription = item.route::class.simpleName
                        )
                    }
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
