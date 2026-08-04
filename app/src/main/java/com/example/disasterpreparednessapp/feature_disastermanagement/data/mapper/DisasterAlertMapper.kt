package com.example.disasterpreparednessapp.feature_disastermanagement.data.mapper

import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.dto.CapAlert
import com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.dto.Item
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.model.DisasterAlert
import java.util.UUID

object DisasterAlertMapper {
    fun mapToDomain(item: Item): DisasterAlert = DisasterAlert(
        id = item.guid?.trim()?.takeIf { it.isNotBlank() }
            ?: item.link?.trim()?.takeIf { it.isNotBlank() }
            ?: item.title?.hashCode()?.toString()
            ?: UUID.randomUUID().toString(),
        title = item.title?.trim().orEmpty().ifBlank { "Official disaster alert" },
        link = item.link.orEmpty(),
        description = cleanText(item.description.orEmpty()),
        pubDate = item.pubDate.orEmpty(),
        author = item.author.orEmpty().replace("controlroom@ndma.gov.in", "").trim(),
        category = item.category?.trim()?.takeIf { it.isNotBlank() } ?: extractCategory(item.title)
    )

    fun mapToDomainList(items: List<Item>?): List<DisasterAlert> = items.orEmpty().map(::mapToDomain)

    fun enrichWithCap(alert: DisasterAlert, capAlert: CapAlert): DisasterAlert {
        val info = capAlert.infos?.firstOrNull { it.language.equals("en-IN", true) }
            ?: capAlert.infos?.firstOrNull()
            ?: return alert
        val description = cleanText(info.description.orEmpty())
        return alert.copy(
            title = info.headline?.trim()?.takeIf { it.isNotBlank() } ?: alert.title,
            description = description.ifBlank { alert.description },
            category = info.category?.trim()?.takeIf { it.isNotBlank() } ?: alert.category,
            event = info.event?.trim(), urgency = info.urgency?.trim(), severity = info.severity?.trim(),
            certainty = info.certainty?.trim(), effective = info.effective?.trim(), onset = info.onset?.trim(),
            expires = info.expires?.trim(), instruction = cleanText(info.instruction.orEmpty()).ifBlank { null },
            affectedAreas = info.areas.orEmpty().mapNotNull { it.areaDesc?.trim()?.takeIf(String::isNotBlank) },
            sender = capAlert.sender?.trim(), status = capAlert.status?.trim(),
            polygonUrl = info.parameters.orEmpty().firstOrNull {
                it.valueName.equals("Polygon URL", true)
            }?.value?.trim()
        )
    }

    private fun cleanText(value: String) = value.replace(Regex("<[^>]*>"), "").replace(Regex("&[a-zA-Z0-9#]+;"), " ").trim()

    private fun extractCategory(title: String?): String? = when {
        title.isNullOrBlank() -> null
        title.contains(Regex("rain|thunder|storm|weather|lightning", RegexOption.IGNORE_CASE)) -> "Weather"
        title.contains(Regex("flood|river|water level", RegexOption.IGNORE_CASE)) -> "Flood"
        title.contains(Regex("earthquake|tremor", RegexOption.IGNORE_CASE)) -> "Earthquake"
        title.contains(Regex("heat|temperature", RegexOption.IGNORE_CASE)) -> "Heat wave"
        title.contains(Regex("cyclone|hurricane", RegexOption.IGNORE_CASE)) -> "Cyclone"
        title.contains(Regex("landslide|mudslide", RegexOption.IGNORE_CASE)) -> "Landslide"
        else -> "Safety"
    }
}
