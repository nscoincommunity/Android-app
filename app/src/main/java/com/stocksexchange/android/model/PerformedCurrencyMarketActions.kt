package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.api.model.rest.CurrencyMarket
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PerformedCurrencyMarketActions(
    val updatedCurrencyMarketsMap: MutableMap<Int, CurrencyMarket> = mutableMapOf(),
    val favoriteCurrencyMarketsMap: MutableMap<Int, CurrencyMarket> = mutableMapOf(),
    val unfavoriteCurrencyMarketsMap: MutableMap<Int, CurrencyMarket> = mutableMapOf()
) : Parcelable {


    fun addUpdatedCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        updatedCurrencyMarketsMap[currencyMarket.pairId] = currencyMarket
        return this
    }


    fun removeUpdatedCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        updatedCurrencyMarketsMap.remove(currencyMarket.pairId)
        return this
    }


    fun removeAllUpdatedCurrencyMarkets() {
        updatedCurrencyMarketsMap.clear()
    }


    fun hasUpdatedCurrencyMarkets(): Boolean {
        return updatedCurrencyMarketsMap.isNotEmpty()
    }


    fun addFavoriteCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        favoriteCurrencyMarketsMap[currencyMarket.pairId] = currencyMarket
        return this
    }


    fun removeFavoriteCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        favoriteCurrencyMarketsMap.remove(currencyMarket.pairId)
        return this
    }


    fun removeAllFavoriteCurrencyMarkets() {
        favoriteCurrencyMarketsMap.clear()
    }


    fun hasFavoriteCurrencyMarkets(): Boolean {
        return favoriteCurrencyMarketsMap.isNotEmpty()
    }


    fun addUnfavoriteCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        unfavoriteCurrencyMarketsMap[currencyMarket.pairId] = currencyMarket
        return this
    }


    fun removeUnfavoriteCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        unfavoriteCurrencyMarketsMap.remove(currencyMarket.pairId)
        return this
    }


    fun removeAllUnfavoriteCurrencyMarkets() {
        unfavoriteCurrencyMarketsMap.clear()
    }


    fun hasUnfavoriteCurrencyMarkets(): Boolean {
        return unfavoriteCurrencyMarketsMap.isNotEmpty()
    }


    fun merge(actions: PerformedCurrencyMarketActions): PerformedCurrencyMarketActions {
        updatedCurrencyMarketsMap.putAll(actions.updatedCurrencyMarketsMap)
        favoriteCurrencyMarketsMap.putAll(actions.favoriteCurrencyMarketsMap)
        unfavoriteCurrencyMarketsMap.putAll(actions.unfavoriteCurrencyMarketsMap)

        return this
    }


    fun clear() {
        if(isEmpty()) {
            return
        }

        if(hasUpdatedCurrencyMarkets()) {
            removeAllUpdatedCurrencyMarkets()
        }

        if(hasFavoriteCurrencyMarkets()) {
            removeAllFavoriteCurrencyMarkets()
        }

        if(hasUnfavoriteCurrencyMarkets()) {
            removeAllUnfavoriteCurrencyMarkets()
        }
    }


    fun isEmpty(): Boolean {
        return (
            !hasUpdatedCurrencyMarkets() &&
            !hasFavoriteCurrencyMarkets() &&
            !hasUnfavoriteCurrencyMarkets()
        )
    }


}