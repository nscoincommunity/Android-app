package com.stocksexchange.android.ui.news.fragments.blog

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter

class BlogNewsRecyclerViewAdapter(
    context: Context,
    items: MutableList<BlogNewsItem>
) : TrackableRecyclerViewAdapter<Long, BlogNewsItem, BlogNewsItem.ViewHolder>(context, items) {


    private lateinit var mResources: BlogNewsResources

    var onNewsItemClickListener: ((View, BlogNewsItem, Int) -> Unit)? = null




    override fun assignListeners(holder: BlogNewsItem.ViewHolder, position: Int, item: BlogNewsItem) {
        super.assignListeners(holder, position, item)

        item.setOnNewsItemClickListener(holder, position, onNewsItemClickListener)
    }


    fun setResources(resources: BlogNewsResources) {
        mResources = resources
    }


    override fun getResources(): BlogNewsResources? {
        return mResources
    }


}