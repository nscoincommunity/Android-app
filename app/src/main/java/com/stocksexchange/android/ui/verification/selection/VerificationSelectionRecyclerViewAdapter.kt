package com.stocksexchange.android.ui.verification.selection

import android.content.Context
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter

class VerificationSelectionRecyclerViewAdapter(
    context: Context,
    items: MutableList<VerificationSelectionItem>
) : TrackableRecyclerViewAdapter<Int, VerificationSelectionItem, VerificationSelectionItem.ViewHolder>(context, items) {


    private lateinit var mResources: VerificationSelectionResources

    var onItemClickListener: ((VerificationSelectionItem.ViewHolder, VerificationSelectionItem, Int) -> Unit)? = null




    init {
        setHasStableIds(true)
    }


    override fun assignListeners(holder: VerificationSelectionItem.ViewHolder, position: Int, item: VerificationSelectionItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnItemClickListener(holder, position, onItemClickListener)
        }
    }


    fun setResources(resources: VerificationSelectionResources) {
        mResources = resources
    }


    override fun getResources(): ItemResources? = mResources


}