package com.stocksexchange.android.ui.currencymarketpreview.views

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
import com.stocksexchange.android.model.CurrencyMarketDetails
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.mapviews.DottedMapView
import com.stocksexchange.android.ui.views.detailsviews.BaseDetailsView
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.truncate
import kotlinx.android.synthetic.main.market_details_view_layout.view.*
import org.koin.core.inject

/**
 * An informational view that shows details about a particular market.
 */
class MarketDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseDetailsView<CurrencyMarketDetails>(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_CURRENCY_NAME_CHARACTER_LIMIT = "currency_name_character_limit"
        private const val ATTRIBUTE_KEY_POSITIVE_STATUS_COLOR = "positive_status_color"
        private const val ATTRIBUTE_KEY_NEGATIVE_STATUS_COLOR = "negative_status_color"

        private const val DEFAULT_CURRENCY_NAME_CHARACTER_LIMIT = 17

        private const val DEFAULT_POSITIVE_STATUS_COLOR = Color.GREEN
        private const val DEFAULT_NEGATIVE_STATUS_COLOR = Color.RED

    }


    private var mIsStatusActive: Boolean = false

    private var mCurrencyNameCharacterLimit: Int = 0

    private var mPositiveStatusColor: Int = 0
    private var mNegativeStatusColor: Int = 0

    private var mPositiveStatusString: String = ""
    private var mNegativeStatusString: String = ""

    private val mNumberFormatter: NumberFormatter by inject()




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.MarketDetailsView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_CURRENCY_NAME_CHARACTER_LIMIT, getInteger(R.styleable.MarketDetailsView_currencyNameCharacterLimit, DEFAULT_CURRENCY_NAME_CHARACTER_LIMIT))
                save(ATTRIBUTE_KEY_ITEM_TITLE_COLOR, getColor(R.styleable.MarketDetailsView_itemTitleColor, DEFAULT_ITEM_TITLE_COLOR))
                save(ATTRIBUTE_KEY_ITEM_VALUE_COLOR, getColor(R.styleable.MarketDetailsView_itemValueColor, DEFAULT_ITEM_VALUE_COLOR))
                save(ATTRIBUTE_KEY_ITEM_SEPARATOR_COLOR, getColor(R.styleable.MarketDetailsView_itemSeparatorColor, DEFAULT_ITEM_SEPARATOR_COLOR))
                save(ATTRIBUTE_KEY_POSITIVE_STATUS_COLOR, getColor(R.styleable.MarketDetailsView_positiveStatusColor, DEFAULT_POSITIVE_STATUS_COLOR))
                save(ATTRIBUTE_KEY_NEGATIVE_STATUS_COLOR, getColor(R.styleable.MarketDetailsView_negativeStatusColor, DEFAULT_NEGATIVE_STATUS_COLOR))
            }
        }
    }


    override fun init() {
        super.init()

        initStrings()
    }


    private fun initStrings() {
        mPositiveStatusString = mStringProvider.getString(R.string.pair_state_active)
        mNegativeStatusString = mStringProvider.getString(R.string.pair_state_disabled)
    }


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setCurrencyNameCharacterLimit(get(ATTRIBUTE_KEY_CURRENCY_NAME_CHARACTER_LIMIT, DEFAULT_CURRENCY_NAME_CHARACTER_LIMIT))
            setPositiveStatusColor(get(ATTRIBUTE_KEY_POSITIVE_STATUS_COLOR, DEFAULT_POSITIVE_STATUS_COLOR))
            setNegativeStatusColor(get(ATTRIBUTE_KEY_NEGATIVE_STATUS_COLOR, DEFAULT_NEGATIVE_STATUS_COLOR))
        }
    }


    private fun setStatusActive(isActive: Boolean) {
        mIsStatusActive = isActive

        if(isActive) {
            statusDmv.setValueColor(mPositiveStatusColor)
        } else {
            statusDmv.setValueColor(mNegativeStatusColor)
        }
    }


    fun setCurrencyNameCharacterLimit(limit: Int) {
        mCurrencyNameCharacterLimit = limit
    }


    fun setPositiveStatusColor(@ColorInt color: Int) {
        mPositiveStatusColor = color

        if(mIsStatusActive) {
            statusDmv.setValueColor(color)
        }
    }


    fun setNegativeStatusColor(@ColorInt color: Int) {
        mNegativeStatusColor = color

        if(!mIsStatusActive) {
            statusDmv.setValueColor(color)
        }
    }


    override fun updateData(data: CurrencyMarketDetails) {
        if(isDataEmpty()) {
            setData(data, true)
            return
        }

        val oldData = mData!!
        setData(data, false)

        if(data.lowPrice != oldData.lowPrice) {
            dailyMinPriceDmv.setValueTextAnimated(getDailyMinPriceString(data))
        }

        if(data.highPrice != oldData.highPrice) {
            dailyMaxPriceDmv.setValueTextAnimated(getDailyMaxPriceString(data))
        }

        if(data.dailyVolumeInBaseCurrency != oldData.dailyVolumeInBaseCurrency) {
            dailyVolumeDmv.setValueTextAnimated(getDailyVolumeString(data))
        }
    }


    override fun bindData() {
        if(isDataEmpty()) {
            return
        }

        val data = mData!!

        setStatusActive(data.isActive)
        statusDmv.setValueText(if(data.isActive) mPositiveStatusString else mNegativeStatusString)

        if(mCurrencyNameCharacterLimit != -1) {
            baseCurrencyDmv.setValueText(data.baseCurrencyName.truncate(mCurrencyNameCharacterLimit))
            quoteCurrencyDmv.setValueText(data.quoteCurrencyName.truncate(mCurrencyNameCharacterLimit))
        } else {
            baseCurrencyDmv.setValueText(data.baseCurrencyName)
            quoteCurrencyDmv.setValueText(data.quoteCurrencyName)
        }

        dailyMinPriceDmv.setValueText(getDailyMinPriceString(data))
        dailyMaxPriceDmv.setValueText(getDailyMaxPriceString(data))
        dailyVolumeDmv.setValueText(getDailyVolumeString(data))

        buyFeeDmv.setValueText(mNumberFormatter.formatFeePercent(data.buyFeeInPercentage))
        sellFeeDmv.setValueText(mNumberFormatter.formatFeePercent(data.sellFeeInPercentage))
    }


    override fun hasProgressBar(): Boolean = true


    override fun hasInfoView(): Boolean = true


    private fun getDailyMinPriceString(data: CurrencyMarketDetails): String {
        return "${mNumberFormatter.formatLimitPrice(data.lowPrice)} ${data.quoteCurrencySymbol}"
    }


    private fun getDailyMaxPriceString(data: CurrencyMarketDetails): String {
        return "${mNumberFormatter.formatLimitPrice(data.highPrice)} ${data.quoteCurrencySymbol}"
    }


    private fun getDailyVolumeString(data: CurrencyMarketDetails): String {
        return "${mNumberFormatter.formatDailyVolume(data.dailyVolumeInBaseCurrency)} ${data.baseCurrencySymbol}"
    }


    override fun getLayoutResourceId(): Int = R.layout.market_details_view_layout


    override fun getProgressBar(): ProgressBar? = progressBar


    override fun getInfoView(): InfoView? = infoView


    override fun getMainView(): View = mainContainerLl


    override fun getDottedMapViewsTitlesArray(): Array<String> {
        return mStringProvider.getStringArray(
            R.string.pair_status,
            R.string.base_currency,
            R.string.quote_currency,
            R.string.daily_min_price,
            R.string.daily_max_price,
            R.string.daily_volume,
            R.string.buy_fee_percent,
            R.string.sell_fee_percent
        )
    }


    override fun getDottedMapViewsArray(): Array<DottedMapView> {
        return arrayOf(
            statusDmv,
            baseCurrencyDmv,
            quoteCurrencyDmv,
            dailyMinPriceDmv,
            dailyMaxPriceDmv,
            dailyVolumeDmv,
            buyFeeDmv,
            sellFeeDmv
        )
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    private class SavedState : BaseDetailsSavedState<CurrencyMarketDetails> {

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