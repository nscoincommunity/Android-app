package com.stocksexchange.android.ui.alertprice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.arthurivanets.adapster.model.markers.Trackable
import com.stocksexchange.android.R
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.views.AlertPriceItemView
import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible

class AlertPriceItem(itemModel: AlertPairItem) : BaseItem<
    AlertPairItem,
    AlertPriceItem.ViewHolder,
    AlertPriceResources
>(itemModel), Trackable<Int> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.alert_price_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: AlertPriceResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            applyTheme(resources!!.settings.theme)
        }
    }


    @SuppressLint("DefaultLocale")
    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: AlertPriceResources?) {
        super.bind(adapter, viewHolder, resources)

        val theme = resources!!.settings.theme
        val numberFormatter = resources.numberFormatter

        with(viewHolder) {
            pairNameTv.text = itemModel.currencyPairName

            updateAlertPriceView(lessAlertPriceApiv, itemModel.lessAlertPrice, theme, numberFormatter)
            updateAlertPriceView(moreAlertPriceApiv, itemModel.moreAlertPrice, theme, numberFormatter)
        }

    }


    private fun updateAlertPriceView(alertPriceApiv: AlertPriceItemView, alertPrice: AlertPrice?,
                                     theme: Theme, numberFormatter: NumberFormatter) {
        if (alertPrice != null) {
            val price = numberFormatter.formatFixedPrice(alertPrice.price)
            alertPriceApiv.setPriceText(price)
            alertPriceApiv.setData(alertPrice.comparisonType, alertPrice.active)

            if (!alertPrice.active) {
                ThemingUtil.AlertPrice.AlertPriceItem.setPriceTextColor(
                    alertPriceApiv.getPriceTextView(),
                    theme
                )
            }

            alertPriceApiv.makeVisible()
        } else {
            alertPriceApiv.makeGone()
        }
    }


    fun setOnMoreDeleteListener(viewHolder: ViewHolder, position: Int,
                                listener: ((View, AlertPriceItem, Int) -> Unit)?) {
        viewHolder.moreAlertPriceApiv.setOnDeleteButtonClickListener {
            listener?.invoke(it, this@AlertPriceItem, position)
        }
    }


    fun setOnLessDeleteListener(viewHolder: ViewHolder, position: Int,
                                listener: ((View, AlertPriceItem, Int) -> Unit)?) {
        viewHolder.lessAlertPriceApiv.setOnDeleteButtonClickListener {
            listener?.invoke(it, this@AlertPriceItem, position)
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((View, AlertPriceItem, Int) -> Unit)?) {
        viewHolder.pairNameTv.setOnClickListener {
            listener?.invoke(it, this@AlertPriceItem, position)
        }
    }


    fun getAlertPairItem(): AlertPairItem {
        return itemModel
    }


    fun getAlertPairItemId(): Int {
        return itemModel.currencyPairId
    }


    fun getAlertPairItemName(): String {
        return itemModel.currencyPairName
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): Int = itemModel.currencyPairId


    class ViewHolder(itemView: View) : BaseViewHolder<AlertPairItem>(itemView) {

        val contentContainer: View = itemView.findViewById(R.id.contentContainerLl)

        val pairNameTv: TextView = itemView.findViewById(R.id.pairNameTv)

        val lessAlertPriceApiv: AlertPriceItemView = itemView.findViewById(R.id.lessAlertPriceApiv)
        val moreAlertPriceApiv: AlertPriceItemView = itemView.findViewById(R.id.moreAlertPriceApiv)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.AlertPrice.AlertPriceItem) {
                contentContainer(itemView, theme)
                baseCurrencySymbol(pairNameTv, theme)
                setTitleTextColor(pairNameTv, theme)

                setTitleTextColor(lessAlertPriceApiv.getTitleTextView(), theme)
                setTitleTextColor(moreAlertPriceApiv.getTitleTextView(), theme)
            }
        }

    }


}