package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RssFeed @JvmOverloads constructor(
    @field:Element(name = "channel")
    var channel: Channel? = null
)

@Root(name = "channel", strict = false)
data class Channel @JvmOverloads constructor(
    @field:ElementList(inline = true, required = false)
    var items: List<Item>? = null
)

@Root(name = "item", strict = false)
data class Item @JvmOverloads constructor(
    @field:Element(name = "title", required = false)
    var title: String? = null,

    @field:Element(name = "link", required = false)
    var link: String? = null,

    @field:Element(name = "author", required = false)
    var author: String? = null,

    @field:Element(name = "description", required = false)
    var description: String? = null,

    @field:Element(name = "category", required = false)
    var category: String? = null,

    @field:Element(name = "guid", required = false)
    var guid: String? = null,

    @field:Element(name = "pubDate", required = false)
    var pubDate: String? = null
)

@Root(name = "alert", strict = false)
data class CapAlert @JvmOverloads constructor(

    @field:Element(name = "identifier", required = false)
    var identifier: String? = null,

    @field:Element(name = "sender", required = false)
    var sender: String? = null,

    @field:Element(name = "sent", required = false)
    var sent: String? = null,

    @field:Element(name = "status", required = false)
    var status: String? = null,

    @field:ElementList(entry = "info", inline = true, required = false)
    var infos: List<CapInfo>? = null
)

@Root(name = "info", strict = false)
data class CapInfo @JvmOverloads constructor(

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
    var parameters: List<CapParameter>? = null
)

@Root(name = "parameter", strict = false)
data class CapParameter @JvmOverloads constructor(
    @field:Element(name = "valueName", required = false)
    var valueName: String? = null,

    @field:Element(name = "value", required = false)
    var value: String? = null
)

@Root(name = "area", strict = false)
data class CapArea @JvmOverloads constructor(

    @field:Element(name = "areaDesc", required = false)
    var areaDesc: String? = null
)