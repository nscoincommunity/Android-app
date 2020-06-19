package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.extensions.getSelectableItemBackgroundBorderlessDrawable
import com.stocksexchange.android.ui.views.base.containers.BaseFrameLayoutView
import com.stocksexchange.api.model.rest.AlertPriceComparison
import kotlinx.android.synthetic.main.alert_price_item_view.view.*

/**
 * A view container showing an alert price item view
 */
class AlertPriceItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseFrameLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val LEFT_LESS_ICON_BACKGROUND = R.drawable.ic_alert_price_less_then_background
        private const val LEFT_MORE_ICON_BACKGROUND = R.drawable.ic_alert_price_more_then_background
        private const val LEFT_NOT_ACTIVE_BACKGROUND = R.drawable.ic_alert_price_icon_background

        private const val LEFT_LESS_ICON_ARROW = R.drawable.ic_less_than_price_arrow
        private const val LEFT_MORE_ICON_ARROW = R.drawable.ic_more_than_price_arrow

        private const val TITLE_LESS_COLOR = R.color.alertPriceLessColor
        private const val TITLE_MORE_COLOR = R.color.deepTealAccentColor

        private const val TITLE_LESS = R.string.alert_price_less_than
        private const val TITLE_MORE = R.string.alert_price_more_than

    }




    override fun init() {
        super.init()

        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        background = context.getSelectableItemBackgroundBorderlessDrawable()
    }


    private fun setLeftIconBackgroundDrawable(drawable: Drawable?) {
        drawable?.let {
            leftIconRl.background = drawable
        }
    }


    private fun setLeftIconArrowDrawable(drawable: Drawable?) {
        drawable?.let {
            leftIconArrowIv.setImageDrawable(drawable)
        }
    }


    private fun setTitleText(text: String) {
        titleTextTv.text = text
    }


    fun setPriceText(text: String) {
        priceTv.text = text
    }


    private fun setPriceColor(@ColorInt color: Int) {
        priceTv.setTextColor(color)
    }


    override fun getLayoutResourceId(): Int = R.layout.alert_price_item_view


    fun getPriceTextView(): TextView = priceTv


    fun getTitleTextView(): TextView = titleTextTv


    fun setData(comparisonType: String, active: Boolean) {
        when (comparisonType) {
            AlertPriceComparison.GREATER.title -> {
                setTitleText(mStringProvider.getString(TITLE_MORE))
                setLeftIconArrowDrawable(context.getDrawable(LEFT_MORE_ICON_ARROW))

                if (active) {
                    setLeftIconBackgroundDrawable(context.getDrawable(LEFT_MORE_ICON_BACKGROUND))
                    setPriceColor(context.resources.getColor(TITLE_MORE_COLOR))
                } else {
                    setLeftIconBackgroundDrawable(context.getDrawable(LEFT_NOT_ACTIVE_BACKGROUND))
                }
            }

            AlertPriceComparison.LESS.title -> {
                setTitleText(mStringProvider.getString(TITLE_LESS))
                setLeftIconArrowDrawable(context.getDrawable(LEFT_LESS_ICON_ARROW))

                if (active) {
                    setLeftIconBackgroundDrawable(context.getDrawable(LEFT_LESS_ICON_BACKGROUND))
                    setPriceColor(context.resources.getColor(TITLE_LESS_COLOR))
                }else {
                    setLeftIconBackgroundDrawable(context.getDrawable(LEFT_NOT_ACTIVE_BACKGROUND))
                }
            }
        }
    }


    fun setOnDeleteButtonClickListener(listener: (View) -> Unit) {
        deleteIv.setOnClickListener(listener)
    }


}