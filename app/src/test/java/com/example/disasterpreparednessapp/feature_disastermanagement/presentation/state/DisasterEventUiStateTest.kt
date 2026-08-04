package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.state

import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterEvent
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen.state.DisasterScreenState
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen.state.DisasterScreenUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DisasterScreenUiStateTest {

    @Test
    fun `DisasterScreenState has correct default values`() {
        // When
        val state = DisasterScreenState()

        // Then
        assertTrue(state.uiState is DisasterScreenUiState.Loading)
        assertEquals("", state.searchQuery)
        assertEquals(null, state.selectedCategory)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun `DisasterScreenState copy works correctly`() {
        // Given
        val originalState = DisasterScreenState()
        val events = listOf(
            DisasterEvent(
                id = "1",
                title = "Test Disaster",
                description = "Test Description",
                link = "https://test.com",
                categories = emptyList(),
                geometries = emptyList()
            )
        )

        // When
        val updatedState = originalState.copy(
            uiState = DisasterScreenUiState.Success(events),
            searchQuery = "earthquake",
            selectedCategory = "severe-storms"
        )

        // Then
        assertTrue(updatedState.uiState is DisasterScreenUiState.Success)
        assertEquals("earthquake", updatedState.searchQuery)
        assertEquals("severe-storms", updatedState.selectedCategory)
        assertFalse(updatedState.isRefreshing)
    }

    @Test
    fun `DisasterScreenUiState Success contains correct events`() {
        // Given
        val events = listOf(
            DisasterEvent(
                id = "1",
                title = "Test Disaster",
                description = "Test Description",
                link = "https://test.com",
                categories = emptyList(),
                geometries = emptyList()
            )
        )

        // When
        val successState = DisasterScreenUiState.Success(events)

        // Then
        assertEquals(events, successState.events)
    }

    @Test
    fun `DisasterScreenUiState Error contains correct message`() {
        // Given
        val errorMessage = "Network error occurred"

        // When
        val errorState = DisasterScreenUiState.Error(errorMessage)

        // Then
        assertEquals(errorMessage, errorState.message)
    }
}
