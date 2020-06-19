package com.stocksexchange.android.ui.orders.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.arthurivanets.adapster.model.markers.Trackable
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.OrderLifecycleType
import com.stocksexchange.android.model.OrderData
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.views.mapviews.DottedMapView
import com.stocksexchange.api.model.rest.OrderSelectivityType
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.extensions.set
import com.stocksexchange.core.utils.text.CustomLinkMovementMethod
import com.stocksexchange.core.utils.text.SelectorSpan

class OrderItem(itemModel: OrderData) : BaseItem<OrderData, OrderItem.ViewHolder, OrderResources>(itemModel), Trackable<Long> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.order_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: OrderResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            val strings = resources!!.strings
            val theme = resources.settings.theme

            mIdDmv.setTitleText(strings[OrderResources.STRING_ID])
            mAmountDmv.setTitleText(strings[OrderResources.STRING_AMOUNT])
            mInitialAmountDmv.setTitleText(strings[OrderResources.STRING_INITIAL_AMOUNT])
            mFilledAmountDmv.setTitleText(strings[OrderResources.STRING_FILLED_AMOUNT])
            mStopPriceDmv.setTitleText(strings[OrderResources.STRING_STOP_PRICE])
            mPriceDmv.setTitleText(strings[OrderResources.STRING_PRICE])
            mDateDmv.setTitleText(strings[OrderResources.STRING_DATE])

            mCancelBtn.text = strings[OrderResources.STRING_ACTION_CANCEL]

            applyTheme(theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: OrderResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val order = itemModel.order
            val currencyMarket = itemModel.currencyMarket
            val quoteCurrencySymbol = currencyMarket.quoteCurrencySymbol
            val numberFormatter = resources!!.numberFormatter
            val theme = resources.settings.theme

            when {
                order.type.isBuy -> ThemingUtil.Orders.OrderItem.buyButton(mTypeTv, theme)
                order.type.isSell -> ThemingUtil.Orders.OrderItem.sellButton(mTypeTv, theme)

                else -> ThemingUtil.Orders.OrderItem.unknownTypeButton(mTypeTv, theme)
            }

            mTypeTv.text = order.type.typeStr

            when(resources.orderLifecycleType) {

                OrderLifecycleType.ACTIVE,
                OrderLifecycleType.COMPLETED -> {
                    calculateId(viewHolder)
                    calculateAmount(viewHolder, resources)
                    calculateResult(viewHolder, resources)
                }

                OrderLifecycleType.CANCELLED -> {
                    calculateId(viewHolder)
                    calculateAmount(viewHolder, resources)

                    mResultDmv.makeGone()
                }

            }

            if(order.type.isStopLimit) {
                mStopPriceDmv.setValueText("${numberFormatter.formatPrice(order.triggerPrice)} $quoteCurrencySymbol")
                mStopPriceDmv.makeVisible()
            } else {
                mStopPriceDmv.makeGone()
            }

            mPriceDmv.setValueText("${numberFormatter.formatPrice(order.price)} $quoteCurrencySymbol")
            mDateDmv.setValueText(resources.timeFormatter.formatDate(order.timestampInMillis))

            if(resources.orderLifecycleType == OrderLifecycleType.ACTIVE) {
                mCancelBtn.makeVisible()
            } else {
                mCancelBtn.makeGone()
            }
        }
    }


    private fun calculateId(viewHolder: ViewHolder) {
        viewHolder.mIdDmv.setValueText(itemModel.order.id.toString())
    }


    private fun calculateAmount(viewHolder: ViewHolder, resources: OrderResources) {
        val numberFormatter = resources.numberFormatter
        val order = itemModel.order
        val baseCurrencySymbol = itemModel.currencyMarket.baseCurrencySymbol
        val shouldShowDetailedAmounts = when(resources.orderLifecycleType) {
            OrderLifecycleType.ACTIVE -> order.hasFilledAmount
            OrderLifecycleType.COMPLETED -> !order.isFilledCompletely
            OrderLifecycleType.CANCELLED -> false
        }

        with(viewHolder) {
            if(shouldShowDetailedAmounts) {
                mInitialAmountDmv.makeVisible()
                mFilledAmountDmv.makeVisible()
                mAmountDmv.makeGone()

                mInitialAmountDmv.setValueText("${numberFormatter.formatAmount(order.initialAmount)} $baseCurrencySymbol")
                mFilledAmountDmv.setValueText("${numberFormatter.formatAmount(order.filledAmount)} $baseCurrencySymbol")
            } else {
                mAmountDmv.makeVisible()
                mInitialAmountDmv.makeGone()
                mFilledAmountDmv.makeGone()

                mAmountDmv.setValueText("${numberFormatter.formatAmount(order.initialAmount)} $baseCurrencySymbol")
            }
        }
    }


    private fun calculateResult(viewHolder: ViewHolder, resources: OrderResources) {
        val orderLifecycleType = resources.orderLifecycleType

        if(orderLifecycleType == OrderLifecycleType.CANCELLED) {
            return
        }

        with(viewHolder) {
            val amount = if(orderLifecycleType == OrderLifecycleType.ACTIVE) {
                itemModel.order.initialAmount
            } else {
                itemModel.order.filledAmount
            }
            val result = (amount * itemModel.order.price)
            val quoteCurrencySymbol = itemModel.currencyMarket.quoteCurrencySymbol
            val isBuyType = itemModel.order.type.isBuy
            val isSellType = itemModel.order.type.isSell
            val titleText = resources.strings[when(orderLifecycleType) {
                OrderLifecycleType.ACTIVE -> when {
                    isBuyType -> OrderResources.STRING_RESULT_EXPENSE
                    isSellType -> OrderResources.STRING_RESULT_INCOME

                    else -> OrderResources.STRING_ERROR
                }
                OrderLifecycleType.COMPLETED -> when {
                    isBuyType -> OrderResources.STRING_RESULT_SPENT
                    isSellType -> OrderResources.STRING_RESULT_RECEIVED

                    else -> OrderResources.STRING_ERROR
                }

                else -> OrderResources.STRING_ERROR
            }]
            val numberFormatter = resources.numberFormatter

            mResultDmv.setTitleText(titleText)
            mResultDmv.setValueText("${numberFormatter.formatAmount(result)} $quoteCurrencySymbol")
        }
    }


    fun setOnMarketNameClickListener(viewHolder: ViewHolder, resources: OrderResources?,
                                     position: Int, listener: ((View, OrderItem, Int) -> Unit)?) {
        val order = itemModel.order
        val currencyMarket = itemModel.currencyMarket
        val marketName = if(order.hasCurrencyPairName && !currencyMarket.isStub) {
            order.formattedCurrencyPairName
        } else {
            resources!!.strings[OrderResources.STRING_ERROR]
        }
        val spannableString = marketName.toSpannable()

        if(resources!!.orderSelectivityType == OrderSelectivityType.ANY_PAIR_ID) {
            spannableString[0, spannableString.length] = getClickableSpanForEntry(resources, position, listener)

            viewHolder.mMarketNameTv.movementMethod = CustomLinkMovementMethod()
        }

        viewHolder.mMarketNameTv.text = spannableString
    }


    fun setOnCancelBtnClickListener(viewHolder: ViewHolder, position: Int,
                                    listener: ((View, OrderItem, Int) -> Unit)?) {
        viewHolder.mCancelBtn.setOnClickListener {
            listener?.invoke(it, this@OrderItem, position)
        }
    }


    private fun getClickableSpanForEntry(resources: OrderResources?, position: Int,
                                         listener: ((View, OrderItem, Int) -> Unit)?): SelectorSpan {
        return object : SelectorSpan(
            resources!!.settings.theme.generalTheme.linkReleasedStateBackgroundColor,
            resources.settings.theme.generalTheme.linkPressedStateBackgroundColor
        ) {

            override fun onClick(widget: View) {
                listener?.invoke(widget, this@OrderItem, position)
            }

        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): Long = itemModel.order.id


    class ViewHolder(itemView: View) : BaseViewHolder<OrderData>(itemView) {

        val mTypeTv: TextView = itemView.findViewById(R.id.typeTv)
        val mMarketNameTv: TextView = itemView.findViewById(R.id.marketNameTv)
        val mCancelBtn: TextView = itemView.findViewById(R.id.cancelBtn)

        val mIdDmv: DottedMapView = itemView.findViewById(R.id.idDmv)
        val mAmountDmv: DottedMapView = itemView.findViewById(R.id.amountDmv)
        val mInitialAmountDmv: DottedMapView = itemView.findViewById(R.id.initialAmount)
        val mFilledAmountDmv: DottedMapView = itemView.findViewById(R.id.filledAmount)
        val mPriceDmv: DottedMapView = itemView.findViewById(R.id.priceDmv)
        val mStopPriceDmv: DottedMapView = itemView.findViewById(R.id.stopPriceDmv)
        val mResultDmv: DottedMapView = itemView.findViewById(R.id.resultDmv)
        val mDateDmv: DottedMapView = itemView.findViewById(R.id.dateDmv)

        val mCardView: CardView = itemView.findViewById(R.id.cardView)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.Orders.OrderItem) {
                cardView(mCardView, theme)

                marketName(mMarketNameTv, theme)

                dottedMap(mIdDmv, theme)
                dottedMap(mAmountDmv, theme)
                dottedMap(mInitialAmountDmv, theme)
                dottedMap(mFilledAmountDmv, theme)
                dottedMap(mPriceDmv, theme)
                dottedMap(mStopPriceDmv, theme)
                dottedMap(mResultDmv, theme)
                dottedMap(mDateDmv, theme)

                cancelButton(mCancelBtn, theme)
            }
        }

    }


}