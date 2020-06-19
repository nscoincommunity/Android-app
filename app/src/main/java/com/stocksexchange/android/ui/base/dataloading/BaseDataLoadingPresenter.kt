package com.stocksexchange.android.ui.base.dataloading

import androidx.annotation.CallSuper
import com.stocksexchange.android.events.RealTimeDataUpdateEvent
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.core.exceptions.NotFoundException
import com.stocksexchange.core.model.DataLoadingState
import com.stocksexchange.core.utils.interfaces.DataLoadable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A base presenter of the MVP architecture that has built-in
 * support for loading data.
 */
abstract class BaseDataLoadingPresenter<out View, out Model, Data, Parameters>(
    view: View,
    model: Model
) : BasePresenter<View, Model>(view, model),
    DataLoadable<Data> where
        View : DataLoadingView<Data>,
        Model : BaseDataLoadingModel<Data, Parameters, *> {


    var isRefreshProgressBarEnabled: Boolean = true

    protected var mIsPreviousDataSetEmpty: Boolean = true

    protected var mIsRealTimeDataUpdateEventReceived = false




    override fun start() {
        super.start()

        if(mView.isDataSourceEmpty() && !mModel.isDataLoading) {
            mView.hideMainView()
            showInfoView()
        }

        if(isDataLoadingAllowed()) {
            reloadData(DataLoadingTrigger.START)
        }
    }


    /**
     * A method that is used for determining whether to allow
     * data loading to be performed or not.
     *
     * @return true to allow; false otherwise
     */
    protected fun isDataLoadingAllowed(): Boolean {
        return ((mView.isDataSourceEmpty() || !mModel.wasLastDataFetchingSuccessful)
                && !mModel.isDataLoading
                && mView.isViewSelected()
                && mModel.isDataLoadingIntervalApplied())
    }


    /**
     * Reloads the data by resetting current parameters and then
     * performing a usual data loading.
     *
     * @param dataLoadingTrigger The trigger that caused the reload
     */
    protected open fun reloadData(dataLoadingTrigger: DataLoadingTrigger) {
        if(mModel.isDataLoading) {
            return
        }

        resetParameters()

        mView.hideMainView()
        mView.hideInfoView()

        loadData(
            getDataTypeForTrigger(dataLoadingTrigger),
            dataLoadingTrigger,
            false
        )
    }


    @CallSuper
    protected open fun resetParameters() {
        mModel.resetParameters()
    }


    protected open fun getDataTypeForTrigger(trigger: DataLoadingTrigger): DataType {
        return when(trigger) {
            DataLoadingTrigger.REAL_TIME_DATA_UPDATE -> DataType.NEW_DATA

            else -> DataType.OLD_DATA
        }
    }


    /**
     * Returns a boolean value denoting whether the refresh progress bar
     * should be disabled after the first successful data loading.
     *
     * @return true if should; false otherwise
     */
    protected open fun shouldDisableRpbAfterFirstSuccessfulDataLoading(): Boolean {
        return false
    }


    /**
     * A method that starts the data loading.
     *
     * @param dataType The type of data
     * @param dataLoadingTrigger The trigger that caused the data loading
     * @param wasInitiatedByTheUser Whether the loading was initiated by the user
     */
    protected open fun loadData(dataType: DataType, dataLoadingTrigger: DataLoadingTrigger, wasInitiatedByTheUser: Boolean) {
        mIsPreviousDataSetEmpty = mView.isDataSourceEmpty()

        mModel.getData(getDataLoadingParams(), dataType, dataLoadingTrigger, wasInitiatedByTheUser)
    }


    protected fun showEmptyView() {
        mView.showEmptyView(getEmptyViewCaption(getDataLoadingParams()))
    }


    protected fun showErrorView(exception: Throwable) {
        mView.showErrorView(getErrorViewCaption(exception))
    }


    /**
     * Shows the info view (either empty or error view).
     */
    private fun showInfoView() {
        if(!mModel.hasLastDataFetchingException()) {
            showEmptyView()
            return
        }

        val lastDataFetchingException = mModel.lastDataFetchingException!!

        if(shouldShowEmptyView(lastDataFetchingException)) {
            showEmptyView()
        } else {
            showErrorView(lastDataFetchingException)
        }
    }


    private fun enabledRefreshProgressBar() {
        isRefreshProgressBarEnabled = true

        mView.enableRefreshProgressBar()
    }


    private fun disableRefreshProgressBar() {
        isRefreshProgressBarEnabled = false

        mView.disableRefreshProgressBar()
    }


    protected open fun shouldShowEmptyView(lastDataFetchingException: Throwable): Boolean {
        return (lastDataFetchingException is NotFoundException)
    }


    protected abstract fun getEmptyViewCaption(params: Parameters): String


    protected open fun getErrorViewCaption(exception: Throwable): String {
        return mStringProvider.getErrorMessage(exception)
    }


    protected abstract fun getDataLoadingParams(): Parameters


    override fun onDataLoadingStarted() {
        mView.hideInfoView()

        // Showing the proper progress bar
        if(mView.isDataSourceEmpty() && !mModel.wasLastDataFetchingInitiatedByTheUser) {
            mView.hideRefreshProgressBar()
            disableRefreshProgressBar()
            mView.showProgressBar()
        } else {
            mView.showRefreshProgressBar()
        }
    }


    override fun onDataLoadingEnded() {
        // Hiding the proper progress bar
        if(mIsPreviousDataSetEmpty && !mModel.wasLastDataFetchingInitiatedByTheUser) {
            mView.hideProgressBar()
            enabledRefreshProgressBar()
        } else {
            mView.hideRefreshProgressBar()
        }

        // Just to make sure InfoView showing is handled
        if(mView.isDataSourceEmpty()) {
            showInfoView()
        } else {
            mView.hideInfoView()
            mView.hideProgressBar()
        }
    }


    override fun onDataLoadingSucceeded(data: Data) {
        mView.addData(data)
    }


    override fun onDataLoadingFailed(error: Throwable) {
        mView.hideInfoView()

        if(mView.isDataSourceEmpty()) {
            showInfoView()
        }
    }


    override fun onDataLoadingStateChanged(dataLoadingState: DataLoadingState) {
        if(dataLoadingState == DataLoadingState.IDLE) {
            if(mIsPreviousDataSetEmpty && !mView.isDataSourceEmpty() && !mModel.isDataLoadingCancelled) {
                onShowMainViewFirstTime()
            }
        }
    }


    /**
     * A callback that gets invoked whenever the main view
     * should be shown the first time (usually, after first
     * successful data loading).
     */
    @CallSuper
    protected open fun onShowMainViewFirstTime() {
        mView.showMainView()

        if(shouldDisableRpbAfterFirstSuccessfulDataLoading()) {
            disableRefreshProgressBar()
        }
    }


    override fun onViewSelected() {
        if(isDataLoadingAllowed()) {
            loadData(
                getDataTypeForTrigger(DataLoadingTrigger.VIEW_SELECTION),
                DataLoadingTrigger.VIEW_SELECTION,
                false
            )
        }
    }


    /**
     * A callback that gets invoked when a user swipes down to refresh the data.
     */
    open fun onRefreshData() {
        loadData(DataType.NEW_DATA, DataLoadingTrigger.REFRESHMENT, true)
    }


    override fun onNetworkConnected() {
        if(shouldLoadDataOnNetworkConnectivity()) {
            loadData(DataType.NEW_DATA, DataLoadingTrigger.NETWORK_CONNECTIVITY, false)
        }
    }


    /**
     * Returns a boolean value denoting whether to perform data loading
     * when the network has become available.
     *
     * @return true if should be performed; false otherwise
     */
    protected fun shouldLoadDataOnNetworkConnectivity(): Boolean {
        return ((mView.isDataSourceEmpty() || !mModel.wasLastDataFetchingSuccessful)
                && mView.isViewSelected()
                && !mModel.isDataLoading)
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: RealTimeDataUpdateEvent) {
        if(!isDataRealTimeDependant() ||
            event.isOriginatedFrom(this) ||
            event.isConsumed(this)) {
            return
        }

        mIsRealTimeDataUpdateEventReceived = true

        performRealTimeDataUpdate()

        event.consume(this)
    }


    /**
     * Returns a boolean value denoting whether the data is real-time
     * dependant so that it can be refreshed automatically if it is.
     *
     * @return true if dependant; false otherwise
     */
    protected open fun isDataRealTimeDependant(): Boolean = false


    protected open fun performRealTimeDataUpdate() {
        if(shouldLoadDataOnRealTimeUpdate()) {
            loadData(DataType.NEW_DATA, DataLoadingTrigger.REAL_TIME_DATA_UPDATE, false)
        }
    }


    /**
     * Returns a boolean value denoting whether to perform
     * data loading when the real-time update arrives.
     *
     * @return true if should be performed; false otherwise
     */
    protected open fun shouldLoadDataOnRealTimeUpdate(): Boolean = true


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            isRefreshProgressBarEnabled = it.isRefreshProgressBarEnabled
            mIsPreviousDataSetEmpty = it.isPreviousDataSetEmpty
            mIsRealTimeDataUpdateEventReceived = it.isRealTimeDataUpdateEventReceived
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            isRefreshProgressBarEnabled = isRefreshProgressBarEnabled,
            isPreviousDataSetEmpty = mIsPreviousDataSetEmpty,
            isRealTimeDataUpdateEventReceived = mIsRealTimeDataUpdateEventReceived
        ))
    }


}