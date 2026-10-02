package com.example.disasterpreparednessapp.feature_disastermanagement.data.remote.disaster.dto.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "item", strict = false)
data class ItemDto @JvmOverloads constructor(
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