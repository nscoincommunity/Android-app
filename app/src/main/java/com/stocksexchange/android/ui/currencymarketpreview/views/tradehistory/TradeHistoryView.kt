package com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.android.mappings.mapToIdTradeActionItemsMap
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.model.TradeData
import com.stocksexchange.android.model.TradeHeader
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.currencymarketpreview.views.base.BaseTradingListView
import com.stocksexchange.android.ui.currencymarketpreview.views.base.interfaces.BaseTradingListData
import com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory.items.TradeHistoryHeaderItem
import com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory.items.TradeHistoryItem
import com.stocksexchange.android.utils.extensions.indexOfFirstOrNull
import com.stocksexchange.api.model.rest.SortOrder
import com.stocksexchange.core.decorators.DefaultSpacingItemDecorator
import com.stocksexchange.core.formatters.TimeFormatter
import com.stocksexchange.core.utils.extensions.disableAnimations
import com.stocksexchange.core.utils.extensions.dpToPx
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import kotlinx.android.synthetic.main.trade_history_view_layout.view.*
import org.koin.core.inject
import java.io.Serializable

/**
 * A list view that displays a trade history of a particular currency pair.
 */
class TradeHistoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTradingListView<
    List<Trade>,
    Trade,
    TradeHistoryParameters,
    TradeHistoryRecyclerViewAdapter
