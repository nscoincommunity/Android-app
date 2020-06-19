package com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory.items

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.arthurivanets.adapster.model.markers.Trackable
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.android.model.TradeData
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory.TradeHistoryResources
import com.stocksexchange.core.utils.Timer
import com.stocksexchange.core.utils.extensions.makeInvisible
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.extensions.setColor

class TradeHistoryItem(itemModel: TradeData) : BaseItem<
    TradeData,
    TradeHistoryItem.ViewHolder,
    TradeHistoryResources
>(itemModel), Trackable<Long> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.trade_history_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: TradeHistoryResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            val colors = resources!!.colors
            val strings = resources.strings

            mAmountTv.setTextColor(colors[TradeHistoryResources.COLOR_TRADE_AMOUNT])
            mTimeTv.setTextColor(colors[TradeHistoryResources.COLOR_TRADE_TIME])
            mCancelBtnTv.setTextColor(colors[TradeHistoryResources.COLOR_CANCEL_BUTTON_TEXT])
            mCancellationPb.setColor(colors[TradeHistoryResources.COLOR_CANCELLATION_PROGRESS_BAR])

            mCancelBtnTv.text = strings[TradeHistoryResources.STRING_ACTION_CANCEL]
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: TradeHistoryResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val trade = itemModel.trade
            val colors = resources!!.colors
            val numberFormatter = resources.numberFormatter
            val timeFormatter = resources.timeFormatter
            val defaultBackgroundColor = Color.TRANSPARENT
            val backgroundHighlightColor: Int
            val priceHighlightColor: Int
            val priceColor: Int

            when(trade.type) {

                TradeType.BUY -> {
                    backgroundHighlightColor = colors[TradeHistoryResources.COLOR_BUY_TRADE_BACKGROUND_HIGHLIGHT]
                    priceHighlightColor = colors[TradeHistoryResources.COLOR_BUY_TRADE_PRICE_HIGHLIGHT]
                    priceColor = colors[TradeHistoryResources.COLOR_BUY_TRADE_PRICE]
                }

                TradeType.SELL -> {
                    backgroundHighlightColor = colors[TradeHistoryResources.COLOR_SELL_TRADE_BACKGROUND_HIGHLIGHT]
                    priceHighlightColor = colors[TradeHistoryResources.COLOR_SELL_TRADE_PRICE_HIGHLIGHT]
                    priceColor = colors[TradeHistoryResources.COLOR_SELL_TRADE_PRICE]
                }

            }

            mSwipeRevealLayout.setLockDrag(!itemModel.isSwipeMenuEnabled)

            if(itemModel.isProgressBarVisible) {
                mCancellationPb.makeVisible()
            } else {
                mCancellationPb.makeInvisible()
            }

            if(trade.isStub) {
                val stubText = resources.stubText

                mAmountTv.text = stubText
                mPriceTv.text = stubText
                mTimeTv.text = stubText

                mPriceTv.setTextColor(priceColor)
            } else {
                val dehighlightViews = {
                    mPriceTv.setTextColor(priceColor)
                    mMainLayoutFl.setBackgroundColor(defaultBackgroundColor)
                }

                if(itemModel.shouldBeHighlighted()) {
                    val highlightViews = {
                        mPriceTv.setTextColor(priceHighlightColor)
                        mMainLayoutFl.setBackgroundColor(backgroundHighlightColor)
                    }

                    (itemView.tag as? Timer)?.cancel()

                    val highlightDuration = itemModel.calculateHighlightDuration()
                    val timer = object : Timer(highlightDuration, highlightDuration) {

                        override fun onStarted() {
                            highlightViews()
                        }

                        override fun onFinished() {
                            dehighlightViews()
                        }

                    }

                    timer.start()
                    itemView.tag = timer
                } else {
                    dehighlightViews()
                }

                mAmountTv.text = numberFormatter.formatTradeHistoryAmount(trade.amount, resources.amountMaxCharsLength)
                mPriceTv.text = numberFormatter.formatTradeHistoryPrice(trade.price, resources.priceMaxCharsLength)
                mTimeTv.text = timeFormatter.formatTradeHistoryTime(trade.timestampInMillis)
            }
        }
    }


    fun setOnCancelBtnClickListener(viewHolder: ViewHolder, position: Int,
                                    listener: ((View, TradeHistoryItem, Int) -> Unit)?) {
        viewHolder.mCancelBtnContainerLl.setOnClickListener {
            listener?.invoke(it, this@TradeHistoryItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): Long = itemModel.trade.id


    class ViewHolder(itemView: View) : BaseViewHolder<TradeData>(itemView) {

        val mAmountTv: TextView = itemView.findViewById(R.id.amountTv)
        val mPriceTv: TextView = itemView.findViewById(R.id.priceTv)
        val mTimeTv: TextView = itemView.findViewById(R.id.timeTv)

        val mCancelBtnTv: TextView = itemView.findViewById(R.id.cancelBtnTv)

        val mCancellationPb: ProgressBar = itemView.findViewById(R.id.cancellationPb)

        val mCancelBtnContainerLl: LinearLayout = itemView.findViewById(R.id.cancelBtnContainerLl)
        val mMainLayoutFl: FrameLayout = itemView.findViewById(R.id.mainLayoutFl)
        val mSwipeRevealLayout: SwipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout)

    }


}