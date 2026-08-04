package com.example.disasterpreparednessapp.feature_disastermanagement.data.repository

import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.api.DisasterApiService
import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model.DisasterEventDto
import com.example.disasterpreparednessapp.feature_eonet_api.data.repository.DisasterRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class DisasterRepositoryImplTest {

    @Mock
    private lateinit var mockApiService: DisasterApiService

    private lateinit var repository: DisasterRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = DisasterRepositoryImpl(mockApiService)
    }

    @Test
    fun `getDisasterEvents returns mapped domain models`() = runTest {
        // Given
        val mockDtos = listOf(
            DisasterEventDto(
                id = "1",
                title = "Test Disaster",
                description = "Test Description",
                link = "https://test.com",
                categories = emptyList(),
                geometries = emptyList()
            )
        )
        whenever(mockApiService.getDisasterEvents()).thenReturn(mockDtos)

        // When
        val result = repository.getDisasterEvents().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Test Disaster", result[0].title)
    }

    @Test
    fun `getDisasterEventById returns mapped domain model`() = runTest {
        // Given
        val mockDto = DisasterEventDto(
            id = "1",
            title = "Test Disaster",
            description = "Test Description",
            link = "https://test.com",
            categories = emptyList(),
            geometries = emptyList()
        )
        whenever(mockApiService.getDisasterEventById("1")).thenReturn(mockDto)

        // When
        val result = repository.getDisasterEventById("1")

        // Then
        assertEquals("1", result?.id)
        assertEquals("Test Disaster", result?.title)
    }

    @Test
    fun `getDisasterEventById returns null when API fails`() = runTest {
        // Given
        whenever(mockApiService.getDisasterEventById("1")).thenThrow(RuntimeException("API Error"))

        // When
        val result = repository.getDisasterEventById("1")

        // Then
        assertNull(result)
    }
}
