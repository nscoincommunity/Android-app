package com.stocksexchange.android.ui.views.popupmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.arthurivanets.adapster.model.markers.Trackable
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Separator
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder

class PopupMenuSeparatorItem(itemModel: Separator) : BaseItem<
    Separator,
    PopupMenuSeparatorItem.ViewHolder,
    PopupResources
>(itemModel), Trackable<Int> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.popup_menu_separator_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: PopupResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            mSeparatorIv.setImageDrawable(resources!!.drawables[PopupResources.DRAWABLE_SEPARATOR])
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): Int = itemModel.id


    class ViewHolder(itemView: View) : BaseViewHolder<Separator>(itemView) {

        val mSeparatorIv: ImageView = itemView.findViewById(R.id.separatorIv)

    }


}