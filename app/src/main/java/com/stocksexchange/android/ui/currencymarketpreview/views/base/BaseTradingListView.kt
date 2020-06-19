package com.stocksexchange.android.ui.currencymarketpreview.views.base

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.recyclerview.BaseRecyclerViewAdapter
import com.stocksexchange.android.ui.currencymarketpreview.views.base.interfaces.BaseTradingListData
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.extensions.setBottomPadding
import com.stocksexchange.core.utils.extensions.setLeftPadding
import com.stocksexchange.core.utils.extensions.setTopPadding

/**
 * A base list view that contains common functionality for trading
 * views with list data.
 */
abstract class BaseTradingListView<
    Data,
    DataItem,
    Params: Parcelable,
    Adapter: BaseRecyclerViewAdapter<*, *>
> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseTradingView<Data, DataItem, Params>(context, attrs, defStyleAttr) {


    companion object {

        internal const val ATTRIBUTE_KEY_IS_HIGHLIGHTING_ENABLED = "is_highlighting_enabled"
        internal const val ATTRIBUTE_KEY_RV_HEIGHT = "rv_height"
        internal const val ATTRIBUTE_KEY_RV_ITEM_BOTTOM_SPACING = "rv_item_bottom_spacing"
        internal const val ATTRIBUTE_KEY_PRICE_MAX_CHARS_LENGTH = "price_max_chars_length"
        internal const val ATTRIBUTE_KEY_AMOUNT_MAX_CHARS_LENGTH = "amount_max_chars_length"
        internal const val ATTRIBUTE_KEY_HEADER_TITLE_TEXT_COLOR = "header_title_text_color"
        internal const val ATTRIBUTE_KEY_HEADER_SEPARATOR_COLOR = "header_separator_color"
        internal const val ATTRIBUTE_KEY_SELL_HIGHLIGHT_BACKGROUND_COLOR = "sell_highlight_background_color"
        internal const val ATTRIBUTE_KEY_BUY_HIGHLIGHT_BACKGROUND_COLOR = "buy_highlight_background_color"
        internal const val ATTRIBUTE_KEY_SELL_PRICE_HIGHLIGHT_COLOR = "sell_price_highlight_color"
        internal const val ATTRIBUTE_KEY_BUY_PRICE_HIGHLIGHT_COLOR = "buy_price_highlight_color"
        internal const val ATTRIBUTE_KEY_SELL_PRICE_COLOR = "sell_price_color"
        internal const val ATTRIBUTE_KEY_BUY_PRICE_COLOR = "buy_price_color"
        internal const val ATTRIBUTE_KEY_AMOUNT_COLOR = "amount_color"
        internal const val ATTRIBUTE_KEY_ITEM_HIGHLIGHT_DURATION = "item_highlight_duration"

        internal const val DEFAULT_IS_HIGHLIGHTING_ENABLED = true

        internal const val DEFAULT_RV_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT
        internal const val DEFAULT_RV_ITEM_BOTTOM_SPACING_IN_DP = 1

        internal const val DEFAULT_PRICE_MAX_CHARS_LENGTH = 10
        internal const val DEFAULT_AMOUNT_MAX_CHARS_LENGTH = 10

        internal const val DEFAULT_HEADER_TITLE_TEXT_COLOR = Color.GRAY
        internal const val DEFAULT_HEADER_SEPARATOR_COLOR = Color.LTGRAY
        internal const val DEFAULT_SELL_HIGHLIGHT_BACKGROUND_COLOR = Color.RED
        internal const val DEFAULT_BUY_HIGHLIGHT_BACKGROUND_COLOR = Color.GREEN
        internal const val DEFAULT_SELL_PRICE_HIGHLIGHT_COLOR = Color.RED
        internal const val DEFAULT_BUY_PRICE_HIGHLIGHT_COLOR = Color.GREEN
        internal const val DEFAULT_SELL_PRICE_COLOR = Color.RED
        internal const val DEFAULT_BUY_PRICE_COLOR = Color.GREEN
        internal const val DEFAULT_AMOUNT_COLOR = Color.WHITE

        internal const val DEFAULT_ITEM_HIGHLIGHT_DURATION = 2000

        internal const val DEFAULT_STUB_TEXT = "- -"

    }


    protected var mIsHighlightingEnabled: Boolean = true
    protected var mCanRecyclerViewScroll: Boolean = true

    protected var mRvItemBottomSpacing: Int = 0

    protected var mPriceMaxCharsLength: Int = 0
    protected var mAmountMaxCharsLength: Int = 0

    protected var mHeaderTitleTextColor: Int = 0
    protected var mHeaderSeparatorColor: Int = 0
    protected var mSellBackgroundHighlightColor: Int = 0
    protected var mBuyBackgroundHighlightColor: Int = 0
    protected var mSellPriceHighlightColor: Int = 0
    protected var mBuyPriceHighlightColor: Int = 0
    protected var mSellPriceColor: Int = 0
    protected var mBuyPriceColor: Int = 0
    protected var mAmountColor: Int = 0

    protected var mItemHighlightDuration: Int = 0

    protected var mItems: MutableList<BaseItem<*, *, *>> = mutableListOf()

    protected var mAdapter: Adapter? = null




    override fun init() {
        super.init()

        initRecyclerView()
    }


    protected abstract fun initRecyclerView()


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setHighlightingEnabled(get(ATTRIBUTE_KEY_IS_HIGHLIGHTING_ENABLED, DEFAULT_IS_HIGHLIGHTING_ENABLED))
            setRecyclerViewHeight(get(ATTRIBUTE_KEY_RV_HEIGHT, DEFAULT_RV_HEIGHT))
            setRecyclerViewItemBottomSpacing(get(ATTRIBUTE_KEY_RV_ITEM_BOTTOM_SPACING, dpToPx(DEFAULT_RV_ITEM_BOTTOM_SPACING_IN_DP)))
            setPriceMaxCharsLength(get(ATTRIBUTE_KEY_PRICE_MAX_CHARS_LENGTH, DEFAULT_PRICE_MAX_CHARS_LENGTH))
            setAmountMaxCharsLength(get(ATTRIBUTE_KEY_AMOUNT_MAX_CHARS_LENGTH, DEFAULT_AMOUNT_MAX_CHARS_LENGTH))
            setHeaderTitleTextColor(get(ATTRIBUTE_KEY_HEADER_TITLE_TEXT_COLOR, DEFAULT_HEADER_TITLE_TEXT_COLOR))
            setHeaderSeparatorColor(get(ATTRIBUTE_KEY_HEADER_SEPARATOR_COLOR, DEFAULT_HEADER_SEPARATOR_COLOR))
            setSellHighlightBackgroundColor(get(ATTRIBUTE_KEY_SELL_HIGHLIGHT_BACKGROUND_COLOR, DEFAULT_SELL_HIGHLIGHT_BACKGROUND_COLOR))
            setBuyHighlightBackgroundColor(get(ATTRIBUTE_KEY_BUY_HIGHLIGHT_BACKGROUND_COLOR, DEFAULT_BUY_HIGHLIGHT_BACKGROUND_COLOR))
            setSellPriceHighlightColor(get(ATTRIBUTE_KEY_SELL_PRICE_HIGHLIGHT_COLOR, DEFAULT_SELL_PRICE_HIGHLIGHT_COLOR))
            setBuyPriceHighlightColor(get(ATTRIBUTE_KEY_BUY_PRICE_HIGHLIGHT_COLOR, DEFAULT_BUY_PRICE_HIGHLIGHT_COLOR))
            setSellPriceColor(get(ATTRIBUTE_KEY_SELL_PRICE_COLOR, DEFAULT_SELL_PRICE_COLOR))
            setBuyPriceColor(get(ATTRIBUTE_KEY_BUY_PRICE_COLOR, DEFAULT_BUY_PRICE_COLOR))
            setAmountColor(get(ATTRIBUTE_KEY_AMOUNT_COLOR, DEFAULT_AMOUNT_COLOR))
            setItemHighlightDuration(get(ATTRIBUTE_KEY_ITEM_HIGHLIGHT_DURATION, DEFAULT_ITEM_HIGHLIGHT_DURATION))
        }
    }


    fun scrollToTop() {
        getRecyclerView().scrollToPosition(0)
    }


    fun setHighlightingEnabled(isHighlightingEnabled: Boolean) {
        mIsHighlightingEnabled = isHighlightingEnabled
    }


    fun setRecyclerViewHeight(height: Int) {
        getRecyclerView().setHeight(height)

        mCanRecyclerViewScroll = (
            (height  == ViewGroup.LayoutParams.MATCH_PARENT) ||
            (height == ViewGroup.LayoutParams.WRAP_CONTENT)
        )
    }


    fun setRecyclerViewTopPadding(padding: Int) {
        getRecyclerView().setTopPadding(padding)
    }


    fun setRecyclerViewBottomPadding(padding: Int) {
        getRecyclerView().setBottomPadding(padding)
    }


    fun setRecyclerViewLeftPadding(padding: Int) {
        getRecyclerView().setLeftPadding(padding)
    }


    fun setRecyclerViewRightPadding(padding: Int) {
        getRecyclerView().setRightPadding(padding)
    }


    fun setRecyclerViewItemBottomSpacing(spacing: Int) {
        mRvItemBottomSpacing = spacing
        updateRecyclerViewItemDecorator()
    }


    protected open fun updateRecyclerViewItemDecorator() {
        // Stub
    }


    fun setPriceMaxCharsLength(priceMaxCharsLength: Int) {
        mPriceMaxCharsLength = priceMaxCharsLength
        updateResources()
    }


    fun setAmountMaxCharsLength(amountMaxCharsLength: Int) {
        mAmountMaxCharsLength = amountMaxCharsLength
        updateResources()
    }


    protected abstract fun updateResources()


    fun setHeaderTitleTextColor(@ColorInt color: Int) {
        mHeaderTitleTextColor = color
        updateResources()
    }


    fun setHeaderSeparatorColor(@ColorInt color: Int) {
        mHeaderSeparatorColor = color
        updateResources()
    }


    fun setSellHighlightBackgroundColor(@ColorInt color: Int) {
        mSellBackgroundHighlightColor = color
        updateResources()
    }


    fun setBuyHighlightBackgroundColor(@ColorInt color: Int) {
        mBuyBackgroundHighlightColor = color
        updateResources()
    }


    fun setSellPriceHighlightColor(@ColorInt color: Int) {
        mSellPriceHighlightColor = color
        updateResources()
    }


    fun setBuyPriceHighlightColor(@ColorInt color: Int) {
        mBuyPriceHighlightColor = color
        updateResources()
    }


    fun setSellPriceColor(@ColorInt color: Int) {
        mSellPriceColor = color
        updateResources()
    }


    fun setBuyPriceColor(@ColorInt color: Int) {
        mBuyPriceColor = color
        updateResources()
    }


    fun setAmountColor(@ColorInt color: Int) {
        mAmountColor = color
        updateResources()
    }


    fun setItemHighlightDuration(itemHighlightDuration: Int) {
        mItemHighlightDuration = itemHighlightDuration
    }

    override fun updateData(data: Data, dataActionItems: List<DataActionItem<DataItem>>) {
        setData(data, false)
    }


    override fun bindData() {
        bindDataInternal {
            BaseTradingListData.NO_TIMESTAMP
        }
    }


    /**
     * An internal method used for binding data.
     *
     * @param getHighlightEndTimestamp The method to retrieve an ending
     * timestamp when the highlighting should stop
     */
    protected abstract fun bindDataInternal(getHighlightEndTimestamp: (DataItem) -> Long)


    override fun clearData() {
        super.clearData()

        mAdapter?.clear()
        mItems.clear()
    }


    private fun getRecyclerView(): RecyclerView {
        return (getMainView() as RecyclerView)
    }


    override fun isDataAlreadyBound(): Boolean {
        return mItems.isNotEmpty()
    }


    abstract fun getLayoutManager(): LinearLayoutManager


}