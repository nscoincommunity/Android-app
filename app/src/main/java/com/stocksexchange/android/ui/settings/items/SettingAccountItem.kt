package com.stocksexchange.android.ui.settings.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.stocksexchange.android.R
import com.stocksexchange.android.model.SettingAccount
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.settings.SettingResources

class SettingAccountItem(itemModel: SettingAccount) : BaseItem<SettingAccount, SettingAccountItem.ViewHolder, SettingResources>(itemModel) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.setting_account_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: SettingResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            applyTheme(resources!!.settings.theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: SettingResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            mTitleTv.text = itemModel.userEmail
            mDescriptionTv.text = itemModel.userName
        }
    }


    fun setOnSignOutClickListener(viewHolder: ViewHolder, position: Int,
                                  listener: ((View, SettingAccountItem, Int) -> Unit)?) {
        viewHolder.iconIv.setOnClickListener {
            listener?.invoke(it, this@SettingAccountItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    class ViewHolder(itemView: View) : BaseViewHolder<SettingAccount>(itemView) {

        val mTitleTv: TextView = itemView.findViewById(R.id.titleTv)
        val mDescriptionTv: TextView = itemView.findViewById(R.id.descriptionTv)
        val iconIv: ImageView = itemView.findViewById(R.id.iconIv)
        val contentContainerRl: RelativeLayout = itemView.findViewById(R.id.contentContainerRl)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.Settings.SettingAccountItem) {
                contentContainer(contentContainerRl, theme)
                title(mTitleTv, theme)
                description(mDescriptionTv, theme)
            }
        }

    }


}