package com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases

import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterEvent
import com.example.disasterpreparednessapp.feature_eonet_api.domain.repository.DisasterRepository
import com.example.disasterpreparednessapp.feature_eonet_api.domain.use_cases.GetDisasterEventsUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetDisasterEventsUseCaseTest {

    @Mock
    private lateinit var mockRepository: DisasterRepository

    private lateinit var useCase: GetDisasterEventsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetDisasterEventsUseCase(mockRepository)
    }

    @Test
    fun `invoke returns disaster events from repository`() = runTest {
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
        whenever(mockRepository.getDisasterEvents()).thenReturn(flowOf(mockEvents))

        // When
        val result = useCase.invoke()

        // Then
        result.collect { events ->
            assertEquals(mockEvents, events)
        }
    }
}
