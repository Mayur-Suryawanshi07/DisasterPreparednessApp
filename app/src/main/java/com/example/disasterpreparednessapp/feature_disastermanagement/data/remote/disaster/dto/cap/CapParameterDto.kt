package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.cap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "parameter", strict = false)
data class CapParameterDto @JvmOverloads constructor(
    @field:Element(name = "valueName", required = false)
    var valueName: String? = null,

    @field:Element(name = "value", required = false)
    var value: String? = null
)