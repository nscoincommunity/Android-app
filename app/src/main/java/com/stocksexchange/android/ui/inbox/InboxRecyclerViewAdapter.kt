package com.stocksexchange.android.ui.inbox

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.stocksexchange.api.model.rest.Inbox

class InboxRecyclerViewAdapter(
    context: Context,
    items: MutableList<InboxItem>
) : TrackableRecyclerViewAdapter<Long, InboxItem, InboxItem.ViewHolder>(context, items) {


    private lateinit var mResources: InboxResources

    var onItemClickListener: ((View, InboxItem, Int) -> Unit)? = null
    var onItemDeleteListener: ((View, InboxItem, Int) -> Unit)? = null




    override fun assignListeners(holder: InboxItem.ViewHolder, position: Int, item: InboxItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnClickListener(holder, position, onItemClickListener)
            setOnDeleteListener(holder, position, onItemDeleteListener)
        }
    }


    fun getItemPosition(item: Inbox): Int? {
        return items.indices.firstOrNull {
            items[it].itemModel.id == item.id
        }
    }


    fun setResources(resources: InboxResources) {
        mResources = resources
    }


    override fun getResources(): InboxResources? {
        return mResources
    }


}