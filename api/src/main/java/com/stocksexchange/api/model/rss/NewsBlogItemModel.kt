package com.stocksexchange.api.model.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class NewsBlogItemModel(
    @field:Element(name="title", data = true)
    @param:Element(name = "title", data = true)
    val title: String,

    @field:Element(name="link")
    @param:Element(name = "link")
    val link: String,

    @field:Element(name="pubDate")
    @param:Element(name = "pubDate")
    val pubDate: String,

    @field:Element(name="content")
    @param:Element(name = "content")
    val mediaContent: NewsBlogImageUrlModel
) {


    val titleWithoutTags: String
        get() = title.replace("\n", " ").replace("\t", "")


}