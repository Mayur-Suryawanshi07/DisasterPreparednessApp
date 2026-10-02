package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.rss

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
data class ChannelDto @JvmOverloads constructor(
    @field:ElementList(inline = true, required = false)
    var itemDtos: List<ItemDto>? = null
)
