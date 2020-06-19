package com.stocksexchange.android.ui.currencymarkets.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.arthurivanets.adapster.model.markers.Trackable
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.core.utils.extensions.truncate

class CurrencyMarketItem(itemModel: CurrencyMarket) : BaseItem<
    CurrencyMarket,
    CurrencyMarketItem.ViewHolder,
    CurrencyMarketResources
>(itemModel), Trackable<Int> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.currency_market_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: CurrencyMarketResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            applyTheme(resources!!.settings.theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: CurrencyMarketResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val fiatCurrencyPriceHandler = resources!!.fiatCurrencyPriceHandler
            val fiatCurrency = resources.settings.fiatCurrency
            val numberFormatter = resources.numberFormatter
            val dailyVolumeStr = resources.volumeTemplate.format(numberFormatter.formatVolume(itemModel.dailyVolumeInQuoteCurrency))
            val baseCurrencySymbol = itemModel.baseCurrencySymbol
            val baseCurrencySymbolCharacterLimit = resources.baseCurrencySymbolCharacterLimit
            val shouldTruncateCurrencySymbol = (baseCurrencySymbolCharacterLimit != -1)

            fiatCurrencyPriceHandler.handleMarketItemFiatCurrencyLastPrice(
                fiatCurrency = fiatCurrency,
                currencyMarket = itemModel,
                textView = mLastPriceInFiatCurrencyTv
            )

            mBaseCurrencySymbolTv.text = if(shouldTruncateCurrencySymbol) {
                baseCurrencySymbol.truncate(baseCurrencySymbolCharacterLimit)
            } else {
                baseCurrencySymbol
            }

            mQuoteCurrencySymbolTv.text = itemModel.quoteCurrencySymbol
            mDailyVolumeTv.text = dailyVolumeStr
            mLastPriceTv.text = numberFormatter.formatFixedPrice(itemModel.lastPrice)
            mDailyPriceChangeTv.text = numberFormatter.formatDailyPriceChange(itemModel.dailyPriceChangeInPercentage)

            with(ThemingUtil.CurrencyMarkets.Item) {
                when {
                    (itemModel.dailyPriceChangeInPercentage > 0.0) -> positiveDailyPriceChange(mDailyPriceChangeTv)
                    (itemModel.dailyPriceChangeInPercentage < 0.0) -> negativeDailyPriceChange(mDailyPriceChangeTv)
                    else -> neutralDailyPriceChange(mDailyPriceChangeTv)
                }
            }
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((View, CurrencyMarketItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(it, this@CurrencyMarketItem, position)
        }
    }


    fun setOnItemLongClickListener(viewHolder: ViewHolder, position: Int,
                                   listener: ((View, CurrencyMarketItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnLongClickListener {
            listener?.invoke(it, this@CurrencyMarketItem, position)
            true
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): Int = itemModel.pairId


    class ViewHolder(itemView: View) : BaseViewHolder<CurrencyMarket>(itemView) {

        val mBaseCurrencySymbolTv: TextView = itemView.findViewById(R.id.baseCurrencySymbolTv)
        val mSeparatorTv: TextView = itemView.findViewById(R.id.separatorTv)
        val mQuoteCurrencySymbolTv: TextView = itemView.findViewById(R.id.quoteCurrencySymbolTv)
        val mDailyVolumeTv: TextView = itemView.findViewById(R.id.dailyVolumeTv)
        val mLastPriceTv: TextView = itemView.findViewById(R.id.lastPriceTv)
        val mLastPriceInFiatCurrencyTv: TextView = itemView.findViewById(R.id.lastPriceInFiatCurrencyTv)
        val mDailyPriceChangeTv: TextView = itemView.findViewById(R.id.dailyPriceChangeTv)

        val mContentContainerCl: ConstraintLayout = itemView.findViewById(R.id.contentContainerCl)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.CurrencyMarkets.Item) {
                contentContainer(mContentContainerCl, theme)
                baseCurrencySymbol(mBaseCurrencySymbolTv, theme)
                separator(mSeparatorTv, theme)
                quoteCurrencySymbol(mQuoteCurrencySymbolTv, theme)
                dailyVolume(mDailyVolumeTv, theme)
                lastPrice(mLastPriceTv, theme)
                lastPriceInFiatCurrency(mLastPriceInFiatCurrencyTv, theme)
            }
        }

    }


}