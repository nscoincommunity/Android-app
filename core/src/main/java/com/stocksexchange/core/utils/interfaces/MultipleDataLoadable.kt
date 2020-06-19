package com.stocksexchange.core.utils.interfaces

import com.stocksexchange.core.model.DataLoadingState

/**
 * An interface to implement to mark a class to be capable
 * of loading multiple types of data.
 */
interface MultipleDataLoadable<DataSources : Enum<*>> {


    /**
     * This method is called to notify you that the data loading
     * of the given data source (type) has been started.
     *
     * @param dataSource The source of data
     */
    fun onDataLoadingStarted(dataSource: DataSources)


    /**
     * This method is called to notify you that the data loading
     * of the given data source (type) has been ended.
     *
     * @param dataSource The source of data
     */
    fun onDataLoadingEnded(dataSource: DataSources)


    /**
     * This method is called to notify you that the data loading
     * state of the given data source (type) has been changed.
     *
     * @param dataLoadingState One of data loading states. See [DataLoadingState].
     * @param dataSource The source of data
     */
    fun onDataLoadingStateChanged(dataLoadingState: DataLoadingState, dataSource: DataSources)


    /**
     * This method is called to notify you that the data loading
     * of the given data source (type) has succeeded.
     *
     * @param data The actual data
     * @param dataSource The source of data
     */
    fun onDataLoadingSucceeded(data: Any, dataSource: DataSources)


    /**
     * This method is called to notify you that the data loading
     * of the given data source (type) has failed.
     *
     * @param error The error
     * @param dataSource The source of data
     */
    fun onDataLoadingFailed(error: Throwable, dataSource: DataSources)


}