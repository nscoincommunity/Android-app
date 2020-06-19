package com.stocksexchange.android.ui.news.fragments.twitter

import android.content.Context
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter

class TwitterNewsRecyclerViewAdapter(
    context: Context,
    items: MutableList<TwitterNewsItem>
) : TrackableRecyclerViewAdapter<Long, TwitterNewsItem, TwitterNewsItem.ViewHolder>(context, items) {


    private lateinit var mResources: TwitterNewsResources




    fun setResources(resources: TwitterNewsResources) {
        mResources = resources
    }


    override fun getResources(): TwitterNewsResources? {
        return mResources
    }


}