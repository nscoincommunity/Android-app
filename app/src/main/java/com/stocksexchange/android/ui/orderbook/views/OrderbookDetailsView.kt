package com.stocksexchange.android.ui.orderbook.views

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.model.OrderbookInfo
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.mapviews.DottedMapView
import com.stocksexchange.android.ui.views.detailsviews.BaseDetailsView
import com.stocksexchange.core.formatters.NumberFormatter
import kotlinx.android.synthetic.main.orderbook_details_view_layout.view.*
import org.koin.core.inject

/**
 * An informational view that shows details about an orderbook of
 * some currency pair.
 */
class OrderbookDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseDetailsView<OrderbookInfo>(context, attrs, defStyleAttr) {


    companion object {

        internal const val ATTRIBUTE_KEY_LOWEST_ASK_VALUE_COLOR = "lowest_ask_value_color"
        internal const val ATTRIBUTE_KEY_HIGHEST_BID_VALUE_COLOR = "highest_bid_value_color"
        internal const val ATTRIBUTE_KEY_BASE_CURRENCY_SYMBOL = "base_currency_symbol"
        internal const val ATTRIBUTE_KEY_EMPTY_PRICE_TEXT = "empty_price_text"
        internal const val ATTRIBUTE_KEY_EMPTY_VOLUME_TEXT = "empty_volume_text"

        private const val DEFAULT_LOWEST_ASK_VALUE_COLOR = Color.RED
        private const val DEFAULT_HIGHEST_BID_VALUE_COLOR = Color.GREEN

        private const val DEFAULT_BASE_CURRENCY_SYMBOL = ""
        private const val DEFAULT_EMPTY_PRICE_TEXT = ""
        private const val DEFAULT_EMPTY_VOLUME_TEXT = ""

    }


    private var mBaseCurrencySymbol: String = ""
    private var mEmptyPriceText: String = ""
    private var mEmptyVolumeText: String = ""

