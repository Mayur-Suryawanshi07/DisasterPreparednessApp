package com.example.disasterpreparednessapp.feature_eonet_api.data.mapper

import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model.DisasterCategoryDto
import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model.DisasterEventDto
import com.example.disasterpreparednessapp.feature_eonet_api.data.remote.model.DisasterGeometryDto
import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterCategory
import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterEvent
import com.example.disasterpreparednessapp.feature_eonet_api.domain.model.DisasterGeometry

fun DisasterEventDto.toDomain(): DisasterEvent {
    return DisasterEvent(
        id = id,
        title = title,
        description = description,
        link = link,
        categories = categories.map { it.toDomain() },
        geometries = geometries.map { it.toDomain() }
    )
}

fun DisasterCategoryDto.toDomain(): DisasterCategory {
    return DisasterCategory(
        id = id,
        title = title
    )
}

fun DisasterGeometryDto.toDomain(): DisasterGeometry {
    return DisasterGeometry(
        date = date,
        coordinates = coordinates
    )
}

fun DisasterEvent.toDto(): DisasterEventDto {
    return DisasterEventDto(
        id = id,
        title = title,
        description = description,
        link = link,
        categories = categories.map { it.toDto() },
        geometries = geometries.map { it.toDto() }
    )
}

fun DisasterCategory.toDto(): DisasterCategoryDto {
    return DisasterCategoryDto(
        id = id,
        title = title
    )
}

fun DisasterGeometry.toDto(): DisasterGeometryDto {
    return DisasterGeometryDto(
        date = date,
        coordinates = coordinates
    )
}
