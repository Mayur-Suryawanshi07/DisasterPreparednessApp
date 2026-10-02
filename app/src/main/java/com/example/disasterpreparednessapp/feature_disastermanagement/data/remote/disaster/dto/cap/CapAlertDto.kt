package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.cap

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "alert", strict = false)
data class CapAlertDto @JvmOverloads constructor(

    @field:Element(name = "identifier", required = false)
    var identifier: String? = null,

    @field:Element(name = "sender", required = false)
    var sender: String? = null,

    @field:Element(name = "sent", required = false)
    var sent: String? = null,

    @field:Element(name = "status", required = false)
    var status: String? = null,

    // NEW: was present in the live payload (e.g. "Update") but not mapped before
    @field:Element(name = "msgType", required = false)
    var msgType: String? = null,

    // NEW: e.g. "Public"
    @field:Element(name = "scope", required = false)
    var scope: String? = null,

    @field:ElementList(entry = "info", inline = true, required = false)
    var infos: List<CapInfoDto>? = null
)