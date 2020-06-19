package com.stocksexchange.android.ui.news.fragments.twitter

import com.stocksexchange.api.model.rest.parameters.NewsParameters
import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.android.data.repositories.newsrepository.NewsRepository
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingSimpleModel
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TwitterNewsModel(
    private val newsRepository: NewsRepository
) : BaseDataLoadingSimpleModel<
    List<NewsTwitterItemModel>,
    NewsParameters,
    TwitterNewsModel.ActionListener
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


    override suspend fun getRepositoryResult(params: NewsParameters): RepositoryResult<List<NewsTwitterItemModel>> {
        return newsRepository.getTwitterNews()
            .log(getLogKey(params))
            .onSuccess {
                withContext(Dispatchers.Main) { handleSuccessfulResponse(it) }
            }
            .onFailure {
                withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) }
            }
    }


    private fun getLogKey(params: NewsParameters) = "newsRepository.get(params: $params)"


    interface ActionListener : BaseDataLoadingActionListener<List<NewsTwitterItemModel>>


}