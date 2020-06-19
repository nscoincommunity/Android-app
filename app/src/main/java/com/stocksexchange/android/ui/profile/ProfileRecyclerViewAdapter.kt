package com.stocksexchange.android.ui.profile

import android.content.Context
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter

class ProfileRecyclerViewAdapter(
    context: Context,
    items: MutableList<ProfileItem>
) : TrackableRecyclerViewAdapter<Int, ProfileItem, ProfileItem.ViewHolder>(context, items) {


    private lateinit var mResources: ProfileItemResources

    var onItemClickListener: ((ProfileItem.ViewHolder, ProfileItem, Int) -> Unit)? = null




    init {
        setHasStableIds(true)
    }


    override fun assignListeners(holder: ProfileItem.ViewHolder, position: Int, item: ProfileItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnItemClickListener(holder, position, onItemClickListener)
        }
    }


    fun setResources(resources: ProfileItemResources) {
        mResources = resources
    }


    override fun getResources(): ItemResources? = mResources


}