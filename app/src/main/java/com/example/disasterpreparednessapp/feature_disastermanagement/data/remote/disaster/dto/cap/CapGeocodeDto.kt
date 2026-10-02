package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.cap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


// NEW: same shape as CapParameter but modeled separately so callers don't
// mix up "parameter" entries (e.g. Polygon URL) with "geocode" entries
// (e.g. LGD District Code) even though the XML shape is identical.
@Root(name = "geocode", strict = false)
data class CapGeocodeDto @JvmOverloads constructor(
    @field:Element(name = "valueName", required = false)
    var valueName: String? = null,

    @field:Element(name = "value", required = false)
    var value: String? = null
)