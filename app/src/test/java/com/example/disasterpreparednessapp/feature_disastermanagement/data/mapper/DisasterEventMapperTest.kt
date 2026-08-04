package com.example.disasterpreparednessapp.feature_disastermanagement.data.mapper

import com.example.disasterpreparednessapp.feature_eonet_api.data.mapper.toDomain
import com.example.disasterpreparednessapp.feature_eonet_api.data.mapper.toDto
import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model.DisasterCategoryDto
import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model.DisasterEventDto
import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model.DisasterGeometryDto
import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterCategory
import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterEvent
import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterGeometry
import org.junit.Assert.assertEquals
import org.junit.Test

class DisasterEventMapperTest {

    @Test
    fun `toDomain converts DTO to domain model correctly`() {
        // Given
        val dto = DisasterEventDto(
            id = "test-id",
            title = "Test Disaster",
            description = "Test Description",
            link = "https://test.com",
            categories = listOf(
                DisasterCategoryDto("cat1", "Earthquake")
            ),
            geometries = listOf(
                DisasterGeometryDto("2024-01-01T00:00:00Z", listOf(0.0, 0.0))
            )
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.title, domain.title)
        assertEquals(dto.description, domain.description)
        assertEquals(dto.link, domain.link)
        assertEquals(1, domain.categories.size)
        assertEquals("cat1", domain.categories[0].id)
        assertEquals("Earthquake", domain.categories[0].title)
        assertEquals(1, domain.geometries.size)
        assertEquals("2024-01-01T00:00:00Z", domain.geometries[0].date)
        assertEquals(listOf(0.0, 0.0), domain.geometries[0].coordinates)
    }

    @Test
    fun `toDto converts domain model to DTO correctly`() {
        // Given
        val domain = DisasterEvent(
            id = "test-id",
            title = "Test Disaster",
            description = "Test Description",
            link = "https://test.com",
            categories = listOf(
                DisasterCategory("cat1", "Earthquake")
            ),
            geometries = listOf(
                DisasterGeometry("2024-01-01T00:00:00Z", listOf(0.0, 0.0))
            )
        )

        // When
        val dto = domain.toDto()

        // Then
        assertEquals(domain.id, dto.id)
        assertEquals(domain.title, dto.title)
        assertEquals(domain.description, dto.description)
        assertEquals(domain.link, dto.link)
        assertEquals(1, dto.categories.size)
        assertEquals("cat1", dto.categories[0].id)
        assertEquals("Earthquake", dto.categories[0].title)
        assertEquals(1, dto.geometries.size)
        assertEquals("2024-01-01T00:00:00Z", dto.geometries[0].date)
        assertEquals(listOf(0.0, 0.0), dto.geometries[0].coordinates)
    }
}
