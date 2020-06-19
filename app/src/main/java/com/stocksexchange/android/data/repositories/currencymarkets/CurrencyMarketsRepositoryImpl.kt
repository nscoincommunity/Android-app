package com.stocksexchange.android.data.repositories.currencymarkets

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.android.data.repositories.currencymarkets.utils.MergePolicy
import com.stocksexchange.android.data.repositories.currencypairs.CurrencyPairsRepository
import com.stocksexchange.android.data.repositories.favoritecurrencypairs.FavoriteCurrencyPairsRepository
import com.stocksexchange.android.data.repositories.tickeritems.TickerItemsRepository
import com.stocksexchange.android.mappings.mapToIdTickerItemMap
import com.stocksexchange.android.mappings.mapToIdCurrencyPairMap
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.exceptions.NotFoundException

class CurrencyMarketsRepositoryImpl(
    private val currencyPairsRepository: CurrencyPairsRepository,
    private val tickerItemsRepository: TickerItemsRepository,
    private val favoriteCurrencyPairsRepository: FavoriteCurrencyPairsRepository
) : CurrencyMarketsRepository {


    @Synchronized
    override suspend fun refresh() {
        currencyPairsRepository.refresh()
        tickerItemsRepository.refresh()
    }


    @Synchronized
    override suspend fun save(currencyMarket: CurrencyMarket) {
        currencyPairsRepository.save(currencyMarket.currencyPair)
        tickerItemsRepository.save(currencyMarket.tickerItem)
    }


    @Synchronized
    override suspend fun favorite(currencyMarket: CurrencyMarket) {
        favoriteCurrencyPairsRepository.favorite(currencyMarket.currencyPair)
    }


    @Synchronized
    override suspend fun unfavorite(currencyMarket: CurrencyMarket) {
        favoriteCurrencyPairsRepository.unfavorite(currencyMarket.currencyPair)
    }


    @Synchronized
    override suspend fun deleteAll() {
        currencyPairsRepository.deleteAll()
        tickerItemsRepository.deleteAll()
    }


    private fun mergeCurrencyPairsAndTickerItemsResults(
        currencyPairsRepositoryResult: RepositoryResult<List<CurrencyPair>>,
        tickerItemsRepositoryResult: RepositoryResult<List<TickerItem>>,
        mergePolicy: MergePolicy
    ): RepositoryResult<List<CurrencyMarket>> {
        if(currencyPairsRepositoryResult.isErroneous() || tickerItemsRepositoryResult.isErroneous()) {
            return RepositoryResult.newErroneousInstance(listOf(
                currencyPairsRepositoryResult,
                tickerItemsRepositoryResult
            ))
        }

        val currencyPairs = currencyPairsRepositoryResult.getSuccessfulResultValue()
        val tickerItemsIdMap = tickerItemsRepositoryResult.getSuccessfulResultValue().mapToIdTickerItemMap()

        var tickerItem: TickerItem
        val currencyMarkets = mutableListOf<CurrencyMarket>().apply {
            for(currencyPair in currencyPairs) {
                tickerItem = (tickerItemsIdMap[currencyPair.id] ?: TickerItem.STUB_TICKER_ITEM)

                if(mergePolicy.allowMerge(currencyPair, tickerItem)) {
                    add(CurrencyMarket(currencyPair = currencyPair, tickerItem = tickerItem))
                }
            }
        }

        return RepositoryResult.newSuccessfulInstance(
            potentialSuccessfulResults = listOf(
                currencyPairsRepositoryResult,
                tickerItemsRepositoryResult
            ),
            successfulValue = currencyMarkets
        )
    }


    @Synchronized
    override suspend fun search(query: String): RepositoryResult<List<CurrencyMarket>> {
        return mergeCurrencyPairsAndTickerItemsResults(
            currencyPairsRepositoryResult = currencyPairsRepository.search(query),
            tickerItemsRepositoryResult = tickerItemsRepository.getAll(),
            mergePolicy = MergePolicy.BOTH_MANDATORY
        )
    }


    @Synchronized
    override suspend fun isCurrencyMarketFavorite(currencyMarket: CurrencyMarket): Boolean {
        return favoriteCurrencyPairsRepository.isCurrencyPairFavorite(currencyMarket.currencyPair)
    }


    @Synchronized
    override suspend fun getCurrencyMarket(pairId: Int): RepositoryResult<CurrencyMarket> {
        val currencyPairResult = currencyPairsRepository.get(pairId)

        if(currencyPairResult.isErroneous()) {
            return RepositoryResult.newErroneousInstance(currencyPairResult)
        }

        val tickerItemResult = tickerItemsRepository.get(pairId)

        if(tickerItemResult.isErroneous()) {
            return RepositoryResult.newErroneousInstance(tickerItemResult)
        }

        return RepositoryResult.newSuccessfulInstance(
            potentialSuccessfulResults = listOf(currencyPairResult, tickerItemResult),
            successfulValue = CurrencyMarket(
                currencyPair = currencyPairResult.getSuccessfulResultValue(),
                tickerItem = tickerItemResult.getSuccessfulResultValue()
            )
        )
    }


    @Synchronized
    override suspend fun getCurrencyMarket(baseCurrencySymbol: String, quoteCurrencySymbol: String): RepositoryResult<CurrencyMarket> {
        return getCurrencyMarket {
            (it.baseCurrencySymbol == baseCurrencySymbol) && (it.quoteCurrencySymbol == quoteCurrencySymbol)
        }
    }


    private suspend fun getCurrencyMarket(condition: (CurrencyMarket) -> Boolean): RepositoryResult<CurrencyMarket> {
        val result = getAll()

        if(result.isSuccessful()) {
            for(currencyMarket in result.getSuccessfulResultValue()) {
                if(condition(currencyMarket)) {
                    return RepositoryResult.newSuccessfulInstance(
                        successfulResult = result,
                        successfulValue = currencyMarket
                    )
                }
            }
        }

        return RepositoryResult(cacheResult = Result.Failure(NotFoundException()))
    }


    @Synchronized
    override suspend fun getCurrencyMarkets(groupId: Int): RepositoryResult<List<CurrencyMarket>> {
        val result = getAllInternal(MergePolicy.BOTH_MANDATORY)

        return if(result.isSuccessful()) {
            val filterResult = Result.Success(result.getSuccessfulResultValue().filter {
                it.groupId == groupId
            })

            when {
                result.isCacheResultSuccessful() -> result.cacheResult = filterResult
                result.isDatabaseResultSuccessful() -> result.databaseResult = filterResult
                result.isServerResultSuccessful() -> result.serverResult = filterResult

                else -> throw IllegalStateException()
            }

            result
        } else {
            result
        }
    }


    @Synchronized
    override suspend fun getFavoriteMarkets(): RepositoryResult<List<CurrencyMarket>> {
        val favoriteCurrencyPairIdsResult = favoriteCurrencyPairsRepository.getAll()
        val currencyPairsResult = currencyPairsRepository.getAll()

        if(favoriteCurrencyPairIdsResult.isErroneous() || currencyPairsResult.isErroneous()) {
            return RepositoryResult.newErroneousInstance(listOf(
                favoriteCurrencyPairIdsResult,
                currencyPairsResult
            ))
        }

        val favoriteCurrencyPairIds = favoriteCurrencyPairIdsResult.getSuccessfulResultValue()
        val currencyPairsMap = currencyPairsResult.getSuccessfulResultValue().mapToIdCurrencyPairMap()
        val currencyPairs = mutableListOf<CurrencyPair>().apply {
            for(favoriteCurrencyPairId in favoriteCurrencyPairIds) {
                if(!currencyPairsMap.containsKey(favoriteCurrencyPairId)) {
                    continue
                }

                add(currencyPairsMap.getValue(favoriteCurrencyPairId))
            }
        }

        return mergeCurrencyPairsAndTickerItemsResults(
            currencyPairsRepositoryResult = RepositoryResult.newSuccessfulInstance(
                successfulResult = currencyPairsResult,
                successfulValue = currencyPairs.toList()
            ),
            tickerItemsRepositoryResult = tickerItemsRepository.getAll(),
            mergePolicy = MergePolicy.BOTH_MANDATORY
        )
    }


    @Synchronized
    override suspend fun getAll(): RepositoryResult<List<CurrencyMarket>> {
        return getAllInternal(MergePolicy.BOTH_OPTIONAL)
    }


    private suspend fun getAllInternal(mergePolicy: MergePolicy): RepositoryResult<List<CurrencyMarket>> {
        return mergeCurrencyPairsAndTickerItemsResults(
            currencyPairsRepositoryResult = currencyPairsRepository.getAll(),
            tickerItemsRepositoryResult = tickerItemsRepository.getAll(),
            mergePolicy = mergePolicy
        )
    }


}