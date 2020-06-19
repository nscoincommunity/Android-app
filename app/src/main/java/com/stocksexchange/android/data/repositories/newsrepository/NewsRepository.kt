package com.stocksexchange.android.data.repositories.newsrepository

import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.android.data.base.NewsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface NewsRepository : NewsData<
    RepositoryResult<NewsBlogRssModel>,
    RepositoryResult<List<NewsTwitterItemModel>>
>, Repository {

    suspend fun refresh()

}