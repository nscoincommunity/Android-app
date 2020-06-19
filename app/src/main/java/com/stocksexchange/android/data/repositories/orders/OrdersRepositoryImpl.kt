package com.stocksexchange.android.data.repositories.orders

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.OrdersFreshDataHandler
import com.stocksexchange.android.data.repositories.currencypairs.CurrencyPairsRepository
import com.stocksexchange.android.data.stores.orders.OrdersDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.api.model.rest.parameters.OrderCreationParameters
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class OrdersRepositoryImpl(
    private val serverDataStore: OrdersDataStore,
    private val databaseDataStore: OrdersDataStore,
    private val currencyPairsRepository: CurrencyPairsRepository,
    private val freshDataHandler: OrdersFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : OrdersRepository {


    @Synchronized
    override suspend fun refresh(params: OrderParameters) {
        freshDataHandler.refresh(params)
    }


    @Synchronized
    override suspend fun save(order: Order) {
        databaseDataStore.save(order)
    }


    @Synchronized
    override suspend fun save(orders: List<Order>) {
        databaseDataStore.save(orders)
    }


    @Synchronized
    override suspend fun create(params: OrderCreationParameters): RepositoryResult<Order> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        val orderCreationResult = serverDataStore.create(params).also {
            if(it is Result.Success) {
                save(it.value)
            }
        }

        return RepositoryResult(serverResult = orderCreationResult)
    }


    @Synchronized
    override suspend fun cancel(order: Order): RepositoryResult<OrdersCancellationResponse> {
        return RepositoryResult(serverResult = if(connectionProvider.isNetworkAvailable()) {
            val result = serverDataStore.cancel(order)

            if(result is Result.Success) {
                save(Order.cancelActiveOrder(order))
            }

            result
        } else {
            Result.Failure(NoInternetException())
        })
    }


    @Synchronized
    override suspend fun delete(order: Order) {
        databaseDataStore.delete(order)
    }


    @Synchronized
    override suspend fun delete(status: OrderStatus) {
        databaseDataStore.delete(status)
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun clear() {
        deleteAll()

        freshDataHandler.reset()
    }


    @Synchronized
    override suspend fun search(params: OrderParameters): RepositoryResult<List<Order>> {
        val currencyPairsResult = currencyPairsRepository.search(params.lowercasedSearchQuery)

        if(currencyPairsResult.isErroneous()) {
            return RepositoryResult.newErroneousInstance(currencyPairsResult)
        }

        val currencyPairIds = currencyPairsResult.getSuccessfulResultValue().map { it.id }

        // Fetching the data to make sure it is present since the search is
        // performed solely on database records
        val ordersResult = when(params.lifecycleType) {
            OrderLifecycleType.ACTIVE -> getActiveOrders(params)

            else -> getHistoryOrders(params)
        }

        return if(ordersResult.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(params, currencyPairIds))
        } else {
            ordersResult
        }
    }


    @Synchronized
    override suspend fun getOrder(id: Long): RepositoryResult<Order> {
        return RepositoryResult(databaseResult = databaseDataStore.getOrder(id))
    }


    @Synchronized
    override suspend fun getActiveOrders(params: OrderParameters): RepositoryResult<List<Order>> {
        return getOrders(params) { getActiveOrders(params) }
    }


    @Synchronized
    override suspend fun getHistoryOrders(params: OrderParameters): RepositoryResult<List<Order>> {
        return getOrders(params) { getHistoryOrders(params) }
    }


    private suspend fun getOrders(
        params: OrderParameters,
        fetchOrders: suspend OrdersDataStore.(params: OrderParameters) -> Result<List<Order>>
    ): RepositoryResult<List<Order>> {
        val result = RepositoryResult<List<Order>>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider, params)) {
            result.serverResult = serverDataStore.fetchOrders(params)

            if(result.isServerResultSuccessful()) {
                if((params.selectivityType == OrderSelectivityType.ANY_PAIR_ID) &&
                    (params.status == OrderStatus.PENDING) &&
                    !params.hasOffset) {
                    // Cleaning up obsolete active orders that may be
                    // present inside the database
                    delete(params.status)
                }

                save(result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.fetchOrders(params)
        }

        return result
    }


}