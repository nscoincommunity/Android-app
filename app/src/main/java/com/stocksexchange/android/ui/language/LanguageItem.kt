package com.stocksexchange.android.ui.language

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
import com.stocksexchange.android.model.LanguageItemModel
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme

class LanguageItem(itemModel: LanguageItemModel) : BaseItem<LanguageItemModel, LanguageItem.ViewHolder, LanguageResources>(itemModel) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.language_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: LanguageResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false))
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: LanguageResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val theme = resources!!.settings.theme
            val stringProvider = resources.stringProvider

            titleTv.text = stringProvider.getString(itemModel.nameStringId)
            iconIv.setImageResource(itemModel.language.imageDrawableId)

            applyTheme(theme)
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((View, LanguageItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(it, this@LanguageItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    class ViewHolder(itemView: View) : BaseViewHolder<LanguageItemModel>(itemView) {

        val titleTv: TextView = itemView.findViewById(R.id.titleTv)
        val iconIv: ImageView = itemView.findViewById(R.id.iconIv)
        val contentContainerRl: RelativeLayout = itemView.findViewById(R.id.contentContainerRl)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.LanguageSelection.LanguageItem) {
                if(boundData.isSelected) {
                    selectedContentContainer(contentContainerRl, theme)
                } else {
                    contentContainer(contentContainerRl, theme)
                }

                title(titleTv, theme)
            }
        }

    }


}