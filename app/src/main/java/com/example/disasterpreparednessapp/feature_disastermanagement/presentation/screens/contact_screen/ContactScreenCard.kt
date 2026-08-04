package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.contact_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract.Contacts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ContactScreenCard(img: Int, name: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(4.dp),
        onClick = {
            EmergencyCallContact(name, context)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = img),
                contentDescription = name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.FillHeight
            )
            Text(
                text = name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}



private fun EmergencyCallContact(name: String, context: Context) {
    when (name) {
        "Call Police", "Call Ambulance", "Call Fire Service" ->
            makeCall(context, getPhoneNumber(name))

        "Medical tips" -> {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.ready.gov/home-fires")
            )
            context.startActivity(intent)

        }

        "Check Weather" -> {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=current+weather&oq=current+weather+&gs_lcrp=EgZjaHJvbWUyBggAEEUYOdIBBzI1NGowajeoAg-wAgE&client=ms-android-xiaomi-rvo2b&sourceid=chrome-mobile&ie=UTF-8")
            )
            context.startActivity(intent)

        }

        "First Aid Instruction" -> {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.verywellhealth.com/basic-first-aid-procedures-1298578")
            )
            context.startActivity(intent)

        }

        "Safety tips" -> {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://ddma.delhi.gov.in/ddma/safety-tips-1")
            )
            context.startActivity(intent)
        }

        "Call Doctor" -> {
            val getNumber = Intent(Intent.ACTION_VIEW, Uri.parse(Contacts.CONTENT_URI.toString()))
            context.startActivity(getNumber)
        }

        "Report A Disaster" -> {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.ndma.gov.in/Response/Emergency-Operations-Center")
            )
            context.startActivity(intent)
        }

        "National Disaster Management Authority" -> {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ndma.gov.in/"))
            context.startActivity(intent)
        }

    }
}