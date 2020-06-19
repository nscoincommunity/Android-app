package com.stocksexchange.android.data.stores.news

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.StexRssApi
import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class NewsServerDataStore(
    private val stexRestApi: StexRestApi,
    private val stexApiRss: StexRssApi
) : NewsDataStore {


    override suspend fun getTwitterNews(): Result<List<NewsTwitterItemModel>> {
        return performBackgroundOperation {
            stexRestApi.getTwitterNewsItems()
        }
    }


    override suspend fun getBlogNews(): Result<NewsBlogRssModel> {
        return performBackgroundOperation {
            stexApiRss.getBlogNews()
        }
    }


}