>(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_IS_ADJUSTING_TO_TARGET_COUNT_ENABLED = "is_adjusting_to_target_count_enabled"
        private const val ATTRIBUTE_KEY_TRADES_LIMIT = "trades_limit"
        private const val ATTRIBUTE_KEY_TRADE_TIME_COLOR = "trade_time_color"

        private const val DEFAULT_IS_ADJUSTING_TO_TARGET_COUNT_ENABLED = true
        private const val DEFAULT_TRADES_LIMIT = 15

        private const val DEFAULT_TRADE_TIME_COLOR = Color.WHITE

        private const val HEADER_ID = 1000L

    }


    private var mIsDataTruncationEnabled: Boolean = true
    private var mIsAdjustingToTargetCountEnabled: Boolean = true
    private var mIsItemSwipeMenuEnabled: Boolean = false

    private var mTradeTimeColor: Int = 0
    private var mCancelButtonTextColor: Int = 0
    private var mCancellationProgressBarColor: Int = 0

    private var mTradesDataIdMap: MutableMap<Long, TradeData> = mutableMapOf()

    private val mTimeFormatter: TimeFormatter by inject()

    private var mTradeResources = TradeHistoryResources.getDefaultResources(mNumberFormatter, mTimeFormatter)

    private var mRecyclerViewDecorator = DefaultSpacingItemDecorator.STUB

    var onItemCancelBtnClickListener: ((View, TradeHistoryItem, Int) -> Unit)?
        set(value) { mAdapter?.onCancelBtnClickListener = value }
        get() = mAdapter?.onCancelBtnClickListener




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.TradeHistoryView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_IS_HIGHLIGHTING_ENABLED, getBoolean(R.styleable.TradeHistoryView_isHighlightingEnabled, DEFAULT_IS_HIGHLIGHTING_ENABLED))
                save(ATTRIBUTE_KEY_IS_ADJUSTING_TO_TARGET_COUNT_ENABLED, getBoolean(R.styleable.TradeHistoryView_isAdjustingToTargetCountEnabled, DEFAULT_IS_ADJUSTING_TO_TARGET_COUNT_ENABLED))
                save(ATTRIBUTE_KEY_RV_HEIGHT, getLayoutDimension(R.styleable.TradeHistoryView_rvHeight, DEFAULT_RV_HEIGHT))
                save(ATTRIBUTE_KEY_RV_ITEM_BOTTOM_SPACING, getDimensionPixelSize(R.styleable.TradeHistoryView_rvItemBottomSpacing, dpToPx(DEFAULT_RV_ITEM_BOTTOM_SPACING_IN_DP)))
                save(ATTRIBUTE_KEY_TRADES_LIMIT, getInteger(R.styleable.TradeHistoryView_tradesLimit, DEFAULT_TRADES_LIMIT))
                save(ATTRIBUTE_KEY_PRICE_MAX_CHARS_LENGTH, getInteger(R.styleable.TradeHistoryView_priceMaxCharsLength, DEFAULT_PRICE_MAX_CHARS_LENGTH))
                save(ATTRIBUTE_KEY_AMOUNT_MAX_CHARS_LENGTH, getInteger(R.styleable.TradeHistoryView_amountMaxCharsLength, DEFAULT_AMOUNT_MAX_CHARS_LENGTH))
                save(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, getColor(R.styleable.TradeHistoryView_progressBarColor, DEFAULT_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_KEY_INFO_VIEW_COLOR, getColor(R.styleable.TradeHistoryView_infoViewColor, DEFAULT_INFO_VIEW_COLOR))
                save(ATTRIBUTE_KEY_HEADER_TITLE_TEXT_COLOR, getColor(R.styleable.TradeHistoryView_headerTitleTextColor, DEFAULT_HEADER_TITLE_TEXT_COLOR))
                save(ATTRIBUTE_KEY_HEADER_SEPARATOR_COLOR, getColor(R.styleable.TradeHistoryView_headerSeparatorColor, DEFAULT_HEADER_SEPARATOR_COLOR))
                save(ATTRIBUTE_KEY_BUY_HIGHLIGHT_BACKGROUND_COLOR, getColor(R.styleable.TradeHistoryView_buyTradeHighlightBackgroundColor, DEFAULT_BUY_HIGHLIGHT_BACKGROUND_COLOR))
                save(ATTRIBUTE_KEY_SELL_HIGHLIGHT_BACKGROUND_COLOR, getColor(R.styleable.TradeHistoryView_sellTradeHighlightBackgroundColor, DEFAULT_SELL_HIGHLIGHT_BACKGROUND_COLOR))
                save(ATTRIBUTE_KEY_BUY_PRICE_HIGHLIGHT_COLOR, getColor(R.styleable.TradeHistoryView_buyTradePriceHighlightColor, DEFAULT_BUY_PRICE_HIGHLIGHT_COLOR))
                save(ATTRIBUTE_KEY_SELL_PRICE_HIGHLIGHT_COLOR, getColor(R.styleable.TradeHistoryView_sellTradePriceHighlightColor, DEFAULT_SELL_PRICE_HIGHLIGHT_COLOR))
                save(ATTRIBUTE_KEY_BUY_PRICE_COLOR, getColor(R.styleable.TradeHistoryView_buyTradeColor, DEFAULT_BUY_PRICE_COLOR))
                save(ATTRIBUTE_KEY_SELL_PRICE_COLOR, getColor(R.styleable.TradeHistoryView_sellTradeColor, DEFAULT_SELL_PRICE_COLOR))
                save(ATTRIBUTE_KEY_AMOUNT_COLOR, getColor(R.styleable.TradeHistoryView_tradeAmountColor, DEFAULT_AMOUNT_COLOR))
                save(ATTRIBUTE_KEY_TRADE_TIME_COLOR, getColor(R.styleable.TradeHistoryView_tradeTimeColor, DEFAULT_TRADE_TIME_COLOR))
                save(ATTRIBUTE_KEY_ITEM_HIGHLIGHT_DURATION, getInteger(R.styleable.TradeHistoryView_itemHighlightDuration, DEFAULT_ITEM_HIGHLIGHT_DURATION))
                save(ATTRIBUTE_KEY_INFO_VIEW_ICON, (getDrawable(R.styleable.TradeHistoryView_infoViewIcon) ?: getCompatDrawable(getDefaultInfoViewIconResourceId())))
            }
        }
    }


    override fun initRecyclerView() {
        with(recyclerView) {
            disableAnimations()
            addItemDecoration(mRecyclerViewDecorator)

            layoutManager = mLayoutManager

            mAdapter = TradeHistoryRecyclerViewAdapter(context, mItems).apply {
                setHasStableIds(true)
                setResources(mTradeResources)
            }

            adapter = mAdapter
        }
    }


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setAdjustingToTargetCountEnabled(get(
                ATTRIBUTE_KEY_IS_ADJUSTING_TO_TARGET_COUNT_ENABLED,
                DEFAULT_IS_ADJUSTING_TO_TARGET_COUNT_ENABLED
            ))
            setTradesLimit(get(ATTRIBUTE_KEY_TRADES_LIMIT, DEFAULT_TRADES_LIMIT))
            setTradeTimeColor(get(ATTRIBUTE_KEY_TRADE_TIME_COLOR, DEFAULT_TRADE_TIME_COLOR))
        }
    }


    override fun updateRecyclerViewItemDecorator() {
        with(recyclerView) {
            removeItemDecoration(mRecyclerViewDecorator)
            mRecyclerViewDecorator = DefaultSpacingItemDecorator(
                spacing = mRvItemBottomSpacing,
                sideFlags = DefaultSpacingItemDecorator.SIDE_BOTTOM,
                itemExclusionPolicy = DefaultSpacingItemDecorator.LastItemExclusionPolicy()
            )
            addItemDecoration(mRecyclerViewDecorator)
        }
    }


    override fun updateResources() {
        mTradeResources = TradeHistoryResources.newInstance(
            mPriceMaxCharsLength,
            mAmountMaxCharsLength,
            DEFAULT_STUB_TEXT,
            mNumberFormatter,
            mTimeFormatter,
            getColorsForResources(),
            getStringsForResources()
        )

        mAdapter?.setResources(mTradeResources)
    }


    private fun getColorsForResources(): List<Int> {
        return listOf(
            mHeaderTitleTextColor,
            mHeaderSeparatorColor,
            mBuyBackgroundHighlightColor,
            mSellBackgroundHighlightColor,
            mBuyPriceHighlightColor,
            mSellPriceHighlightColor,
            mBuyPriceColor,
            mSellPriceColor,
            mAmountColor,
            mTradeTimeColor,
            mCancelButtonTextColor,
            mCancellationProgressBarColor
        )
    }


    private fun getStringsForResources(): List<String> {
        return listOf(
            mStringProvider.getString(R.string.action_cancel)
        )
    }


    override fun truncateData(data: List<Trade>?): List<Trade>? {
        if(!mIsDataTruncationEnabled) {
            return data
        }

        return if(data == null) {
            data
        } else if(data.size > mDataParameters.count) {
            data.take(mDataParameters.count)
        } else {
            data
        }
    }


    override fun updateData(data: List<Trade>, dataActionItems: List<DataActionItem<Trade>>) {
        super.updateData(data, dataActionItems)

        val idTradeActionItemsMap = dataActionItems.mapToIdTradeActionItemsMap()

        bindDataInternal {
            if(mIsHighlightingEnabled) {
                if(idTradeActionItemsMap[it.id]?.action == DataActionItem.Action.INSERT) {
                    (System.currentTimeMillis() + mItemHighlightDuration)
                } else {
                    BaseTradingListData.NO_TIMESTAMP
                }
            } else {
                BaseTradingListData.NO_TIMESTAMP
            }

        }
    }


    override fun bindDataInternal(getHighlightEndTimestamp: (Trade) -> Long) {
        if(isDataEmpty()) {
            return
        }

        val data = mData!!
        val tradesDataIdMap: MutableMap<Long, TradeData> = mutableMapOf()

        mItems.clear()
        mItems.add(TradeHistoryHeaderItem(TradeHeader(
            id = HEADER_ID,
            amountTitleText = mStringProvider.getString(R.string.amount),
            priceTitleText = mStringProvider.getString(R.string.price),
            timeTitleText = mStringProvider.getString(R.string.time)
        )))

        val tradesCount = if(mIsAdjustingToTargetCountEnabled) {
            mDataParameters.count
        } else {
            data.size
        }
        val stubBuyTrade = Trade.STUB_BUY_TRADE
        val stubSellTrade = Trade.STUB_SELL_TRADE
        var previousTrade = stubBuyTrade
        var trade: Trade?
        var tradeData: TradeData?

        for(i in 0 until tradesCount) {
            trade = data.getOrNull(i) ?: if(i == 0) {
                stubBuyTrade
            } else {
                when(previousTrade.type) {
                    TradeType.BUY -> stubSellTrade
                    TradeType.SELL -> stubBuyTrade
                }
            }
            previousTrade = trade
            tradeData = TradeData(
                highlightEndTimestamp = if(mTradesDataIdMap.containsKey(trade.id)) {
                    mTradesDataIdMap[trade.id]?.highlightEndTimestamp
                        ?: BaseTradingListData.NO_TIMESTAMP
                } else {
                    getHighlightEndTimestamp(trade)
                },
                trade = trade,
                isSwipeMenuEnabled = (mIsItemSwipeMenuEnabled && !trade.isStub),
                isProgressBarVisible = false
            )

            tradesDataIdMap[trade.id] = tradeData
            mItems.add(TradeHistoryItem(tradeData))
        }

        mTradesDataIdMap = tradesDataIdMap
        mAdapter?.items = mItems
    }


    override fun clearData() {
        super.clearData()

        mTradesDataIdMap = mutableMapOf()
    }


    fun showItemProgressBar(tradeData: TradeData) {
        updateItem(tradeData.copy(isProgressBarVisible = true))
    }


    fun hideItemProgressBar(tradeData: TradeData) {
        updateItem(tradeData.copy(isProgressBarVisible = false))
    }


    fun addItem(trade: Trade) {
        val newData = (if(isDataEmpty()) mutableListOf() else mData!!.toMutableList()).apply {
            val expectedComparisonResult = (if(mDataParameters.sortOrder == SortOrder.ASC) 1 else -1)
            val newItemPosition = (indices.firstOrNull { this[it].compareTo(trade) == expectedComparisonResult } ?: size)

            add(newItemPosition, trade)
        }

        setData(newData, false)
        bindDataInternal {
            if(mIsHighlightingEnabled && (it.id == trade.id)) {
                (System.currentTimeMillis() + mItemHighlightDuration)
            } else {
                BaseTradingListData.NO_TIMESTAMP
            }
        }
    }


    fun removeItem(trade: Trade) {
        if(isDataEmpty()) {
            return
        }

        val data = mData!!
        val itemPosition = data.indexOfFirstOrNull { it.id == trade.id } ?: return
        val newData = data.toMutableList().apply {
            removeAt(itemPosition)
        }

        setData(newData, true)
    }


    private fun updateItem(tradeData: TradeData) {
        if(mItems.isEmpty()) {
            return
        }

        val itemPosition = mItems.indexOfFirstOrNull {
            ((it is TradeHistoryItem) && (it.itemModel.trade.id == tradeData.trade.id))
        } ?: return
        val newItem = TradeHistoryItem(tradeData)

        mTradesDataIdMap[tradeData.trade.id] = tradeData
        mAdapter?.updateItemWith(itemPosition, newItem)
    }


    fun setDataTruncationEnabled(isEnabled: Boolean) {
        mIsDataTruncationEnabled = isEnabled
    }


    fun setAdjustingToTargetCountEnabled(isEnabled: Boolean) {
        mIsAdjustingToTargetCountEnabled = isEnabled
    }


    fun setItemSwipeMenuEnabled(isEnabled: Boolean) {
        mIsItemSwipeMenuEnabled = isEnabled
    }


    fun setTradesLimit(limit: Int) {
        mDataParameters = mDataParameters.copy(count = limit)
    }


    fun setTradeTimeColor(@ColorInt color: Int) {
        mTradeTimeColor = color
        updateResources()
    }


    fun setCancelButtonTextColor(@ColorInt color: Int) {
        mCancelButtonTextColor = color
        updateResources()
    }


    fun setCancellationProgressBarColor(@ColorInt color: Int) {
        mCancellationProgressBarColor = color
        updateResources()
    }


    fun setCurrencyPairId(currencyPairId: Int) {
        mDataParameters = mDataParameters.copy(currencyPairId = currencyPairId)
    }


    override fun isDataEmpty(): Boolean {
        return (mData?.isEmpty() ?: true)
    }


    fun containsItem(trade: Trade): Boolean {
        if(isDataEmpty()) {
            return false
        }

        return mData!!.any { it.id == trade.id }
    }


    override fun getDefaultInfoViewIconResourceId(): Int {
        return R.mipmap.ic_active_orders_stub
    }


    override fun getLayoutResourceId(): Int = R.layout.trade_history_view_layout


    override fun getDefaultParameters(): TradeHistoryParameters {
        return TradeHistoryParameters.getDefaultParameters()
    }


    fun getRecyclerViewChildViewHolder(adapterPosition: Int): RecyclerView.ViewHolder? {
        return recyclerView.findViewHolderForAdapterPosition(adapterPosition)
    }


    override fun getLayoutManager(): LinearLayoutManager = mLayoutManager


    override fun getProgressBar(): ProgressBar = progressBar


    override fun getInfoView(): InfoView = infoView


    override fun getMainView(): View = recyclerView


    private val mLayoutManager = object : LinearLayoutManager(context) {

        override fun canScrollVertically(): Boolean {
            return mCanRecyclerViewScroll
        }

    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        if(state is SavedState) {
            setData(state.data, true)
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    override fun onSaveInstanceState(savedState: BaseTradingSavedState<TradeHistoryParameters>) {
        super.onSaveInstanceState(savedState)

        if(savedState is SavedState) {
            with(savedState) {
                data = mData
            }
        }
    }


    private class SavedState : BaseTradingSavedState<TradeHistoryParameters> {

        companion object {

            private const val KEY_DATA = "data"


            @JvmField
            var CREATOR = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)

            }

        }


        internal var data: List<Trade>? = null


        @Suppress("UNCHECKED_CAST")
        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                data = (getSerializable(KEY_DATA) as? List<Trade>)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putSerializable(KEY_DATA, (data as? Serializable))
            })
        }

    }


}