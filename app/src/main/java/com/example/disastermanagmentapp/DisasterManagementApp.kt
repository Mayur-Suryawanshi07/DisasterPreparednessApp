package com.example.disastermanagmentapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.disastermanagmentapp.feature_disastermanagement.presentation.Navigation.AppRootNav

@Composable
fun DisasterManagementApp(){
    val navController = rememberNavController()
    AppRootNav(navController)
}