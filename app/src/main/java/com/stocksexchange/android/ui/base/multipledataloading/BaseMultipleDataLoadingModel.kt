package com.stocksexchange.android.ui.base.multipledataloading

import androidx.annotation.CallSuper
import com.stocksexchange.android.Constants
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import com.stocksexchange.android.ui.base.multipledataloading.BaseMultipleDataLoadingModel.BaseMultipleDataLoadingActionListener
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.core.model.DataLoadingState
import com.stocksexchange.core.utils.interfaces.MultipleDataLoadable
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

/**
 * A base model of the MVP architecture that has built-in
 * support for loading multiple types of data simultaneously.
 * Achieves this by using a data source model class that contains
 * all possible types of data to load.
 */
abstract class BaseMultipleDataLoadingModel<
    DataSources : Enum<*>,
    ActionListener : BaseMultipleDataLoadingActionListener<DataSources>
> : BaseModel<ActionListener>() {


    /**
     * A map that contains integers denoting the last data fetching time
     * of the particular data source.
     */
    protected var mLastDataFetchingTimeMap: MutableMap<DataSources, Long> = mutableMapOf()

    /**
     * A map that contains boolean values denoting whether data is currently
     * being loaded of the particular data source.
     */
    protected var mIsDataLoadingMap: MutableMap<DataSources, Boolean> = mutableMapOf()

    /**
     * A map that contains boolean values denoting whether data loading has been
     * cancelled of the particular data source.
     */
    protected var mIsDataLoadingCancelledMap: MutableMap<DataSources, Boolean> = mutableMapOf()

    /**
     * A map that contains data loading coroutine jobs of the data sources.
     */
    protected var mDataLoadingJobsMap: MutableMap<DataSources, Job?> = mutableMapOf()




    override fun stop() {
        cancelAllDataLoading()

        super.stop()
    }


    /**
     * Cancels all data loading and returns immediately.
     */
    fun cancelAllDataLoading() {
        for(dataSource in getDataSourcesArray()) {
            cancelDataLoading(dataSource)
        }
    }


    /**
     * Retrieves an array of data sources this model loads.
     *
     * @return The array of data sources
     */
    protected abstract fun getDataSourcesArray(): Array<DataSources>


    /**
     * Cancels data loading of the particular data source and returns immediately.
     *
     * @param dataSource The data source
     *
     * @return true if data loading has been cancelled; false otherwise
     */
    fun cancelDataLoading(dataSource: DataSources): Boolean {
        return if(mDataLoadingJobsMap[dataSource]?.isActive == true) {
            mIsDataLoadingCancelledMap[dataSource] = true

            mDataLoadingJobsMap[dataSource]?.cancel()
            onDataLoadingEnded(dataSource)

            true
        } else {
            false
        }
    }


    /**
     * Cancels data loading of the particular data source and awaits the moment
     * when the loading has been cancelled completely.
     *
     * @param dataSource The data source
     * @param onCancelled The block to run upon complete cancellation
     */
    fun cancelDataLoadingAndWait(dataSource: DataSources, onCancelled: () -> Unit) {
        if(cancelDataLoading(dataSource)) {
            mDataLoadingJobsMap[dataSource]?.invokeOnCompletion {
                onCancelled()
            }
        } else {
            onCancelled()
        }
    }


    private fun updateDataFetchingTimestamp(dataSource: DataSources) {
        mLastDataFetchingTimeMap[dataSource] = System.currentTimeMillis()
    }


    @CallSuper
    protected open fun onDataLoadingStarted(dataSource: DataSources) {
        mIsDataLoadingMap[dataSource] = true

        mActionListener?.onDataLoadingStarted(dataSource)
        mActionListener?.onDataLoadingStateChanged(DataLoadingState.ACTIVE, dataSource)
    }


    @CallSuper
    protected open fun onDataLoadingEnded(dataSource: DataSources) {
        mIsDataLoadingMap[dataSource] = false

        mActionListener?.onDataLoadingEnded(dataSource)
        mActionListener?.onDataLoadingStateChanged(DataLoadingState.IDLE, dataSource)
    }


    private fun onDataLoadingSucceeded(dataSource: DataSources, data: Any) {
        if(mIsDataLoadingCancelledMap[dataSource] == true) {
            return
        }

        updateDataFetchingTimestamp(dataSource)

        mActionListener?.onDataLoadingSucceeded(data, dataSource)
        onDataLoadingEnded(dataSource)
    }


    private fun onDataLoadingFailed(dataSource: DataSources, error: Throwable) {
        if(mIsDataLoadingCancelledMap[dataSource] == true) {
            return
        }

        updateDataFetchingTimestamp(dataSource)

        onDataLoadingEnded(dataSource)
        mActionListener?.onDataLoadingFailed(error, dataSource)
    }


    /**
     * A method starts the data loading.
     *
     * @param dataType The data source
     * @param dataSource The type of data to load
     * @param parameters The parameters for loading data
     */
    @Suppress("UNCHECKED_CAST")
    fun loadData(dataType: DataType, parameters: Any, dataSource: DataSources) {
        mIsDataLoadingCancelledMap[dataSource] = false

        val startOption = getCoroutineStartOption(dataSource)

        // Needs to be UI coroutine, otherwise cancelAndWait method will crash
        mDataLoadingJobsMap[dataSource] = createUiLaunchCoroutine(startOption) {
            if(dataType == DataType.NEW_DATA) {
                refreshData(parameters, dataSource)
            }

            val repositoryResult = getRepositoryResult(parameters, dataSource)

            withContext(Dispatchers.Main) {
                (repositoryResult as RepositoryResult<Any>)
                    .onSuccess { onDataLoadingSucceeded(dataSource, it) }
                    .onFailure { onDataLoadingFailed(dataSource, it) }
            }
        }

        onDataLoadingStarted(dataSource)
    }


    protected abstract suspend fun refreshData(parameters: Any, dataSource: DataSources)


    /**
     * Retrieves a configuration that determines how to start
     * a coroutine.
     *
     * @param dataSource The data source
     *
     * @return The coroutine start option
     */
    protected open fun getCoroutineStartOption(dataSource: DataSources): CoroutineStart {
        return CoroutineStart.DEFAULT
    }


    /**
     * A method that is directly responsible for loading data.
     *
     * @param parameters The parameters for loading data
     * @param dataSource The data source
     *
     * @return The repository result
     */
    protected abstract suspend fun getRepositoryResult(parameters: Any, dataSource: DataSources): Any


    fun isDataLoadingIntervalApplied(dataSource: DataSources): Boolean {
        return ((System.currentTimeMillis() - (mLastDataFetchingTimeMap[dataSource] ?: 0L)) > Constants.MIN_DATA_REFRESHING_INTERVAL)
    }


    fun isDataLoading(dataSource: DataSources): Boolean {
       return (mIsDataLoadingMap[dataSource] ?: false)
    }


    fun isDataLoadingCancelled(dataSource: DataSources): Boolean {
        return (mIsDataLoadingCancelledMap[dataSource] ?: false)
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: SavedState) {
        savedState.extract(modelStateExtractor).also {
            mLastDataFetchingTimeMap = (it.lastDataFetchingTimeMap as MutableMap<DataSources, Long>)
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun onSaveState(savedState: SavedState) {
        savedState.saveState(ModelState(
            lastDataFetchingTimeMap = (mLastDataFetchingTimeMap as MutableMap<Any, Long>)
        ))
    }


    /**
     * A listener that interacts with a presenter.
     */
    interface BaseMultipleDataLoadingActionListener<DataSources : Enum<*>> : BaseActionListener,
        MultipleDataLoadable<DataSources>


}