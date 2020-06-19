package com.stocksexchange.android.ui.currencymarkets.fragment

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.model.*
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingSimpleModel
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsModel.ActionListener

class CurrencyMarketsModel(
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseDataLoadingSimpleModel<
    List<CurrencyMarket>,
    CurrencyMarketParameters,
    ActionListener
    >() {


    override fun canLoadData(params: CurrencyMarketParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val marketType = params.currencyMarketType
        val searchQuery = params.searchQuery

        val isMarketSearch = (marketType == CurrencyMarketType.SEARCH)
        val isNewData = (dataType == DataType.NEW_DATA)

        val isMarketSearchWithNoQuery = (isMarketSearch && searchQuery.isBlank())
        val isMarketSearchNewData = (isMarketSearch && isNewData)
        val isFavoriteMarketsNewData = ((marketType == CurrencyMarketType.FAVORITES) && isNewData)

        val isNetworkConnectivityTrigger = (dataLoadingTrigger == DataLoadingTrigger.NETWORK_CONNECTIVITY)
        val isRealTimeDataUpdateTrigger = (dataLoadingTrigger == DataLoadingTrigger.REAL_TIME_DATA_UPDATE)
        val dataLoadingIntervalCancellingFlags = (isNetworkConnectivityTrigger || isRealTimeDataUpdateTrigger)

        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied() && !dataLoadingIntervalCancellingFlags)

        return (!isMarketSearchWithNoQuery
                && !isMarketSearchNewData
                && !isFavoriteMarketsNewData
                && !isNewDataWithIntervalNotApplied)
    }


    override suspend fun refreshData(params: CurrencyMarketParameters) {
        currencyMarketsRepository.refresh()
    }


    override suspend fun getRepositoryResult(params: CurrencyMarketParameters): RepositoryResult<List<CurrencyMarket>> {
        return when(params.currencyMarketType) {
            CurrencyMarketType.FAVORITES -> currencyMarketsRepository.getFavoriteMarkets()
            CurrencyMarketType.SEARCH -> currencyMarketsRepository.search(params.lowercasedSearchQuery)

            else -> currencyMarketsRepository.getCurrencyMarkets(params.currencyPairGroup.id)
        }.log("currencyMarketsRepository.get(params: $params)")
    }


    interface ActionListener : BaseDataLoadingActionListener<List<CurrencyMarket>>


}