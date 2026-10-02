package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.contact_screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.MyBottomNavBar
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.MyTopAppBar

@Composable
fun ContactScreen(
    navController: NavHostController,
    contactViewModel: ContactScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by contactViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MyTopAppBar("Contact List")
        },
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Single emergency dial card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        makeCall(context, "112")
                    },

                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dial 112 for Emergency",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFE53935), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Call 112",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = rememberLazyGridState()
            ) {
                items(uiState.emergencyContacts) { contact ->
                    ContactScreenCard(
                        contact = contact,
                        onClick = {
                            handleContactAction(context, contact)
                        }
                    )
                }
            }
        }
    }
}

fun handleContactAction(context: Context, contact: EmergencyContact) {
    when (contact.actionType) {
        ContactActionType.PHONE_CALL -> {
            contact.phoneNumber?.let { makeCall(context, it) }
        }
        ContactActionType.OPEN_NDMA -> {
            openUrl(context, "https://www.ndma.gov.in/")
        }
        ContactActionType.OPEN_WEATHER -> {
            openUrl(context, "https://mausam.imd.gov.in/")
        }
        ContactActionType.OPEN_FIRST_AID -> {
            openUrl(context, "https://www.redcross.org/get-help/how-to-prepare-for-emergencies/1-step-first-aid.html")
        }
        ContactActionType.OPEN_SAFETY_TIPS -> {
            openUrl(context, "https://www.ready.gov/safety-skills")
        }
        ContactActionType.OPEN_REPORT -> {
            openUrl(context, "https://ndmaindia.home.blog/contact-us/")
        }
        else -> {}
    }
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

fun makeCall(context: Context, phoneNumber: String) {
    if (phoneNumber.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_DIAL, "tel:$phoneNumber".toUri())
        context.startActivity(intent)
    }
}
