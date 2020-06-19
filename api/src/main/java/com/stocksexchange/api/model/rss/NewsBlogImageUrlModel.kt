package com.stocksexchange.api.model.rss

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "content", strict = false)
data class NewsBlogImageUrlModel(
    @field:Attribute(name="url")
    @param:Attribute(name="url")
    val url: String
)