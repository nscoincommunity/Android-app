package com.stocksexchange.android.ui.currencymarketpreview.views.orderbook.items

import android.graphics.drawable.Drawable
import android.view.Gravity
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
import com.stocksexchange.android.model.OrderbookOrderData
import com.stocksexchange.android.model.OrderbookOrderType
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.utils.extensions.getOrderbookOrderBackgroundDrawable
import com.stocksexchange.android.ui.currencymarketpreview.views.orderbook.OrderbookResources
import com.stocksexchange.core.utils.Timer

class OrderbookOrderItem(itemModel: OrderbookOrderData) : BaseItem<
    OrderbookOrderData,
    OrderbookOrderItem.ViewHolder,
    OrderbookResources
>(itemModel), Trackable<String> {


    companion object {

        const val SELL_ITEM_LAYOUT_ID = R.layout.orderbook_sell_order_item_layout
        const val BUY_ITEM_LAYOUT_ID = R.layout.orderbook_buy_order_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: OrderbookResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false))
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: OrderbookResources?) {
        with(viewHolder) {
            val numberFormatter = resources!!.numberFormatter
            val colors = resources.colors
            val priceHighlightColor: Int
            val priceColor: Int
            val backgroundDrawable: Drawable
            val backgroundHighlightDrawable: Drawable

            when(itemModel.type) {
                OrderbookOrderType.ASK -> {
                    priceHighlightColor = colors[OrderbookResources.COLOR_SELL_ORDER_PRICE_HIGHLIGHT]
                    priceColor = colors[OrderbookResources.COLOR_SELL_ORDER_PRICE]
                    backgroundDrawable = itemView.context.getOrderbookOrderBackgroundDrawable(
                        colors[OrderbookResources.COLOR_SELL_ORDER_BACKGROUND],
                        Gravity.END,
                        itemModel.volumeLevel
                    )
                    backgroundHighlightDrawable = itemView.context.getOrderbookOrderBackgroundDrawable(
                        colors[OrderbookResources.COLOR_SELL_ORDER_BACKGROUND_HIGHLIGHT],
                        Gravity.END,
                        OrderbookOrderData.VOLUME_LEVEL_MAX_VALUE
                    )
                }

                OrderbookOrderType.BID -> {
                    priceHighlightColor = colors[OrderbookResources.COLOR_BUY_ORDER_PRICE_HIGHLIGHT]
                    priceColor = colors[OrderbookResources.COLOR_BUY_ORDER_PRICE]
                    backgroundDrawable = itemView.context.getOrderbookOrderBackgroundDrawable(
                        colors[OrderbookResources.COLOR_BUY_ORDER_BACKGROUND],
                        Gravity.START,
                        itemModel.volumeLevel
                    )
                    backgroundHighlightDrawable = itemView.context.getOrderbookOrderBackgroundDrawable(
                        colors[OrderbookResources.COLOR_BUY_ORDER_BACKGROUND_HIGHLIGHT],
                        Gravity.START,
                        OrderbookOrderData.VOLUME_LEVEL_MAX_VALUE
                    )
                }
            }

            if(itemModel.order.isStub) {
                val stubText = resources.stubText

                mPriceTv.text = stubText
                mAmountTv.text = stubText

                mPriceTv.setTextColor(priceColor)
                itemView.background = null
            } else {
                val dehighlightViews = {
                    mPriceTv.setTextColor(priceColor)
                    itemView.background = backgroundDrawable
                }

                if(itemModel.shouldBeHighlighted()) {
                    val highlightViews = {
                        mPriceTv.setTextColor(priceHighlightColor)
                        itemView.background = backgroundHighlightDrawable
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

                mPriceTv.text = numberFormatter.formatOrderbookPrice(
                    itemModel.order.price,
                    resources.priceMaxCharsLength
                )
                mAmountTv.text = numberFormatter.formatOrderbookAmount(
                    itemModel.order.amount,
                    resources.amountMaxCharsLength
                )
            }

            mAmountTv.setTextColor(colors[OrderbookResources.COLOR_ORDER_AMOUNT])
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((View, OrderbookOrderItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(it, this@OrderbookOrderItem, position)
        }
    }


    override fun getLayout(): Int {
        return when(itemModel.type) {
            OrderbookOrderType.ASK -> SELL_ITEM_LAYOUT_ID
            OrderbookOrderType.BID -> BUY_ITEM_LAYOUT_ID
        }
    }


    override fun getTrackKey(): String {
        val orderType = itemModel.type.name
        val price = itemModel.order.price.toString()
        val amount = itemModel.order.amount.toString()

        return "${orderType}_${price}_$amount"
    }


    class ViewHolder(itemView: View) : BaseViewHolder<OrderbookOrderData>(itemView) {

        val mPriceTv: TextView = itemView.findViewById(R.id.priceTv)
        val mAmountTv: TextView = itemView.findViewById(R.id.amountTv)

    }


}