package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rss.NewsBlogItemModel
import com.stocksexchange.android.ui.news.fragments.blog.BlogNewsItem


fun NewsBlogItemModel.mapToBlogNewsItem(): BlogNewsItem {
    return BlogNewsItem(this)
}


fun List<NewsBlogItemModel>.mapToBlogNewsList(): List<BlogNewsItem> {
    return map { it.mapToBlogNewsItem() }
}