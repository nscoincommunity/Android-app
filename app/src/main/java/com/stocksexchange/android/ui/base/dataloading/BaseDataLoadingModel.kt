package com.stocksexchange.android.ui.base.dataloading

import com.stocksexchange.android.Constants
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingModel.BaseDataLoadingActionListener
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.core.model.DataLoadingState
import com.stocksexchange.core.utils.interfaces.DataLoadable
import kotlinx.coroutines.Job

/**
 * A base model of the MVP architecture that has built-in
 * support for data loading.
 */
abstract class BaseDataLoadingModel<
    Data,
    in Parameters,
    ActionListener: BaseDataLoadingActionListener<Data>
> : BaseModel<ActionListener>() {


    var wasLastDataFetchingSuccessful: Boolean = false
        private set

    var wasLastDataFetchingInitiatedByTheUser: Boolean = false
        private set

    var isDataLoading: Boolean = false
        private set

    var isDataLoadingCancelled: Boolean = false
        private set

    protected var mLastDataFetchingTime: Long = 0L

    protected var mDataLoadingJob: Job? = null

    var lastDataFetchingException: Throwable? = null




    override fun stop() {
        cancelDataLoading()

        super.stop()
    }


    open fun cancelDataLoading() {
        if(mDataLoadingJob?.isActive == true) {
            isDataLoadingCancelled = true

            mDataLoadingJob?.cancel()
            onDataLoadingEnded()
        }
    }


    protected open fun updateDataFetchingTimestamp() {
        mLastDataFetchingTime = System.currentTimeMillis()
    }


    open fun resetParameters() {
        wasLastDataFetchingSuccessful = false
        wasLastDataFetchingInitiatedByTheUser = false
        mLastDataFetchingTime = 0L
    }


    open fun handleSuccessfulResponse(data: Data) {
        if(isDataLoadingCancelled) {
            return
        }

        wasLastDataFetchingSuccessful = true
        lastDataFetchingException = null

        updateDataFetchingTimestamp()

        mActionListener?.onDataLoadingSucceeded(data)
        onDataLoadingEnded()
    }


    open fun handleUnsuccessfulResponse(error: Throwable) {
        if(isDataLoadingCancelled) {
            return
        }

        lastDataFetchingException = error
        updateDataFetchingTimestamp()

        onDataLoadingEnded()
        mActionListener?.onDataLoadingFailed(error)
    }


    protected open fun onDataLoadingStarted() {
        isDataLoading = true

        mActionListener?.onDataLoadingStarted()
        mActionListener?.onDataLoadingStateChanged(DataLoadingState.ACTIVE)
    }


    protected open fun onDataLoadingEnded() {
        isDataLoading = false

        mActionListener?.onDataLoadingEnded()
        mActionListener?.onDataLoadingStateChanged(DataLoadingState.IDLE)
    }


    fun hasLastDataFetchingException(): Boolean = (lastDataFetchingException != null)


    fun isDataLoadingIntervalApplied(): Boolean {
        return ((System.currentTimeMillis() - (mLastDataFetchingTime)) > Constants.MIN_DATA_REFRESHING_INTERVAL)
    }


    fun wasDataLoadingPerformed(): Boolean = (mLastDataFetchingTime > 0L)


    /**
     * A method that starts the data loading process. The data loading
     * won't be performed if [canLoadData] returns false. In that case,
     * [onDataLoadingEnded] callback is called. Otherwise, see [getDataAsync].
     *
     * @param params The parameters for loading data
     * @param dataType The type of data to load
     * @param dataLoadingTrigger The trigger that caused the data loading
     * @param wasInitiatedByTheUser Whether the loading was initiated
     * by the user
     */
    fun getData(params: Parameters, dataType: DataType,
                dataLoadingTrigger: DataLoadingTrigger, wasInitiatedByTheUser: Boolean) {
        // Altering the states
        wasLastDataFetchingInitiatedByTheUser = wasInitiatedByTheUser
        isDataLoadingCancelled = false

        if(!canLoadData(params, dataType, dataLoadingTrigger)) {
            onDataLoadingEnded()
            return
        }

        mDataLoadingJob = getDataAsync(params, dataType)
        onDataLoadingStarted()
    }


    /**
     * A method used for determining whether to allow data loading
     * to be performed or not. Should be overridden to provide
     * custom checks.
     *
     * @param params The parameters for loading data
     * @param dataType The type of data to load
     * @param dataLoadingTrigger The trigger that caused the data loading
     *
     * @return true if data can be loaded; false otherwise
     */
    open fun canLoadData(params: Parameters, dataType: DataType,
                         dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val isNewDataWithIntervalNotApplied = ((dataType == DataType.NEW_DATA) && !isDataLoadingIntervalApplied())
        val isNetworkConnectivityTrigger = (dataLoadingTrigger == DataLoadingTrigger.NETWORK_CONNECTIVITY)

        return (!isNewDataWithIntervalNotApplied || isNetworkConnectivityTrigger)
    }


    protected open suspend fun refreshData(params: Parameters) {
        // Left for subclass implementations
    }


    private fun getDataAsync(params: Parameters, dataType: DataType): Job {
        return createBgLaunchCoroutine {
            if(dataType == DataType.NEW_DATA) {
                refreshData(params)
            }

            performDataLoading(params)
        }
    }


    /**
     * A method actually responsible for fetching data.
     *
     * @param params The parameters for loading data
     */
    protected abstract suspend fun performDataLoading(params: Parameters)


    override fun onRestoreState(savedState: SavedState) {
        savedState.extract(modelStateExtractor).also {
            wasLastDataFetchingSuccessful = it.wasLastDataFetchingSuccessful
            wasLastDataFetchingInitiatedByTheUser = it.wasLastDataFetchingInitiatedByUser
            mLastDataFetchingTime = it.lastDataFetchingTime
            lastDataFetchingException = it.lastDataFetchingException
        }
    }


    override fun onSaveState(savedState: SavedState) {
        savedState.saveState(ModelState(
            wasLastDataFetchingSuccessful = wasLastDataFetchingSuccessful,
            wasLastDataFetchingInitiatedByUser = wasLastDataFetchingInitiatedByTheUser,
            lastDataFetchingTime = mLastDataFetchingTime,
            lastDataFetchingException = lastDataFetchingException
        ))
    }


    /**
     * A listener that interacts with a presenter.
     */
    interface BaseDataLoadingActionListener<in Data> : BaseActionListener, DataLoadable<Data>


}