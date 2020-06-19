package com.stocksexchange.android.ui.protocolselection

import android.content.Context
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter

class ProtocolSelectionRecyclerViewAdapter(
    context: Context,
    items: MutableList<ProtocolSelectionItem>
) : TrackableRecyclerViewAdapter<Int, ProtocolSelectionItem, ProtocolSelectionItem.ViewHolder>(context, items) {


    private lateinit var mResources: ProtocolSelectionResources

    var onItemClickListener: ((ProtocolSelectionItem.ViewHolder, ProtocolSelectionItem, Int) -> Unit)? = null




    init {
        setHasStableIds(true)
    }


    override fun assignListeners(holder: ProtocolSelectionItem.ViewHolder, position: Int, item: ProtocolSelectionItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnItemClickListener(holder, position, onItemClickListener)
        }
    }


    fun setResources(resources: ProtocolSelectionResources) {
        mResources = resources
    }


    override fun getResources(): ProtocolSelectionResources? = mResources


}