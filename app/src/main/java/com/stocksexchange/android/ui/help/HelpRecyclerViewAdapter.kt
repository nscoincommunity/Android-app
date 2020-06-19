package com.stocksexchange.android.ui.help

import android.content.Context
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter

class HelpRecyclerViewAdapter(
    context: Context,
    items: MutableList<HelpItem>
) : TrackableRecyclerViewAdapter<Int, HelpItem, HelpItem.ViewHolder>(context, items) {


    private lateinit var mResources: HelpItemResources

    var onItemClickListener: ((HelpItem.ViewHolder, HelpItem, Int) -> Unit)? = null




    init {
        setHasStableIds(true)
    }


    override fun assignListeners(holder: HelpItem.ViewHolder, position: Int, item: HelpItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnItemClickListener(holder, position, onItemClickListener)
        }
    }


    fun setResources(resources: HelpItemResources) {
        mResources = resources
    }


    override fun getResources(): ItemResources? = mResources


}