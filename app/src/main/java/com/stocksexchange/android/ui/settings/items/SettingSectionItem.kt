package com.stocksexchange.android.ui.settings.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.stocksexchange.android.R
import com.stocksexchange.android.model.SettingSection
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.ui.settings.SettingResources
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme

class SettingSectionItem(itemModel: SettingSection) : BaseItem<
    SettingSection,
    SettingSectionItem.ViewHolder,
    SettingResources
>(itemModel) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.setting_section_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: SettingResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false))
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: SettingResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            mTitleTv.text = itemModel.title

            applyTheme(resources!!.settings.theme)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    class ViewHolder(itemView: View) : BaseViewHolder<SettingSection>(itemView) {

        val mTitleTv: TextView = itemView.findViewById(R.id.titleTv)


        override fun applyTheme(theme: Theme) {
            ThemingUtil.Settings.SettingSection.title(mTitleTv, theme)
        }

    }


}