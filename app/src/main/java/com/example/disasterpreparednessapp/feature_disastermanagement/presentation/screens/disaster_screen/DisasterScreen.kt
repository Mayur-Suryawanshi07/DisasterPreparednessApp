package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.R
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.MyBottomNavBar
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.MyTopAppBar
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AppTransparent
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.AppWhite
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.ErrorRed
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.weather_screen.WeatherScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisasterScreen(
    modifier: Modifier = Modifier,
    viewModel: DisasterScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }

    val filters = listOf("ALL INDIA", "WEATHER FORECAST")

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Active Alerts",
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.national_agency_logo),
                        contentDescription = "NDMA",
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                }
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Routes.RequestHelp)
                },
                containerColor = Color(0xFFD32F2F),
                contentColor = AppWhite,
                shape = CircleShape,
                icon = {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Request Help (SOS)"
                    )
                },
                text = {
                    Text("Request Help (SOS)", fontWeight = FontWeight.Bold)
                }
            )
        },

        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {
            MyBottomNavBar(navController = navController)
        }

    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                },
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                filters.forEachIndexed { index, filter ->
                    val selected = index == selectedTabIndex
                    Tab(
                        selected = selected,
                        onClick = {
                            selectedTabIndex = index
                            if (filter == "ALL INDIA") {
                                searchText = ""
                            }
                        },
                        text = {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
            }

            if (selectedTabIndex == 1) {
                WeatherScreen(
                    modifier = Modifier.weight(1f),
                    requestLocationOnStart = false
                )
            } else {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    placeholder = { Text("Search", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = AppTransparent,
                        unfocusedBorderColor = AppTransparent
                    )
                )

                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.refresh() },
                    state = rememberPullToRefreshState(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (val state = uiState) {
                        is DisasterScreenUiState.Loading -> {
                            LoadingState()
                        }

                        is DisasterScreenUiState.Success -> {
                            DisasterScreenCard(
                                events = state.events,
                                searchText = searchText,
                                navController = navController
                            )
                        }

                        is DisasterScreenUiState.Error -> {
                            ErrorState(
                                message = state.message,
                                onRetry = { viewModel.retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() = Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
}

@Composable
fun EmptyState() =
    Box(
        Modifier
            .fillMaxSize()
            .padding(32.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "All clear for now",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "No alerts match the current view.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) =
    Box(
        Modifier
            .fillMaxSize()
            .padding(32.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Unable to update alerts",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                message,
                textAlign = TextAlign.Center,
                color = ErrorRed
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("Try again") }
        }
    }
