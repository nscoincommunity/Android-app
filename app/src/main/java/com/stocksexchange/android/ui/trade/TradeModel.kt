package com.stocksexchange.android.ui.trade

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.data.repositories.orderbooks.OrderbooksRepository
import com.stocksexchange.android.data.repositories.orders.OrdersRepository
import com.stocksexchange.android.data.repositories.wallets.WalletsRepository
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.trade.TradeModel.ActionListener
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.api.model.rest.parameters.OrderCreationParameters
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters

class TradeModel(
    private val currencyMarketsRepository: CurrencyMarketsRepository,
    private val walletsRepository: WalletsRepository,
    private val orderbooksRepository: OrderbooksRepository,
    private val ordersRepository: OrdersRepository
) : BaseModel<ActionListener>() {


    companion object {

        const val REQUEST_TYPE_WALLETS_RETRIEVAL = 0
        const val REQUEST_TYPE_ORDERBOOK_RETRIEVAL = 1
        const val REQUEST_TYPE_ORDER_CREATION = 2

    }




    fun performWalletsRetrievalRequest(currencyIds: List<Int>) {
        performRequest(
            requestType = REQUEST_TYPE_WALLETS_RETRIEVAL,
            params = currencyIds
        )
    }


    fun performOrderbookRetrievalRequest(params: OrderbookParameters) {
        performRequest(
            requestType = REQUEST_TYPE_ORDERBOOK_RETRIEVAL,
            params = params
        )
    }


    fun performOrderCreationRequest(params: OrderCreationParameters) {
        performRequest(
            requestType = REQUEST_TYPE_ORDER_CREATION,
            params = params
        )
    }


    @Suppress("UNCHECKED_CAST")
    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {
            REQUEST_TYPE_WALLETS_RETRIEVAL -> getWallets(params as List<Int>)
            REQUEST_TYPE_ORDERBOOK_RETRIEVAL -> getOrderbook(params as OrderbookParameters)
            REQUEST_TYPE_ORDER_CREATION -> createOrder(params as OrderCreationParameters)

            else -> throw IllegalStateException()
        }
    }


    private suspend fun getWallets(params: List<Int>): RepositoryResult<List<Wallet>> {
        // Refreshing to fetch new data on subsequent requests
        walletsRepository.refresh()

        return walletsRepository.getByCurrencyIds(params ).apply {
            log("walletsRepository.get(params: $params)")
        }
    }


    private suspend fun getOrderbook(params: OrderbookParameters): RepositoryResult<Orderbook> {
        return orderbooksRepository.get(params).apply {
            log("orderbooksRepository.get(params: $params)")
        }
    }


    private suspend fun createOrder(params: OrderCreationParameters): RepositoryResult<Order> {
        return ordersRepository.create(params).apply {
            log("ordersRepository.create(params: $params)")
        }
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



    interface ActionListener : BaseActionListener {

        fun onCurrencyMarketFavorited(currencyMarket: CurrencyMarket)

        fun onCurrencyMarketUnfavorited(currencyMarket: CurrencyMarket)

    }


}