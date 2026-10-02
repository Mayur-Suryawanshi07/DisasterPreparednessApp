package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.cap

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "info", strict = false)
data class CapInfoDto @JvmOverloads constructor(

    @field:Element(name = "language", required = false)
    var language: String? = null,

    @field:Element(name = "category", required = false)
    var category: String? = null,

    @field:Element(name = "event", required = false)
    var event: String? = null,

    @field:Element(name = "urgency", required = false)
    var urgency: String? = null,

    @field:Element(name = "severity", required = false)
    var severity: String? = null,

    @field:Element(name = "certainty", required = false)
    var certainty: String? = null,

    // NEW: seen as "1" in the payload — kept as String since its meaning/format isn't documented in-feed
    @field:Element(name = "audience", required = false)
    var audience: String? = null,

    @field:Element(name = "effective", required = false)
    var effective: String? = null,

    @field:Element(name = "onset", required = false)
    var onset: String? = null,

    @field:Element(name = "expires", required = false)
    var expires: String? = null,

    @field:Element(name = "headline", required = false)
    var headline: String? = null,

    @field:Element(name = "description", required = false)
    var description: String? = null,

    @field:Element(name = "instruction", required = false)
    var instruction: String? = null,

    @field:ElementList(entry = "area", inline = true, required = false)
    var areas: List<CapArea>? = null,

    @field:ElementList(entry = "parameter", inline = true, required = false)
    var parameters: List<CapParameterDto>? = null
)