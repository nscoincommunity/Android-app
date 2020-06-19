package com.stocksexchange.android.data.stores.news

import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.android.data.base.NewsData
import com.stocksexchange.core.model.Result

interface NewsDataStore : NewsData<Result<NewsBlogRssModel>, Result<List<NewsTwitterItemModel>>>
