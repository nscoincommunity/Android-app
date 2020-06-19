package com.stocksexchange.android.ui.help

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.stocksexchange.android.R
import com.stocksexchange.android.model.HelpItemModel
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder

class HelpItem(itemModel: HelpItemModel) : BaseItem<HelpItemModel, HelpItem.ViewHolder, HelpItemResources>(itemModel) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.help_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: HelpItemResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false))
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: HelpItemResources?) {
        with(viewHolder) {
            val theme = resources!!.settings.theme
            val stringProvider = resources.stringProvider

            mTitleTv.text = stringProvider.getString(itemModel.titleId)
            mSubtitleTv.text = stringProvider.getString(itemModel.subtitleId)

            mIconIv.setImageResource(itemModel.iconId)
            mArrowIv.setImageResource(R.mipmap.ic_arrow_right)

            applyTheme(theme)
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((ViewHolder, HelpItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(viewHolder, this@HelpItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    class ViewHolder(itemView: View) : BaseViewHolder<HelpItemModel>(itemView) {

        val mTitleTv: TextView = itemView.findViewById(R.id.titleTv)
        val mSubtitleTv: TextView = itemView.findViewById(R.id.subtitleTv)

        val mIconIv: ImageView = itemView.findViewById(R.id.iconIv)
        val mArrowIv: ImageView = itemView.findViewById(R.id.arrowIv)

        val mCardView: CardView = itemView.findViewById(R.id.cardView)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.Help.Item) {
                cardView(mCardView, theme)
                mainIcon(mIconIv, theme)
                arrowIcon(mArrowIv, theme)
                title(mTitleTv, theme)
                subtitle(mSubtitleTv, theme)
            }
        }

    }


}