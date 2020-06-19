package com.stocksexchange.android.ui.language

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter

class LanguageRecyclerViewAdapter(
    context: Context,
    items: MutableList<LanguageItem>
) : TrackableRecyclerViewAdapter<Int, LanguageItem, LanguageItem.ViewHolder>(context, items) {


    private lateinit var mResources: LanguageResources

    var onLanguageItemClickListener: ((View, LanguageItem, Int) -> Unit)? = null




    override fun assignListeners(holder: LanguageItem.ViewHolder, position: Int, item: LanguageItem) {
        super.assignListeners(holder, position, item)

        item.setOnItemClickListener(holder, position, onLanguageItemClickListener)
    }


    fun setResources(resources: LanguageResources) {
        mResources = resources
    }


    override fun getResources(): LanguageResources? {
        return mResources
    }


}