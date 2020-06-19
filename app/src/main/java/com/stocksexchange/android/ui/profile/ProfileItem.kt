package com.stocksexchange.android.ui.profile

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
import com.stocksexchange.android.model.ProfileItemModel
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder

class ProfileItem(itemModel: ProfileItemModel) : BaseItem<ProfileItemModel, ProfileItem.ViewHolder, ProfileItemResources>(itemModel) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.profile_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: ProfileItemResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            bindData(itemModel)
            applyTheme(resources!!.theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: ProfileItemResources?) {
        with(viewHolder) {
            val stringProvider = resources!!.stringProvider

            titleTv.text = stringProvider.getString(itemModel.titleId)
            descriptionTv.text = stringProvider.getString(itemModel.descriptionId)
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((ViewHolder, ProfileItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(viewHolder, this@ProfileItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    class ViewHolder(itemView: View) : BaseViewHolder<ProfileItemModel>(itemView) {

        val contentContainerRl = itemView.findViewById<RelativeLayout>(R.id.contentContainerRl)

        val titleTv = itemView.findViewById<TextView>(R.id.titleTv)
        val descriptionTv = itemView.findViewById<TextView>(R.id.descriptionTv)

        val iconIv = itemView.findViewById<ImageView>(R.id.iconIv)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.Profile.Item) {
                contentContainer(contentContainerRl, theme)
                description(descriptionTv, theme)
                icon(iconIv, theme)

                if(boundData.ordinal == ProfileItemModel.LOGIN.ordinal) {
                    loginTitle(titleTv, theme)
                } else {
                    title(titleTv, theme)
                }
            }
        }

    }


}