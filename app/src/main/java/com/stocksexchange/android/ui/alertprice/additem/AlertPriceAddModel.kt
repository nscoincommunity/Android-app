package com.stocksexchange.android.ui.alertprice.additem

import com.stocksexchange.android.data.repositories.alertprice.AlertPriceRepository
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters
import com.stocksexchange.android.ui.alertprice.additem.AlertPriceAddModel.ActionListener

class AlertPriceAddModel(
    private val alertPriceRepository: AlertPriceRepository,
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseModel<ActionListener>() {


    companion object {

        const val REQUEST_TYPE_GET_ALERT_PRICE_BY_PAIR_ID_RETRIEVAL = 0
        const val REQUEST_TYPE_DELETE_ALERT_PRICE_RETRIEVAL = 1
        const val REQUEST_TYPE_CREATE_ALERT_PRICE_RETRIEVAL = 2

    }



    fun toggleFavoriteState(currencyMarket: CurrencyMarket) {
        createUiLaunchCoroutine {
            if(currencyMarketsRepository.isCurrencyMarketFavorite(currencyMarket)) {
                unfavoriteCurrencyMarket(currencyMarket)
            } else {
                favoriteCurrencyMarket(currencyMarket)
            }
        }
    }


    private suspend fun unfavoriteCurrencyMarket(currencyMarket: CurrencyMarket) {
        currencyMarketsRepository.unfavorite(currencyMarket)

        mActionListener?.onCurrencyMarketUnfavorited(currencyMarket)
    }


    private suspend fun favoriteCurrencyMarket(currencyMarket: CurrencyMarket) {
        currencyMarketsRepository.favorite(currencyMarket)

        mActionListener?.onCurrencyMarketFavorited(currencyMarket)
    }


    fun getAlertPriceListByPairId(id: Int) {
        performRequest(
            requestType = REQUEST_TYPE_GET_ALERT_PRICE_BY_PAIR_ID_RETRIEVAL,
            params = id
        )
    }


    fun deleteAlertPriceItem(id: Int) {
        performRequest(
            requestType = REQUEST_TYPE_DELETE_ALERT_PRICE_RETRIEVAL,
            params = id
        )
    }


    fun createAlertPriceItem(params: AlertPriceParameters) {
        performRequest(
            requestType = REQUEST_TYPE_CREATE_ALERT_PRICE_RETRIEVAL,
            params = params
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_GET_ALERT_PRICE_BY_PAIR_ID_RETRIEVAL -> {
                alertPriceRepository.getAlertPriceListByPairId(params as Int).apply {
                    log("alertPriceRepository.getAlertPriceListByPairId(params: $params)")
                }
            }

            REQUEST_TYPE_DELETE_ALERT_PRICE_RETRIEVAL -> {
                alertPriceRepository.delete(params as Int).apply {
                    log("alertPriceRepository.delete(params: $params)")
                }
            }

            REQUEST_TYPE_CREATE_ALERT_PRICE_RETRIEVAL -> {
                alertPriceRepository.create(params as AlertPriceParameters).apply {
                    log("alertPriceRepository.create(params: $params)")
                }
            }

            else -> throw IllegalStateException()

        }
    }


    interface ActionListener : BaseActionListener {

        fun onCurrencyMarketFavorited(currencyMarket: CurrencyMarket)

        fun onCurrencyMarketUnfavorited(currencyMarket: CurrencyMarket)

    }


}