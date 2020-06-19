package com.stocksexchange.android.ui.news.fragments.blog

import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.api.model.rest.parameters.NewsParameters
import com.stocksexchange.android.data.repositories.newsrepository.NewsRepository
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingSimpleModel
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlogNewsModel(
    private val newsRepository: NewsRepository
) : BaseDataLoadingSimpleModel<
    NewsBlogRssModel,
    NewsParameters,
    BlogNewsModel.ActionListener
    >() {


    override fun canLoadData(params: NewsParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val isNewData = (dataType == DataType.NEW_DATA)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())
        val isNetworkConnectivityTrigger = (dataLoadingTrigger == DataLoadingTrigger.NETWORK_CONNECTIVITY)
        val isBottomReachTrigger = (dataLoadingTrigger == DataLoadingTrigger.BOTTOM_REACH)

        return ((!isNewDataWithIntervalNotApplied || isNetworkConnectivityTrigger || isBottomReachTrigger))
    }


    override suspend fun refreshData(params: NewsParameters) {
        newsRepository.refresh()
    }


    override suspend fun getRepositoryResult(params: NewsParameters): RepositoryResult<NewsBlogRssModel> {
        return newsRepository.getBlogNews()
            .log(getLogKey())
            .onSuccess {
                withContext(Dispatchers.Main) { handleSuccessfulResponse(it) }
            }
            .onFailure {
                withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) }
            }
    }


    private fun getLogKey(): String = "newsRepository.get()"


    interface ActionListener : BaseDataLoadingActionListener<NewsBlogRssModel>


}