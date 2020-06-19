package com.stocksexchange.android.ui.alertprice

import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters
import com.stocksexchange.android.data.repositories.alertprice.AlertPriceRepository
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingSimpleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlertPriceModel(
    private val alertPriceRepository: AlertPriceRepository,
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseDataLoadingSimpleModel<
    List<AlertPrice>,
    AlertPriceParameters,
    AlertPriceModel.ActionListener
    >() {


    companion object {

        const val REQUEST_TYPE_DELETE_ITEM = 0
        const val REQUEST_TYPE_ON_CLICK_PAIR = 1

    }




    override fun canLoadData(params: AlertPriceParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val isNewData = (dataType == DataType.NEW_DATA)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())
        val isNetworkConnectivityTrigger = (dataLoadingTrigger == DataLoadingTrigger.NETWORK_CONNECTIVITY)
        val isBottomReachTrigger = (dataLoadingTrigger == DataLoadingTrigger.BOTTOM_REACH)

        return (!isNewDataWithIntervalNotApplied || isNetworkConnectivityTrigger || isBottomReachTrigger)
    }


    override suspend fun refreshData(params: AlertPriceParameters) {
        alertPriceRepository.refresh(params)
    }


    override suspend fun getRepositoryResult(params: AlertPriceParameters): RepositoryResult<List<AlertPrice>> {
        return alertPriceRepository.get(params)
            .log(getLogKey(params))
            .onSuccess { withContext(Dispatchers.Main) { handleSuccessfulResponse(it) } }
            .onFailure { withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) } }
    }


    private fun getLogKey(params: AlertPriceParameters): String {
        return "alertPriceRepository.get(params: $params)"
    }


    fun deleteItem(id: Int) {
        performRequest(
            requestType = REQUEST_TYPE_DELETE_ITEM,
            params = id
        )
    }


    fun onClickItem(pairId: Int) {
        performRequest(
            requestType = REQUEST_TYPE_ON_CLICK_PAIR,
            params = pairId
        )
    }




    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when (requestType) {
            REQUEST_TYPE_DELETE_ITEM -> {
                alertPriceRepository.delete(params as Int).apply {
                    log("alertPriceRepository.delete()")
                }
            }

            REQUEST_TYPE_ON_CLICK_PAIR -> {
                currencyMarketsRepository.getCurrencyMarket(params as Int).apply {
                    log("currencyMarketsRepository.getCurrencyMarket($params)")
                }
            }

            else -> throw IllegalStateException()
        }
    }


    interface ActionListener : BaseDataLoadingActionListener<List<AlertPrice>>


}