    private val mNumberFormatter: NumberFormatter by inject()




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.OrderbookDetailsView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_ITEM_TITLE_COLOR, getInt(R.styleable.OrderbookDetailsView_itemTitleColor, DEFAULT_ITEM_TITLE_COLOR))
                save(ATTRIBUTE_KEY_ITEM_VALUE_COLOR, getInt(R.styleable.OrderbookDetailsView_itemValueColor, DEFAULT_ITEM_VALUE_COLOR))
                save(ATTRIBUTE_KEY_ITEM_SEPARATOR_COLOR, getInt(R.styleable.OrderbookDetailsView_itemSeparatorColor, DEFAULT_ITEM_SEPARATOR_COLOR))
                save(ATTRIBUTE_KEY_LOWEST_ASK_VALUE_COLOR, getInt(R.styleable.OrderbookDetailsView_lowestAskValueColor, DEFAULT_LOWEST_ASK_VALUE_COLOR))
                save(ATTRIBUTE_KEY_HIGHEST_BID_VALUE_COLOR, getInt(R.styleable.OrderbookDetailsView_highestBidValueColor, DEFAULT_HIGHEST_BID_VALUE_COLOR))
                save(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, getInt(R.styleable.OrderbookDetailsView_progressBarColor, DEFAULT_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_KEY_INFO_VIEW_COLOR, getInt(R.styleable.OrderbookDetailsView_infoViewColor, DEFAULT_INFO_VIEW_COLOR))
                save(ATTRIBUTE_KEY_BASE_CURRENCY_SYMBOL, getString(R.styleable.OrderbookDetailsView_baseCurrencySymbol) ?: DEFAULT_BASE_CURRENCY_SYMBOL)
                save(ATTRIBUTE_KEY_EMPTY_PRICE_TEXT, getString(R.styleable.OrderbookDetailsView_emptyPriceText) ?: DEFAULT_EMPTY_PRICE_TEXT)
                save(ATTRIBUTE_KEY_EMPTY_VOLUME_TEXT, getString(R.styleable.OrderbookDetailsView_emptyVolumeText) ?: DEFAULT_EMPTY_VOLUME_TEXT)
            }
        }
    }


    override fun initInfoView() {
        super.initInfoView()

        infoView.hideIcon()
    }


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setLowestAskValueColor(get(ATTRIBUTE_KEY_LOWEST_ASK_VALUE_COLOR, DEFAULT_LOWEST_ASK_VALUE_COLOR))
            setHighestBidValueColor(get(ATTRIBUTE_KEY_HIGHEST_BID_VALUE_COLOR, DEFAULT_HIGHEST_BID_VALUE_COLOR))
            setBaseCurrencySymbol(get(ATTRIBUTE_KEY_BASE_CURRENCY_SYMBOL, DEFAULT_BASE_CURRENCY_SYMBOL))
            setEmptyPriceText(get(ATTRIBUTE_KEY_EMPTY_PRICE_TEXT, DEFAULT_EMPTY_PRICE_TEXT))
            setEmptyVolumeText(get(ATTRIBUTE_KEY_EMPTY_VOLUME_TEXT, DEFAULT_EMPTY_VOLUME_TEXT))
        }
    }


    fun setLowestAskValueColor(@ColorInt color: Int) {
        lowestAskDmv.setValueColor(color)
    }


    fun setHighestBidValueColor(@ColorInt color: Int) {
        highestBidDmv.setValueColor(color)
    }


    fun setBaseCurrencySymbol(symbol: String) {
        mBaseCurrencySymbol = symbol
    }


    /**
     * Sets a text to use when the value of the price is empty.
     *
     * @param text The text to set
     */
    fun setEmptyPriceText(text: String) {
        mEmptyPriceText = text
    }


    /**
     * Sets a text to use when the value of the volume is empty.
     *
     * @param text The text to set
     */
    fun setEmptyVolumeText(text: String) {
        mEmptyVolumeText = text
    }


    override fun updateData(data: OrderbookInfo) {
        if(isDataEmpty()) {
            setData(data, true)
            return
        }

        val oldData = mData!!
        setData(data, false)

        if(data.lowestAsk != oldData.lowestAsk) {
            lowestAskDmv.setValueTextAnimated(getLowestAskString(data))
        }

        if(data.highestBid != oldData.highestBid) {
            highestBidDmv.setValueTextAnimated(getHighestBidString(data))
        }

        if(data.buyVolume != oldData.buyVolume) {
            buyVolumeDmv.setValueTextAnimated(getBuyVolumeString(data))
        }

        if(data.sellVolume != oldData.sellVolume) {
            sellVolumeDmv.setValueTextAnimated(getSellVolumeString(data))
        }
    }


    override fun bindData() {
        if(isDataEmpty()) {
            return
        }

        val data = mData!!

        lowestAskDmv.setValueText(getLowestAskString(data))
        highestBidDmv.setValueText(getHighestBidString(data))
        buyVolumeDmv.setValueText(getBuyVolumeString(data))
        sellVolumeDmv.setValueText(getSellVolumeString(data))
    }


    override fun hasProgressBar(): Boolean = true


    override fun hasInfoView(): Boolean = true


    private fun getLowestAskString(data: OrderbookInfo): String {
        return if(data.hasLowestAsk) {
            mNumberFormatter.formatFixedPrice(data.lowestAsk)
        } else {
            mEmptyPriceText
        }
    }


    private fun getHighestBidString(data: OrderbookInfo): String {
        return if(data.hasHighestBid) {
            mNumberFormatter.formatFixedPrice(data.highestBid)
        } else {
            mEmptyPriceText
        }
    }


    private fun getSellVolumeString(data: OrderbookInfo): String {
        return if(data.hasSellVolume) {
            "${mNumberFormatter.formatAmount(data.sellVolume)} $mBaseCurrencySymbol"
        } else {
            mEmptyVolumeText
        }
    }


    private fun getBuyVolumeString(data: OrderbookInfo): String {
        return if(data.hasBuyVolume) {
            "${mNumberFormatter.formatAmount(data.buyVolume)} $mBaseCurrencySymbol"
        } else {
            mEmptyVolumeText
        }
    }


    override fun getLayoutResourceId(): Int = R.layout.orderbook_details_view_layout


    override fun getProgressBar(): ProgressBar? = progressBar


    override fun getInfoView(): InfoView? = infoView


    override fun getMainView(): View = mainContainerLl


    override fun getDottedMapViewsTitlesArray(): Array<String> {
        return mStringProvider.getStringArray(
            R.string.orderbook_details_view_lowest_ask_dmv_title,
            R.string.orderbook_details_view_highest_bid_dmv_title,
            R.string.orderbook_details_view_buy_volume_dmv_title,
            R.string.orderbook_details_view_sell_volume_dmv_title
        )
    }


    override fun getDottedMapViewsArray(): Array<DottedMapView> {
        return arrayOf(
            lowestAskDmv,
            highestBidDmv,
            buyVolumeDmv,
            sellVolumeDmv
        )
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    private class SavedState : BaseDetailsSavedState<OrderbookInfo> {

        companion object {

            @JvmField
            var CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }

            }

        }


        constructor(parcel: Parcel): super(parcel)

        constructor(superState: Parcelable?): super(superState)

    }


}