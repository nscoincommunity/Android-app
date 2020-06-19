package com.stocksexchange.android.data.repositories.newsrepository

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.android.data.stores.news.NewsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class NewsRepositoryImpl(
    private val serverDataStore: NewsDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : NewsRepository {


    @Synchronized
    override suspend fun refresh() {
        freshDataHandler.refresh()
    }


    @Synchronized
    override suspend fun getTwitterNews(): RepositoryResult<List<NewsTwitterItemModel>> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(NoInternetException())
        }

        return RepositoryResult(serverResult = serverDataStore.getTwitterNews())
    }


    @Synchronized
    override suspend fun getBlogNews(): RepositoryResult<NewsBlogRssModel> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(NoInternetException())
        }

        return RepositoryResult(serverResult = serverDataStore.getBlogNews())
    }


}