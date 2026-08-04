package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert

val DetailInk = Color(0xFF10243B)
private val DetailCanvas = Color(0xFFF5F7FA)
val AlertGold = Color(0xFFFFC234)

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
        containerColor = DetailCanvas,
        topBar = {
            TopAppBar(
                title = { Text("Alert brief", fontWeight = FontWeight.Bold, color = DetailInk) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = DetailInk) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DetailCanvas)
            )
        }
    ) { padding ->
        when (val value = state) {
            DisasterDetailState.Loading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = DetailInk) }
            is DisasterDetailState.Success -> AlertBrief(value.alert, Modifier.padding(padding))
            is DisasterDetailState.Error -> DetailError(value.message, Modifier.padding(padding), onBack)
        }
    }
}



