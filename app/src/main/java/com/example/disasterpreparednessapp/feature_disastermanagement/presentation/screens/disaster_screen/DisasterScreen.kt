package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.MyBottomNavBar
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.Navigation.Routes
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.Canvas
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.Ink
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.goldenYellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisasterScreen(
    viewModel: DisasterScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }
    val filters = listOf("All alerts", "Weather", "Flood", "Safety")

    Scaffold(
        containerColor = Canvas,
        bottomBar = { MyBottomNavBar(navController = navController) }) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlertHeader(uiState.isRefreshing, viewModel::refreshEvents)
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                placeholder = { Text("Search alerts, regions or hazards") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Ink,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val selected =
                        uiState.searchQuery == filter || (filter == "All alerts" && uiState.searchQuery.isBlank())
                    FilterChip(
                        selected = selected,
                        onClick = {
                            if (filter == "All alerts") viewModel.clearSearch() else viewModel.searchDisasterEvents(
                                filter
                            )
                        },
                        label = { Text(filter) }, shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Ink,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            when (val state = uiState.uiState) {
                is DisasterScreenUiState.Loading -> LoadingState()
                is DisasterScreenUiState.Success -> DisasterEventList(
                    state.events,
                    searchText,
                    navController
                )

                is DisasterScreenUiState.Error -> ErrorState(state.message, viewModel::retry)
                is DisasterScreenUiState.Empty -> EmptyState()
            }
        }
    }
}

@Composable
private fun AlertHeader(isRefreshing: Boolean, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Ink)
            .padding(start = 20.dp, end = 12.dp, top = 20.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = goldenYellow,
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.size(46.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.NotificationsActive,
                    null,
                    tint = Ink
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "EMERGENCY WATCH",
                color = Color(0xFFB8C7D8),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Active alerts",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun LoadingState() = Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    CircularProgressIndicator(color = Ink)
}

@Composable
private fun EmptyState() =
    Box(Modifier
        .fillMaxSize()
        .padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "All clear for now",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "No alerts match the current view.",
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
        }
    }

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) =
    Box(Modifier
        .fillMaxSize()
        .padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Unable to update alerts",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp)); Text(
            message,
            textAlign = TextAlign.Center,
            color = Color.Red
        )
            Spacer(Modifier.height(16.dp)); Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Ink)
        ) { Text("Try again") }
        }
    }


@Composable
private fun DisasterEventList(
    events: List<DisasterAlert>,
    searchText: String,
    navController: NavHostController
) {
    val visibleEvents = events.filter { event ->
        searchText.isBlank() || "${event.title} ${event.description} ${event.category.orEmpty()}".contains(
            searchText,
            ignoreCase = true
        )
    }
    if (visibleEvents.isEmpty()) {
        EmptyState(); return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            visibleEvents,
            key = { it.id }) { event ->
            DisasterScreenEventCard(event) {
                navController.navigate(
                    Routes.DisasterDetail(
                        event.id
                    )
                )
            }
        }
    }
}



