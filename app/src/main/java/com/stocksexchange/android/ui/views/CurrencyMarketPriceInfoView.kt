package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseConstraintLayoutView
import kotlinx.android.synthetic.main.currency_market_price_info_view.view.*
import kotlin.properties.Delegates

class CurrencyMarketPriceInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseConstraintLayoutView(context, attrs, defStyleAttr) {


    @get:ColorInt
    var dailyPriceChangePositiveColor: Int by Delegates.observable(Color.GREEN) { _, _, _ ->
        updateDailyPriceChangeTextColor()
    }

    @get:ColorInt
    var dailyPriceChangeNegativeColor: Int by Delegates.observable(Color.RED) { _, _, _ ->
        updateDailyPriceChangeTextColor()
    }

    @get:ColorInt
    var dailyPriceChangeNeutralColor: Int by Delegates.observable(Color.BLUE) { _, _, _ ->
        updateDailyPriceChangeTextColor()
    }

    @get:ColorInt
    var priceTextColor: Int
        set(@ColorInt value) { priceTv.setTextColor(value) }
        get() = priceTv.currentTextColor

    @get:ColorInt
    var fiatPriceTextColor: Int
        set(@ColorInt value) { fiatPriceTv.setTextColor(value) }
        get() = fiatPriceTv.currentTextColor

    @get:ColorInt
    var dailyPriceChangeTextColor: Int
        set(@ColorInt value) { dailyPriceChangeTv.setTextColor(value) }
        get() = dailyPriceChangeTv.currentTextColor

    var priceText: CharSequence
        set(value) { priceTv.text = value }
        get() = priceTv.text

    var fiatPriceText: CharSequence
        set(value) { fiatPriceTv.text = value }
        get() = fiatPriceTv.text

    var dailyPriceChangeText: CharSequence
        set(value) {
            dailyPriceChangeTv.text = value
            updateDailyPriceChangeTextColor()
        }
        get() = dailyPriceChangeTv.text




    private fun updateDailyPriceChangeTextColor() {
        if(dailyPriceChangeText.isBlank()) {
            return
        }

        dailyPriceChangeTextColor = when(dailyPriceChangeText.first()) {
            '+' -> dailyPriceChangePositiveColor
            '-' -> dailyPriceChangeNegativeColor
            else -> dailyPriceChangeNeutralColor
        }
    }


    override fun getLayoutResourceId(): Int = R.layout.currency_market_price_info_view


}