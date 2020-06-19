package com.stocksexchange.android.data.repositories.currencymarkets.utils

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.api.model.rest.TickerItem

/**
 * A policy used for determining whether to merge a currency pair
 * and a ticker item together.
 */
interface MergePolicy {


    companion object {

        /**
         * A policy that allows both a currency pair and a ticker item to be merged.
         * This policy does not perform any checks, therefore is useful to use
         * when all data should be returned.
         */
        val BOTH_OPTIONAL = object : MergePolicy {

            override fun allowMerge(currencyPair: CurrencyPair, tickerItem: TickerItem): Boolean {
                return true
            }

        }

        /**
         * A policy that requires both a currency pair and a ticker item to be valid
         * (non-empty) instances. This policy is useful to use when only the valid
         * data should be returned.
         */
        val BOTH_MANDATORY = object : MergePolicy {

            override fun allowMerge(currencyPair: CurrencyPair, tickerItem: TickerItem): Boolean {
                return (!currencyPair.isStub && !tickerItem.isStub)
            }

        }

    }


    /**
     * A method to implement to determine whether to allow the merge to be
     * performed or not.
     *
     * @param currencyPair The currency pair to check. Emptiness of the instance
     * can be checked by calling [CurrencyPair.isStub] property.
     * @param tickerItem The ticker item to check. Emptiness of the instance
     * can be checked by calling [TickerItem.isStub] property.
     *
     * @return true if should be allowed; false otherwise
     */
    fun allowMerge(currencyPair: CurrencyPair, tickerItem: TickerItem): Boolean


}