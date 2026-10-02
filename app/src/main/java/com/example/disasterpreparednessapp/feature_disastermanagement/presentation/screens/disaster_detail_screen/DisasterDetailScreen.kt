package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.Canvas
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme.Ink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisasterDetailScreen(
    alertId: String,
    onBack: () -> Unit,
    viewModel: DisasterDetailScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(alertId) { viewModel.loadAlert(alertId) }

    Scaffold(
        containerColor = Canvas,
        topBar = {
            TopAppBar(
                title = { Text("Alert brief", fontWeight = FontWeight.Bold, color = Ink) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Back",
                            tint = Ink
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Canvas)
            )
        }
    ) { padding ->
        when (val value = state) {
            DisasterDetailState.Loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Ink) }

            is DisasterDetailState.Success -> DisasterDetailCard(
                alert = value.alert,
                modifier = Modifier.padding(padding),
                capInfo = value.capInfo
            )

            is DisasterDetailState.Error -> DetailError(
                value.message,
                Modifier.padding(padding),
                onBack
            )
        }
    }
}