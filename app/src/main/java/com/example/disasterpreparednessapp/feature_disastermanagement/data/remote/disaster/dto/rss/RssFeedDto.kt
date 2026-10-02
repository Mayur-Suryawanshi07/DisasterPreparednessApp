package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "rss", strict = false)
data class RssFeedDto @JvmOverloads constructor(
    @field:Element(name = "channel")
    var channelDto: ChannelDto? = null
)