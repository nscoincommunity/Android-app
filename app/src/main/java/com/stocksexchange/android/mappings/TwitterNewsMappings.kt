package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.android.ui.news.fragments.twitter.TwitterNewsItem


fun NewsTwitterItemModel.mapToTwitterNewsItem(): TwitterNewsItem {
    return TwitterNewsItem(this)
}


fun List<NewsTwitterItemModel>.mapToTwitterNewsList(): List<TwitterNewsItem> {
    return map { it.mapToTwitterNewsItem() }
}