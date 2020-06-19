package com.stocksexchange.android.ui.currencymarkets.fragment

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.model.comparators.CurrencyMarketItemComparator
import com.stocksexchange.android.utils.handlers.FiatCurrencyPriceHandler

class CurrencyMarketsRecyclerViewAdapter(
    context: Context,
    items: MutableList<CurrencyMarketItem>
) : TrackableRecyclerViewAdapter<Long, CurrencyMarketItem, CurrencyMarketItem.ViewHolder>(context, items) {


    private lateinit var mResources: CurrencyMarketResources

    var onItemClickListener: ((View, CurrencyMarketItem, Int) -> Unit)? = null
    var onItemLongClickListener: ((View, CurrencyMarketItem, Int) -> Unit)? = null




    override fun assignListeners(holder: CurrencyMarketItem.ViewHolder, position: Int, item: CurrencyMarketItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnItemClickListener(holder, position, onItemClickListener)
            setOnItemLongClickListener(holder, position, onItemLongClickListener)
        }
    }


    /**
     * Sorts the items using the given comparator and notifies the adapter.
     *
     * @param comparator The comparator to sort the items with
     */
    fun sort(comparator: CurrencyMarketComparator) {
        items.sortWith(CurrencyMarketItemComparator(comparator))
        notifyDataSetChanged()
    }


    fun setBaseCurrencySymbolCharacterLimit(baseCurrencySymbolCharacterLimit: Int) {
        mResources.baseCurrencySymbolCharacterLimit = baseCurrencySymbolCharacterLimit
    }


    fun setFiatCurrencyPriceHandler(fiatCurrencyPriceHandler: FiatCurrencyPriceHandler) {
        mResources.fiatCurrencyPriceHandler = fiatCurrencyPriceHandler
    }


    fun setSettings(settings: Settings) {
        mResources.settings = settings
    }


    /**
     * Returns a data set index for the specified pair ID or null if
     * it is absent.
     *
     * @param pairId The pair ID to get the index for
     *
     * @return The data set index or null if it is absent
     */
    fun getDataSetPositionForPairId(pairId: Int): Int? {
        return items.indices.firstOrNull {
            items[it].itemModel.pairId == pairId
        }
    }


    /**
     * Retrieves a chronological data set position for the specified currency market.
     *
     * @param currencyMarket The currency market to get the position for
     * @param comparator The comparator to compare two currency markets
     *
     * @return The chronological data set position
     */
    fun getChronologicalPositionForCurrencyMarket(
        currencyMarket: CurrencyMarket,
        comparator: CurrencyMarketComparator?
    ): Int {
        if(comparator == null) {
            return itemCount
        }

        return items.indices.firstOrNull {
            comparator.compare(items[it].itemModel, currencyMarket) == -1
        } ?: itemCount
    }


    fun setResources(resources: CurrencyMarketResources) {
        mResources = resources
    }


    override fun getResources(): CurrencyMarketResources? {
        return mResources
    }


}