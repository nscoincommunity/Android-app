package com.stocksexchange.core.utils.listeners

/**
 * A listener that tracks data set changes (insertions, deletions,
 * replacements, etc.)
 */
interface OnDataSetChangeListener<in DS : Collection<IT>, IT> {


    fun onItemAdded(dataSet: DS, item: IT) {
        // Stub
    }


    fun onItemUpdated(dataSet: DS, item: IT) {
        // Stub
    }


    fun onItemReplaced(dataSet: DS, oldItem: IT, newItem: IT) {
        // Stub
    }


    fun onItemDeleted(dataSet: DS, item: IT) {
        // Stub
    }


    fun onDataSetReplaced(newDataSet: DS) {
        // Stub
    }


    fun onDataSetCleared(dataSet: DS) {
        // Stub
    }


}