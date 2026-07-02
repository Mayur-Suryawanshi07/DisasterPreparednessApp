package com.example.disastermanagmentapp.feature_disastermanagement.presentation.Navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    @Serializable
    data object Login : Routes

    @Serializable
    data object Signup : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object Contact : Routes

    @Serializable
    data object Profile : Routes
}

@Serializable
sealed interface Graphs {
    @Serializable
    data object Auth : Graphs

    @Serializable
    data object Main : Graphs
}
