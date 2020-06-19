package com.stocksexchange.android.ui.views.popupmenu

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
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder

class PopupMenuItem(itemModel: PopupMenuItemData) : BaseItem<
    PopupMenuItemData,
    PopupMenuItem.ViewHolder,
    PopupResources
>(itemModel), Trackable<Int> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.popup_menu_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: PopupResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            mTitleTv.setTextColor(resources!!.colors[PopupResources.COLOR_ITEM_TEXT_COLOR])
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: PopupResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            mTitleTv.text = itemModel.title
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((View, PopupMenuItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(it, this@PopupMenuItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): Int = itemModel.id


    class ViewHolder(itemView: View) : BaseViewHolder<PopupMenuItemData>(itemView) {

        val mTitleTv: TextView = itemView.findViewById(R.id.titleTv)

    }


}