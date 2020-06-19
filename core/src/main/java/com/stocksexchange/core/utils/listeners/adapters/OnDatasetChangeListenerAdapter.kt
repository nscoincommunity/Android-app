package com.stocksexchange.core.utils.listeners.adapters

import com.arthurivanets.adapster.listeners.OnDatasetChangeListener

interface OnDatasetChangeListenerAdapter<DS : List<IT>, IT> : OnDatasetChangeListener<DS, IT> {


    override fun onItemAdded(dataSet: DS, item: IT?) {
        // Stub
    }


    override fun onItemUpdated(dataSet: DS, item: IT?) {
        // Stub
    }


    override fun onItemReplaced(dataSet: DS, oldItem: IT?, newItem: IT?) {
        // Stub
    }


    override fun onItemDeleted(dataSet: DS, item: IT?) {
        // Stub
    }


    override fun onDatasetSizeChanged(oldSize: Int, newSize: Int) {
        // Stub
    }


    override fun onDatasetReplaced(newDataSet: DS) {
        // Stub
    }


    override fun onDatasetCleared(dataSet: DS) {
        // Stub
    }


}