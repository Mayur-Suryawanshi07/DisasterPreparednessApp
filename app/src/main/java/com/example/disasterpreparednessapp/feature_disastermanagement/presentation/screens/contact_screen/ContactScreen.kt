package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.contact_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.MyBottomNavBar
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.component.MyTopAppBar

@Composable
fun ContactScreen(
    viewModel: ContactScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (modifier = Modifier
        .fillMaxSize(),
        topBar = {
            MyTopAppBar("Contact List")
        },
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }


    ){
        val state = rememberLazyGridState()
        Column(
            modifier = Modifier
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            LazyVerticalGrid(
                state=state,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)

            ) {
                items(uiState.emergencyContacts) { contact ->
                    ContactScreenCard(contact.imageResId, contact.name)
                }
            }
        }
    }
}


fun getPhoneNumber(name: String): String =
    when (name) {
        "Call Police" -> "100"
        "Call Doctor" -> " "
        "Call Ambulance" -> "102"
        "Call Fire Service" -> "101"
        else -> ""
    }

fun makeCall(context: Context, phoneNumber: String) {
    if (phoneNumber.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        context.startActivity(intent)
    }
}
