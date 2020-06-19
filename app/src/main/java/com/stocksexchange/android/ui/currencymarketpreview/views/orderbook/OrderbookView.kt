package com.stocksexchange.android.ui.currencymarketpreview.views.orderbook

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.OrderbookOrder
import com.stocksexchange.android.mappings.mapToPriceOrderbookOrderActionItemsMap
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.model.DataActionItem.Action.INSERT
import com.stocksexchange.android.model.DataActionItem.Action.UPDATE
import com.stocksexchange.android.model.OrderbookHeader
import com.stocksexchange.android.model.OrderbookOrderData
import com.stocksexchange.android.model.OrderbookOrderData.Companion.VOLUME_LEVEL_MAX_VALUE
import com.stocksexchange.android.model.OrderbookOrderType
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.currencymarketpreview.views.base.BaseTradingListView
import com.stocksexchange.android.ui.currencymarketpreview.views.base.interfaces.BaseTradingListData
import com.stocksexchange.android.ui.currencymarketpreview.views.orderbook.items.OrderbookHeaderItem
import com.stocksexchange.android.ui.currencymarketpreview.views.orderbook.items.OrderbookOrderItem
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.core.decorators.OrderbookItemDecorator
import com.stocksexchange.core.utils.extensions.disableAnimations
import com.stocksexchange.core.utils.extensions.dpToPx
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import kotlinx.android.synthetic.main.orderbook_view_layout.view.*
import kotlin.math.roundToInt

/**
 * A list view that displays an orderbook of a particular currency pair.
 */
class OrderbookView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTradingListView<
    Orderbook,
    OrderbookOrder,
    OrderbookParameters,
    OrderbookRecyclerViewAdapter
