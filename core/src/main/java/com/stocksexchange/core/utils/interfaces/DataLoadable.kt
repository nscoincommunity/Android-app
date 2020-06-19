package com.stocksexchange.core.utils.interfaces

import com.stocksexchange.core.model.DataLoadingState

/**
 * An interface to implement to mark a class to be capable
 * of loading the data.
 */
interface DataLoadable<in Data> {


    /**
     * This method is called to notify you that the data loading
     * has been started.
     */
    fun onDataLoadingStarted()


    /**
     * This method is called to notify you that the data loading
     * has been ended.
     */
    fun onDataLoadingEnded()


    /**
     * This method is called to notify you that the data loading
     * state has been changed.
     *
     * @param dataLoadingState One of data loading states. See [DataLoadingState].
     */
    fun onDataLoadingStateChanged(dataLoadingState: DataLoadingState)


    /**
     * This method is called to notify you that the data loading
     * has succeeded.
     *
     * @param data The actual data
     */
    fun onDataLoadingSucceeded(data: Data)


    /**
     * This method is called to notify you that the data loading
     * has failed.
     *
     * @param error The error
     */
    fun onDataLoadingFailed(error: Throwable)


}