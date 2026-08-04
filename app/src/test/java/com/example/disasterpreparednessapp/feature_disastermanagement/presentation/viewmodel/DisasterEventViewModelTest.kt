package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.viewmodel

import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterEvent
import com.example.disasterpreparednessapp.feature_eonet_api.domain.use_cases.GetDisasterEventsUseCase
import com.example.disasterpreparednessapp.feature_eonet_api.domain.use_cases.SearchDisasterEventsUseCase
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen.state.DisasterScreenUiState
import com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen.viewmodel.DisasterScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DisasterScreenViewModelTest {

    @Mock
    private lateinit var mockGetDisasterEventsUseCase: GetDisasterEventsUseCase

    @Mock
    private lateinit var mockSearchDisasterEventsUseCase: SearchDisasterEventsUseCase

    private lateinit var viewModel: DisasterScreenViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel =
            DisasterScreenViewModel(mockGetDisasterEventsUseCase, mockSearchDisasterEventsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        // Then
        assertEquals(DisasterScreenUiState.Loading, viewModel.uiState.value.uiState)
    }

    @Test
    fun `loadDisasterEvents updates state to Success when events are returned`() = runTest {
        // Given
        val mockEvents = listOf(
            DisasterEvent(
                id = "1",
                title = "Test Disaster",
                description = "Test Description",
                link = "https://test.com",
                categories = emptyList(),
                geometries = emptyList()
            )
        )
        whenever(mockGetDisasterEventsUseCase()).thenReturn(flowOf(mockEvents))

        // When
        viewModel.loadDisasterEvents()
        testDispatcher.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value.uiState is DisasterScreenUiState.Success)
        val successState = viewModel.uiState.value.uiState as DisasterScreenUiState.Success
        assertEquals(mockEvents, successState.events)
    }

    @Test
    fun `loadDisasterEvents updates state to Empty when no events are returned`() = runTest {
        // Given
        whenever(mockGetDisasterEventsUseCase()).thenReturn(flowOf(emptyList()))

        // When
        viewModel.loadDisasterEvents()
        testDispatcher.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value.uiState is DisasterScreenUiState.Empty)
    }

    @Test
    fun `searchDisasterEvents updates search query and calls use case`() = runTest {
        // Given
        val searchQuery = "earthquake"
        val mockEvents = listOf(
            DisasterEvent(
                id = "1",
                title = "Earthquake",
                description = "Test Description",
                link = "https://test.com",
                categories = emptyList(),
                geometries = emptyList()
            )
        )
        whenever(mockSearchDisasterEventsUseCase(searchQuery)).thenReturn(flowOf(mockEvents))

        // When
        viewModel.searchDisasterEvents(searchQuery)
        testDispatcher.advanceUntilIdle()

        // Then
        assertEquals(searchQuery, viewModel.uiState.value.searchQuery)
        assertTrue(viewModel.uiState.value.uiState is DisasterScreenUiState.Success)
    }

    @Test
    fun `clearSearch resets search query and loads all events`() = runTest {
        // Given
        val mockEvents = listOf(
            DisasterEvent(
                id = "1",
                title = "Test Disaster",
                description = "Test Description",
                link = "https://test.com",
                categories = emptyList(),
                geometries = emptyList()
            )
        )
        whenever(mockGetDisasterEventsUseCase()).thenReturn(flowOf(mockEvents))

        // When
        viewModel.clearSearch()
        testDispatcher.advanceUntilIdle()

        // Then
        assertEquals("", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `retry calls loadDisasterEvents`() = runTest {
        // Given
        val mockEvents = listOf(
            DisasterEvent(
                id = "1",
                title = "Test Disaster",
                description = "Test Description",
                link = "https://test.com",
                categories = emptyList(),
                geometries = emptyList()
            )
        )
        whenever(mockGetDisasterEventsUseCase()).thenReturn(flowOf(mockEvents))

        // When
        viewModel.retry()
        testDispatcher.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value.uiState is DisasterScreenUiState.Success)
    }
}
