package com.stocksexchange.android.ui.news.fragments.twitter

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
import com.arthurivanets.adapster.model.markers.Trackable
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.utils.ImageDownloader
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.extensions.setHeight

class TwitterNewsItem(itemModel: NewsTwitterItemModel) : BaseItem<
    NewsTwitterItemModel,
    TwitterNewsItem.ViewHolder,
    TwitterNewsResources>(itemModel),
    Trackable<String>
{


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.news_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: TwitterNewsResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            val theme = resources!!.settings.theme

            applyTheme(theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: TwitterNewsResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val imageDownloader = resources!!.imageDownloader
            val timeFormatter = resources.timeFormatter
            val imageWidth = resources.imageWidth
            val imageHeight = resources.imageHeight
            val dateStr = timeFormatter.formatNewsTweetPublicationTime(
                itemModel.publicationTimestampInMillis
            )

            mImageIv.setHeight(imageHeight)

            mTypeTv.text = itemModel.message

            if(dateStr.isNotEmpty()) {
                mDateTv.text = dateStr
                mDateTv.makeVisible()
            } else {
                mDateTv.makeGone()
            }

            if(itemModel.hasImageUrls) {
                imageDownloader.downloadImage(ImageDownloader.Builder()
                    .imageUrl(itemModel.imageUrls[0])
                    .centerCrop()
                    .width(imageWidth)
                    .height(imageHeight)
                    .destination(mImageIv)
                    .build()
                )
            } else {
                mImageIv.setImageResource(R.drawable.ic_logo_with_black_background)
            }
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): String = itemModel.message


    class ViewHolder(itemView: View) : BaseViewHolder<NewsTwitterItemModel>(itemView) {

        val mTypeTv: TextView = itemView.findViewById(R.id.titleTv)

        val mImageIv: ImageView = itemView.findViewById(R.id.imageIv)

        val mCardView: CardView = itemView.findViewById(R.id.cardView)

        val mDateTv: TextView = itemView.findViewById(R.id.dateTv)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.News.NewsItem) {
                cardView(mCardView, theme)
                titleView(mTypeTv, theme)
                dateView(mDateTv, theme)
                imageView(mImageIv, theme)
            }
        }


    }


}