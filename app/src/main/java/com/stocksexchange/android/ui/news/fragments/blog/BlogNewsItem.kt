package com.stocksexchange.android.ui.news.fragments.blog

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
import com.stocksexchange.api.model.rss.NewsBlogItemModel
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.utils.ImageDownloader
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.extensions.setHeight

class BlogNewsItem(itemModel: NewsBlogItemModel) : BaseItem<
    NewsBlogItemModel,
    BlogNewsItem.ViewHolder,
    BlogNewsResources>(itemModel),
    Trackable<String>
{


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.news_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: BlogNewsResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            val theme = resources!!.settings.theme

            applyTheme(theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: BlogNewsResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val imageDownloader = resources!!.imageDownloader
            val timeFormatter = resources.timeFormatter
            val imageWidth = resources.imageWidth
            val imageHeight = resources.imageHeight
            val imageError = resources.imageError
            val dateStr = timeFormatter.formatNewsBlogPostPublicationTime(itemModel.pubDate)

            mImageIv.setHeight(imageHeight)

            mTypeTv.text = itemModel.titleWithoutTags

            if(dateStr.isNotEmpty()) {
                mDateTv.text = dateStr
                mDateTv.makeVisible()
            } else {
                mDateTv.makeGone()
            }

            imageDownloader.downloadImage(ImageDownloader.Builder()
                .imageUrl(itemModel.mediaContent.url)
                .errorImage(imageError)
                .centerCrop()
                .width(imageWidth)
                .height(imageHeight)
                .destination(mImageIv)
                .build()
            )
        }
    }


    fun setOnNewsItemClickListener(viewHolder: ViewHolder, position: Int,
                                       listener: ((View, BlogNewsItem, Int) -> Unit)?) {
        viewHolder.mCardView.setOnClickListener {
            listener?.invoke(it, this@BlogNewsItem, position)
        }
    }


    fun getBlogNewsLink(): String {
        return itemModel.link
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): String = itemModel.titleWithoutTags


    class ViewHolder(itemView: View) : BaseViewHolder<NewsBlogItemModel>(itemView) {

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