>(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_SHOULD_HIDE_HEADER_MORE_BUTTON = "should_hide_header_more_button"
        private const val ATTRIBUTE_KEY_RV_ITEM_HORIZONTAL_SPACING = "rv_item_horizontal_spacing"
        private const val ATTRIBUTE_KEY_ORDERS_OF_TYPE_COUNT_LIMIT = "orders_of_type_count_limit"
        private const val ATTRIBUTE_KEY_HEADER_MORE_BUTTON_COLOR = "header_more_button_color"
        private const val ATTRIBUTE_KEY_SELL_ORDER_BACKGROUND_COLOR = "sell_order_background_color"
        private const val ATTRIBUTE_KEY_BUY_ORDER_BACKGROUND_COLOR = "buy_order_background_color"

        private const val DEFAULT_SHOULD_HIDE_HEADER_MORE_BUTTON = false

        private const val DEFAULT_RV_ITEM_HORIZONTAL_SPACING_IN_DP = 4

        private const val DEFAULT_ORDERS_OF_TYPE_COUNT_LIMIT = 15

        private const val DEFAULT_HEADER_MORE_BUTTON_COLOR = Color.GRAY
        private const val DEFAULT_ORDER_BACKGROUND_COLOR = Color.TRANSPARENT

        private const val ORDERS_OF_TYPE_COUNT_NO_LIMIT = -1

        private const val RECYCLER_VIEW_COLUMN_COUNT = 2

        private const val RECYCLER_VIEW_ASK_HEADER_POSITION = 0
        private const val RECYCLER_VIEW_BID_HEADER_POSITION = 1

    }


    private var mShouldHideHeaderMoreButton: Boolean = false

    private var mRvItemHorizontalSpacing: Int = 0

    private var mOrdersOfTypeCountLimit: Int = 0

    private var mHeaderMoreButtonColor: Int = 0
    private var mSellOrderBackgroundColor: Int = 0
    private var mBuyOrderBackgroundColor: Int = 0

    private var mPriceOrderbookOrdersDataMap: Map<Double, OrderbookOrderData> = mapOf()

    private var mOrderbookResources = OrderbookResources.getDefaultResources(mNumberFormatter)

    private var mRecyclerViewDecorator: OrderbookItemDecorator? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.OrderbookView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_IS_HIGHLIGHTING_ENABLED, getBoolean(R.styleable.OrderbookView_isHighlightingEnabled, DEFAULT_IS_HIGHLIGHTING_ENABLED))
                save(ATTRIBUTE_KEY_SHOULD_HIDE_HEADER_MORE_BUTTON, getBoolean(R.styleable.OrderbookView_shouldHideHeaderMoreButton, DEFAULT_SHOULD_HIDE_HEADER_MORE_BUTTON))
                save(ATTRIBUTE_KEY_RV_HEIGHT, getLayoutDimension(R.styleable.OrderbookView_rvHeight, DEFAULT_RV_HEIGHT))
                save(ATTRIBUTE_KEY_RV_ITEM_HORIZONTAL_SPACING, getDimensionPixelSize(R.styleable.OrderbookView_rvItemHorizontalSpacing, dpToPx(DEFAULT_RV_ITEM_HORIZONTAL_SPACING_IN_DP)))
                save(ATTRIBUTE_KEY_RV_ITEM_BOTTOM_SPACING, getDimensionPixelSize(R.styleable.OrderbookView_rvItemBottomSpacing, dpToPx(DEFAULT_RV_ITEM_BOTTOM_SPACING_IN_DP)))
                save(ATTRIBUTE_KEY_ORDERS_OF_TYPE_COUNT_LIMIT, getInteger(R.styleable.OrderbookView_ordersOfTypeCountLimit, DEFAULT_ORDERS_OF_TYPE_COUNT_LIMIT))
                save(ATTRIBUTE_KEY_PRICE_MAX_CHARS_LENGTH, getInteger(R.styleable.OrderbookView_priceMaxCharsLength, DEFAULT_PRICE_MAX_CHARS_LENGTH))
                save(ATTRIBUTE_KEY_AMOUNT_MAX_CHARS_LENGTH, getInteger(R.styleable.OrderbookView_amountMaxCharsLength, DEFAULT_AMOUNT_MAX_CHARS_LENGTH))
                save(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, getColor(R.styleable.OrderbookView_progressBarColor, DEFAULT_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_KEY_INFO_VIEW_COLOR, getColor(R.styleable.OrderbookView_infoViewColor, DEFAULT_INFO_VIEW_COLOR))
                save(ATTRIBUTE_KEY_HEADER_TITLE_TEXT_COLOR, getColor(R.styleable.OrderbookView_headerTitleTextColor, DEFAULT_HEADER_TITLE_TEXT_COLOR))
                save(ATTRIBUTE_KEY_HEADER_MORE_BUTTON_COLOR, getColor(R.styleable.OrderbookView_headerMoreButtonColor, DEFAULT_HEADER_MORE_BUTTON_COLOR))
                save(ATTRIBUTE_KEY_HEADER_SEPARATOR_COLOR, getColor(R.styleable.OrderbookView_headerSeparatorColor, DEFAULT_HEADER_SEPARATOR_COLOR))
                save(ATTRIBUTE_KEY_SELL_HIGHLIGHT_BACKGROUND_COLOR, getColor(R.styleable.OrderbookView_sellOrderHighlightBackgroundColor, DEFAULT_SELL_HIGHLIGHT_BACKGROUND_COLOR))
                save(ATTRIBUTE_KEY_BUY_HIGHLIGHT_BACKGROUND_COLOR, getColor(R.styleable.OrderbookView_buyOrderHighlightBackgroundColor, DEFAULT_BUY_HIGHLIGHT_BACKGROUND_COLOR))
                save(ATTRIBUTE_KEY_SELL_PRICE_HIGHLIGHT_COLOR, getColor(R.styleable.OrderbookView_askOrderPriceHighlightColor, DEFAULT_SELL_PRICE_HIGHLIGHT_COLOR))
                save(ATTRIBUTE_KEY_BUY_PRICE_HIGHLIGHT_COLOR, getColor(R.styleable.OrderbookView_bidOrderPriceHighlightColor, DEFAULT_BUY_PRICE_HIGHLIGHT_COLOR))
                save(ATTRIBUTE_KEY_SELL_PRICE_COLOR, getColor(R.styleable.OrderbookView_askOrderPriceColor, DEFAULT_SELL_PRICE_COLOR))
                save(ATTRIBUTE_KEY_BUY_PRICE_COLOR, getColor(R.styleable.OrderbookView_bidOrderPriceColor, DEFAULT_BUY_PRICE_COLOR))
                save(ATTRIBUTE_KEY_AMOUNT_COLOR, getColor(R.styleable.OrderbookView_orderAmountColor, DEFAULT_AMOUNT_COLOR))
                save(ATTRIBUTE_KEY_SELL_ORDER_BACKGROUND_COLOR, getColor(R.styleable.OrderbookView_askOrderBackgroundColor, DEFAULT_ORDER_BACKGROUND_COLOR))
                save(ATTRIBUTE_KEY_BUY_ORDER_BACKGROUND_COLOR, getColor(R.styleable.OrderbookView_bidOrderBackgroundColor, DEFAULT_ORDER_BACKGROUND_COLOR))
                save(ATTRIBUTE_KEY_ITEM_HIGHLIGHT_DURATION, getColor(R.styleable.OrderbookView_itemHighlightDuration, DEFAULT_ITEM_HIGHLIGHT_DURATION))
                save(ATTRIBUTE_KEY_INFO_VIEW_ICON, (getDrawable(R.styleable.OrderbookView_infoViewIcon) ?: getCompatDrawable(getDefaultInfoViewIconResourceId())))
            }
        }
    }


    override fun initRecyclerView() {
        with(recyclerView) {
            disableAnimations()

            layoutManager = mLayoutManager

            mAdapter = OrderbookRecyclerViewAdapter(context, mItems)
            mAdapter?.setHasStableIds(true)
            mAdapter?.setResources(mOrderbookResources)

            adapter = mAdapter
        }
    }


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setShouldHideHeaderMoreButton(get(ATTRIBUTE_KEY_SHOULD_HIDE_HEADER_MORE_BUTTON, DEFAULT_SHOULD_HIDE_HEADER_MORE_BUTTON))
            setRecyclerViewHorizontalSpacing(get(ATTRIBUTE_KEY_RV_ITEM_HORIZONTAL_SPACING, dpToPx(DEFAULT_RV_ITEM_HORIZONTAL_SPACING_IN_DP)))
            setOrdersOfTypeCountLimit(get(ATTRIBUTE_KEY_ORDERS_OF_TYPE_COUNT_LIMIT, DEFAULT_ORDERS_OF_TYPE_COUNT_LIMIT))
            setHeaderMoreButtonColor(get(ATTRIBUTE_KEY_HEADER_MORE_BUTTON_COLOR, DEFAULT_HEADER_MORE_BUTTON_COLOR))
            setSellOrderBackgroundColor(get(ATTRIBUTE_KEY_SELL_ORDER_BACKGROUND_COLOR, DEFAULT_ORDER_BACKGROUND_COLOR))
            setBuyOrderBackgroundColor(get(ATTRIBUTE_KEY_BUY_ORDER_BACKGROUND_COLOR, DEFAULT_ORDER_BACKGROUND_COLOR))
        }
    }


    private fun hasOrdersOfTypeCountLimit(): Boolean {
        return (mOrdersOfTypeCountLimit != ORDERS_OF_TYPE_COUNT_NO_LIMIT)
    }


    override fun updateRecyclerViewItemDecorator() {
        with(recyclerView) {
            if(mRecyclerViewDecorator != null) {
                removeItemDecoration(mRecyclerViewDecorator!!)
            }

            mRecyclerViewDecorator = object : OrderbookItemDecorator(
                RECYCLER_VIEW_COLUMN_COUNT,
                mRvItemHorizontalSpacing,
                mRvItemBottomSpacing
            ) {

                override fun isHeader(adapterPosition: Int, view: View): Boolean {
                    return ((adapterPosition == RECYCLER_VIEW_ASK_HEADER_POSITION) ||
                            (adapterPosition == RECYCLER_VIEW_BID_HEADER_POSITION))
                }

            }

            addItemDecoration(mRecyclerViewDecorator!!)
        }
    }


    override fun updateResources() {
        mOrderbookResources = OrderbookResources.newInstance(
            mShouldHideHeaderMoreButton,
            mPriceMaxCharsLength,
            mAmountMaxCharsLength,
            DEFAULT_STUB_TEXT,
            mNumberFormatter,
            getColorsForResources()
        )

        mAdapter?.setResources(mOrderbookResources)
    }


    private fun getColorsForResources(): List<Int> {
        return listOf(
            mHeaderTitleTextColor,
            mHeaderMoreButtonColor,
            mHeaderSeparatorColor,
            mSellBackgroundHighlightColor,
            mBuyBackgroundHighlightColor,
            mSellPriceHighlightColor,
            mBuyPriceHighlightColor,
            mSellPriceColor,
            mBuyPriceColor,
            mAmountColor,
            mSellOrderBackgroundColor,
            mBuyOrderBackgroundColor
        )
    }


    override fun truncateData(data: Orderbook?): Orderbook? {
        if(!hasOrdersOfTypeCountLimit()) {
            return data
        }

        return data?.truncate(mOrdersOfTypeCountLimit, mOrdersOfTypeCountLimit)
    }


    override fun updateData(data: Orderbook, dataActionItems: List<DataActionItem<OrderbookOrder>>) {
        super.updateData(data, dataActionItems)

        val priceOrderbookOrderActionItemsMap = dataActionItems.mapToPriceOrderbookOrderActionItemsMap()

        bindDataInternal {
            if(mIsHighlightingEnabled) {
                if(priceOrderbookOrderActionItemsMap.containsKey(it.price)) {
                    if(priceOrderbookOrderActionItemsMap[it.price]?.action in listOf(INSERT, UPDATE)) {
                        (System.currentTimeMillis() + mItemHighlightDuration)
                    } else {
                        BaseTradingListData.NO_TIMESTAMP
                    }
                } else if(mPriceOrderbookOrdersDataMap.containsKey(it.price)) {
                    mPriceOrderbookOrdersDataMap[it.price]?.highlightEndTimestamp ?: BaseTradingListData.NO_TIMESTAMP
                } else {
                    BaseTradingListData.NO_TIMESTAMP
                }
            } else {
                BaseTradingListData.NO_TIMESTAMP
            }
        }
    }


    override fun bindDataInternal(getHighlightEndTimestamp: (OrderbookOrder) -> Long) {
        if(isDataEmpty()) {
            return
        }

        val orderbook = mData!!
        val priceOrderbookOrdersDataMap: MutableMap<Double, OrderbookOrderData> = mutableMapOf()

        mItems.clear()
        mItems.add(OrderbookHeaderItem(OrderbookHeader(
            mStringProvider.getString(R.string.action_ask),
            OrderbookOrderType.ASK
        )))
        mItems.add(OrderbookHeaderItem(OrderbookHeader(
            mStringProvider.getString(R.string.action_bid),
            OrderbookOrderType.BID
        )))

        val stubOrder = OrderbookOrder.STUB_ORDERBOOK_ORDER
        val sellOrders = orderbook.sellOrders
        val buyOrders = orderbook.buyOrders
        val sellOrdersVolume = orderbook.sellOrdersVolume
        val buyOrdersVolume = orderbook.buyOrdersVolume
        var sellOrderVolume = 0.0
        var buyOrderVolume = 0.0
        var sellVolumeLevelPercentage: Double
        var buyVolumeLevelPercentage: Double
        var sellOrder: OrderbookOrder
        var buyOrder: OrderbookOrder
        var sellOrderData: OrderbookOrderData
        var buyOrderData: OrderbookOrderData

        val ordersCount = if(hasOrdersOfTypeCountLimit()) {
            mOrdersOfTypeCountLimit
        } else {
            orderbook.largestOrdersCount
        }

        for(i in 0 until ordersCount) {
            sellOrder = sellOrders.getOrNull(i) ?: stubOrder
            buyOrder = buyOrders.getOrNull(i) ?: stubOrder

            sellOrderVolume += if(sellOrder.isStub) 0.0 else sellOrder.amount
            buyOrderVolume += if(buyOrder.isStub) 0.0 else buyOrder.amount

            sellVolumeLevelPercentage = if(sellOrder.isStub) 0.0 else (sellOrderVolume / sellOrdersVolume)
            buyVolumeLevelPercentage = if(buyOrder.isStub) 0.0 else (buyOrderVolume / buyOrdersVolume)

            sellOrderData = OrderbookOrderData(
                OrderbookOrderType.ASK,
                sellOrder,
                (sellVolumeLevelPercentage * VOLUME_LEVEL_MAX_VALUE).roundToInt(),
                getHighlightEndTimestamp(sellOrder)
            )
            buyOrderData = OrderbookOrderData(
                OrderbookOrderType.BID,
                buyOrder,
                (buyVolumeLevelPercentage * VOLUME_LEVEL_MAX_VALUE).roundToInt(),
                getHighlightEndTimestamp(buyOrder)
            )

            priceOrderbookOrdersDataMap[sellOrderData.order.price] = sellOrderData
            priceOrderbookOrdersDataMap[buyOrderData.order.price] = buyOrderData

            mItems.add(OrderbookOrderItem(sellOrderData))
            mItems.add(OrderbookOrderItem(buyOrderData))
        }

        mPriceOrderbookOrdersDataMap = priceOrderbookOrdersDataMap
        mAdapter?.items = mItems
    }


    override fun clearData() {
        super.clearData()

        mPriceOrderbookOrdersDataMap = emptyMap()
    }

    fun getSelectedAmount(orderbookOrderData: OrderbookOrderData): Double {
        var selectedAmount = 0.0
        mPriceOrderbookOrdersDataMap.forEach {
            if (it.value.type == orderbookOrderData.type) {
                when (orderbookOrderData.type) {
                    OrderbookOrderType.BID -> if (it.value.order.price >= orderbookOrderData.order.price) {
                        selectedAmount += it.value.order.amount
                    }
                    OrderbookOrderType.ASK -> if (it.value.order.price <= orderbookOrderData.order.price) {
                        selectedAmount += it.value.order.amount
                    }
                }
            }
        }

        return selectedAmount
    }


    /**
     * Disables a limit for each order's type.
     */
    fun disableOrdersOfTypeCountLimit() {
        mOrdersOfTypeCountLimit = ORDERS_OF_TYPE_COUNT_NO_LIMIT
    }


    fun setShouldHideHeaderMoreButton(isHidden: Boolean) {
        mShouldHideHeaderMoreButton = isHidden
        updateResources()
    }


    fun setRecyclerViewHorizontalSpacing(spacing: Int) {
        mRvItemHorizontalSpacing = spacing
        updateRecyclerViewItemDecorator()
    }


    fun setOrdersOfTypeCountLimit(limit: Int) {
        mOrdersOfTypeCountLimit = limit
    }


    fun setHeaderMoreButtonColor(@ColorInt color: Int) {
        mHeaderMoreButtonColor = color
        updateResources()
    }


    fun setSellOrderBackgroundColor(@ColorInt color: Int) {
        mSellOrderBackgroundColor = color
        updateResources()
    }


    fun setBuyOrderBackgroundColor(@ColorInt color: Int) {
        mBuyOrderBackgroundColor = color
        updateResources()
    }


    fun setCurrencyPairId(currencyPairId: Int) {
        mDataParameters = mDataParameters.copy(currencyPairId = currencyPairId)
    }


    fun setOnHeaderMoreButtonClickListener(listener: ((View, OrderbookHeaderItem, Int) -> Unit)) {
        mAdapter?.onHeaderMoreButtonClickListener = listener
    }


    fun setOnItemClickListener(listener: ((View, OrderbookOrderItem, Int) -> Unit)) {
        mAdapter?.onItemClickListener = listener
    }


    override fun isDataEmpty(): Boolean {
        return (mData?.isEmpty ?: true)
    }


    override fun getDefaultInfoViewIconResourceId(): Int {
        return R.mipmap.ic_orderbook_stub
    }


    override fun getLayoutResourceId(): Int = R.layout.orderbook_view_layout


    override fun getDefaultParameters(): OrderbookParameters {
        return OrderbookParameters.getDefaultParameters()
    }


    override fun getLayoutManager(): LinearLayoutManager = mLayoutManager


    override fun getProgressBar(): ProgressBar = progressBar


    override fun getInfoView(): InfoView = infoView


    override fun getMainView(): View = recyclerView


    private val mLayoutManager = object : GridLayoutManager(
        context, RECYCLER_VIEW_COLUMN_COUNT, RecyclerView.VERTICAL, false
    ) {

        override fun canScrollVertically(): Boolean {
            return mCanRecyclerViewScroll
        }

    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        if(state is SavedState) {
            setOrdersOfTypeCountLimit(state.ordersOfTypeCountLimit)
            setData(state.data, true)
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    override fun onSaveInstanceState(savedState: BaseTradingSavedState<OrderbookParameters>) {
        super.onSaveInstanceState(savedState)

        if(savedState is SavedState) {
            with(savedState) {
                ordersOfTypeCountLimit = mOrdersOfTypeCountLimit
                data = mData
            }
        }
    }


    private class SavedState : BaseTradingSavedState<OrderbookParameters> {

        companion object {

            private const val KEY_ORDERS_OF_TYPE_COUNT_LIMIT = "orders_of_type_count_limit"
            private const val KEY_DATA = "data"


            @JvmField
            var CREATOR = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)

            }

        }


        internal var ordersOfTypeCountLimit: Int = 0

        internal var data: Orderbook? = null


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                ordersOfTypeCountLimit = getInt(KEY_ORDERS_OF_TYPE_COUNT_LIMIT)
                data = getParcelable(KEY_DATA)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putInt(KEY_ORDERS_OF_TYPE_COUNT_LIMIT, ordersOfTypeCountLimit)
                putParcelable(KEY_DATA, data)
            })
        }

    }


}