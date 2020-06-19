package com.stocksexchange.android.ui.inbox

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.arthurivanets.adapster.model.markers.Trackable
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.api.model.rest.InboxCategory
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.ThemingUtil.Inbox.InboxItem.readDateField
import com.stocksexchange.android.theming.ThemingUtil.Inbox.InboxItem.readTitleField
import com.stocksexchange.android.theming.ThemingUtil.Inbox.InboxItem.unreadDateField
import com.stocksexchange.android.theming.ThemingUtil.Inbox.InboxItem.unreadTitleField
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.views.mapviews.SpaceMapView
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible

class InboxItem(itemModel: Inbox) : BaseItem<
    Inbox,
    InboxItem.ViewHolder,
    InboxResources
>(itemModel), Trackable<String> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.inbox_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: InboxResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            val theme = resources!!.theme
            applyTheme(theme)
        }
    }


    @SuppressLint("DefaultLocale")
    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: InboxResources?) {
        super.bind(adapter, viewHolder, resources)

        val strings = resources!!.strings
        val theme = resources!!.theme

        with(viewHolder) {
            eventTitleTv.text = itemModel.title
            eventDateTv.text = itemModel.date

            if (itemModel.isItemRead()) {
                readTitleField(eventTitleTv, theme)
                readDateField(eventDateTv, theme)
            } else{
                unreadTitleField(eventTitleTv, theme)
                unreadDateField(eventDateTv, theme)
            }

            when (itemModel.channel?.toLowerCase()) {

                InboxCategory.WITHDRAWAL.title -> {
                    addDetaileLine(line1Dmv, theme, strings[InboxResources.STRING_COIN], itemModel.coin)
                    addDetaileLine(line2Dmv, theme, strings[InboxResources.STRING_COIN_FULL_NAME], itemModel.coinFullName)
                    addDetaileLine(line3Dmv, theme, strings[InboxResources.STRING_AMOUNT], itemModel.amount)
                }

                InboxCategory.ORDER.title -> {
                    addDetaileLine(line1Dmv, theme, strings[InboxResources.STRING_ORDER_ID], itemModel.orderId)
                    addDetaileLine(line2Dmv, theme, strings[InboxResources.STRING_DATE], itemModel.orderId)
                    addDetaileLine(line3Dmv, theme, strings[InboxResources.STRING_CURRENCY_PAIR], itemModel.currencyPair, true)
                    addDetaileLine(line4Dmv, theme, strings[InboxResources.STRING_TYPE], itemModel.type)
                    addDetaileLine(line5Dmv, theme, strings[InboxResources.STRING_ORDER_AMOUNT], itemModel.orderAmount)
                    addDetaileLine(line6Dmv, theme, strings[InboxResources.STRING_PRICE], itemModel.price)
                    addDetaileLine(line7Dmv, theme, strings[InboxResources.STRING_EXPECTED_AMOUNT], itemModel.expectedAmount)
                }

                else -> {
                    addDetaileLine(line1Dmv, theme, strings[InboxResources.STRING_DESCRIPTION], itemModel.desc)
                }

            }

            if (itemModel.isItemDetailsShown()) {
                messageDetailsLl.makeVisible()
            } else {
                messageDetailsLl.makeGone()
            }

        }

    }


    //todo: optimize this method
    private fun addDetaileLine(item: SpaceMapView, theme: Theme, title: String, value: String?, isPair: Boolean = false) {
        if (value != null) {
            item.setTitleText("$title:")
            item.setValueText(value)
            if (isPair) {
                ThemingUtil.Inbox.InboxItem.setGreenColorField(item, theme)
            } else {
                ThemingUtil.Inbox.InboxItem.setColorTitleField(item, theme)
            }

            item.makeVisible()
        } else {
            item.makeGone()
        }
    }



    fun setOnClickListener(viewHolder: ViewHolder, position: Int,
                           listener: ((View, InboxItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(it, this@InboxItem, position)
        }
    }


    fun setOnDeleteListener(viewHolder: ViewHolder, position: Int,
                            listener: ((View, InboxItem, Int) -> Unit)?) {
        viewHolder.itemDeleteIv.setOnClickListener {
            listener?.invoke(it, this@InboxItem, position)
        }
    }


    fun isShowed(): Boolean {
        return itemModel.isItemRead()
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): String = itemModel.id


    class ViewHolder(itemView: View) : BaseViewHolder<Inbox>(itemView) {

        val eventTitleTv: TextView = itemView.findViewById(R.id.eventTitleTv)
        val eventDateTv: TextView = itemView.findViewById(R.id.eventDateTv)
        val messageDetailsLl: LinearLayout = itemView.findViewById(R.id.messageDetailsLl)
        val itemDeleteIv: ImageView = itemView.findViewById(R.id.itemDeleteIv)

        val line1Dmv: SpaceMapView = itemView.findViewById(R.id.line1Dmv)
        val line2Dmv: SpaceMapView = itemView.findViewById(R.id.line2Dmv)
        val line3Dmv: SpaceMapView = itemView.findViewById(R.id.line3Dmv)
        val line4Dmv: SpaceMapView = itemView.findViewById(R.id.line4Dmv)
        val line5Dmv: SpaceMapView = itemView.findViewById(R.id.line5Dmv)
        val line6Dmv: SpaceMapView = itemView.findViewById(R.id.line6Dmv)
        val line7Dmv: SpaceMapView = itemView.findViewById(R.id.line7Dmv)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.Inbox.InboxItem) {
                contentContainer(itemView, theme)
            }
        }

    }


}