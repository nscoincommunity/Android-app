package com.stocksexchange.android.ui.protocolselection

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
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.utils.ImageDownloader

class ProtocolSelectionItem(itemModel: Protocol) : BaseItem<Protocol, ProtocolSelectionItem.ViewHolder, ProtocolSelectionResources>(itemModel) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.protocol_selection_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: ProtocolSelectionResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            applyTheme(resources!!.settings.theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: ProtocolSelectionResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val iconSize = resources!!.dimensions[ProtocolSelectionResources.DIMENSION_ICON_SIZE]
            val imageUrl = String.format(
                Constants.STEX_PROTOCOL_IMAGE_URL_TEMPLATE,
                resources.currencyId,
                itemModel.id
            )
            val imageDownloader = resources.imageDownloader
            val errorImage = resources.errorImage

            imageDownloader.downloadImage(ImageDownloader.Builder()
                .imageUrl(imageUrl)
                .size(iconSize)
                .centerInside()
                .errorImage(errorImage)
                .destination(mIconIv)
                .build()
            )

            mNameTv.text = itemModel.name
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((ViewHolder, ProtocolSelectionItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(viewHolder, this@ProtocolSelectionItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    class ViewHolder(itemView: View) : BaseViewHolder<Protocol>(itemView) {

        val mNameTv: TextView = itemView.findViewById(R.id.nameTv)

        val mIconIv: ImageView = itemView.findViewById(R.id.iconIv)

        val mCardView: CardView = itemView.findViewById(R.id.cardView)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.ProtocolSelection.Item) {
                cardView(mCardView, theme)
                name(mNameTv, theme)
            }
        }

    }


}