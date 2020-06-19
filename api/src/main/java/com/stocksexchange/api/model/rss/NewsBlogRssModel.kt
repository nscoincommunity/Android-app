package com.stocksexchange.api.model.rss

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class NewsBlogRssModel(
    @param:ElementList(name = "item", inline = true)
    @field:ElementList(name = "item", inline = true)
    @field:Path("channel")
    val rssItems: List<NewsBlogItemModel>
)