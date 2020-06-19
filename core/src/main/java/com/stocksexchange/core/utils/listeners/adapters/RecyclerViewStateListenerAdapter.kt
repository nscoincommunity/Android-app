package com.stocksexchange.core.utils.listeners.adapters

import androidx.recyclerview.widget.RecyclerView
import com.stocksexchange.core.utils.listeners.RecyclerViewScrollListener

interface RecyclerViewStateListenerAdapter : RecyclerViewScrollListener.StateListener {


    override fun onScrolledUpwards(recyclerView: RecyclerView, deltaY: Int) {
        // Stub
    }


    override fun onScrolledDownwards(recyclerView: RecyclerView, deltaY: Int) {
        // Stub
    }


    override fun onTopReached(recyclerView: RecyclerView, reachedCompletely: Boolean) {
        // Stub
    }


    override fun onMiddleReached(recyclerView: RecyclerView, direction: RecyclerViewScrollListener.Direction) {
        // Stub
    }


    override fun onBottomReached(recyclerView: RecyclerView, reachedCompletely: Boolean) {
        // Stub
    }


}