package com.stocksexchange.android.ui.orders.fragment

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.Order
import com.stocksexchange.api.model.rest.OrderLifecycleType
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.data.repositories.orders.OrdersRepository
import com.stocksexchange.android.mappings.mapToPairIdMarketMap
import com.stocksexchange.android.model.*
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingModel
import com.stocksexchange.android.ui.orders.fragment.OrdersModel.ActionListener
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.helpers.tag
import com.stocksexchange.api.model.rest.OrderMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrdersModel(
    private val ordersRepository: OrdersRepository,
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseDataLoadingModel<
    List<OrderData>,
    OrderParameters,
    ActionListener
    >() {


    companion object {

        const val REQUEST_TYPE_ORDER_CANCELLATION = 0

        private val CLASS = OrdersModel::class.java

        private val SAVED_STATE_LAST_RESULT_ITEM_COUNT = tag(CLASS, "last_result_item_count")

    }


    private var mLastResultItemCount: Int = -1




    override fun resetParameters() {
        super.resetParameters()

        mLastResultItemCount = -1
    }


    override fun canLoadData(params: OrderParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val orderMode = params.mode
        val searchQuery = params.searchQuery

        val isOrderSearch = (orderMode == OrderMode.SEARCH)
        val isNewData = (dataType == DataType.NEW_DATA)

        val isOrderSearchWithNoQuery = (isOrderSearch && searchQuery.isBlank())
        val isOrderSearchNewData = (isOrderSearch && isNewData)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())
        val isNetworkConnectivityTrigger = (dataLoadingTrigger == DataLoadingTrigger.NETWORK_CONNECTIVITY)
        val isBottomReachTrigger = (dataLoadingTrigger == DataLoadingTrigger.BOTTOM_REACH)
        val isDataLoaderExhausted = isDataLoaderExhausted(params)

        return (!isOrderSearchWithNoQuery &&
                !isOrderSearchNewData &&
                (!isNewDataWithIntervalNotApplied ||
                isNetworkConnectivityTrigger ||
                isBottomReachTrigger) &&
                !isDataLoaderExhausted
        )
    }


    private fun isDataLoaderExhausted(params: OrderParameters): Boolean {
        return ((mLastResultItemCount >= 0) && (mLastResultItemCount >= params.limit))
    }


    override suspend fun refreshData(params: OrderParameters) {
        ordersRepository.refresh(params)
    }


    override suspend fun performDataLoading(params: OrderParameters) {
        when(params.mode) {
            OrderMode.STANDARD -> when(params.lifecycleType) {
                OrderLifecycleType.ACTIVE -> ordersRepository.getActiveOrders(params)

                else -> ordersRepository.getHistoryOrders(params)
            }

            OrderMode.SEARCH -> ordersRepository.search(params)
        }.log("ordersRepository.getOrders(params: $params)")
        .onSuccess { orders ->
            currencyMarketsRepository.getAll()
                .log("currencyMarketsRepository.getAll()")
                .onSuccess {
                    val result = mergeOrdersAndCurrencyMarkets(orders, it)

                    withContext(Dispatchers.Main) { handleSuccessfulResponse(result) }
                }
                .onFailure { withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) } }
        }
        .onFailure { withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) } }
    }


    private fun mergeOrdersAndCurrencyMarkets(orders: List<Order>,
                                              currencyMarkets: List<CurrencyMarket>): List<OrderData> {
        val currencyMarketsMap = currencyMarkets.mapToPairIdMarketMap()
        val stubCurrencyMarket = CurrencyMarket.STUB_CURRENCY_MARKET

        return mutableListOf<OrderData>().apply {
            for(order in orders) {
                add(OrderData(
                    order = order,
                    currencyMarket = (currencyMarketsMap[order.currencyPairId] ?: stubCurrencyMarket)
                ))
            }
        }
    }


    fun performOrderCancellationRequest(order: Order, metadata: OrderData) {
        performRequest(
            requestType = REQUEST_TYPE_ORDER_CANCELLATION,
            params = order,
            metadata = metadata
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_ORDER_CANCELLATION -> {
                ordersRepository.cancel(params as Order).apply {
                    log("ordersRepository.cancel(order: $params)")
                }
            }

            else -> throw IllegalStateException()

        }
    }


    override fun handleSuccessfulResponse(data: List<OrderData>) {
        super.handleSuccessfulResponse(data)

        mLastResultItemCount = data.size
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        with(savedState) {
            mLastResultItemCount = get(SAVED_STATE_LAST_RESULT_ITEM_COUNT, -1)
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        with(savedState) {
            save(SAVED_STATE_LAST_RESULT_ITEM_COUNT, mLastResultItemCount)
        }
    }


    interface ActionListener : BaseDataLoadingActionListener<List<OrderData>>


}