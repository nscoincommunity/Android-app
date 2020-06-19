package com.stocksexchange.android.ui.base.multipledataloading

import androidx.annotation.CallSuper
import com.stocksexchange.android.events.RealTimeDataUpdateEvent
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.core.exceptions.NotFoundException
import com.stocksexchange.core.model.DataLoadingState
import com.stocksexchange.core.utils.interfaces.MultipleDataLoadable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A base presenter of the MVP architecture that has built-in
 * support for loading multiple types of data simultaneously.
 */
abstract class BaseMultipleDataLoadingPresenter<View, Model, DataSources>(
    view: View,
    model: Model
) : BasePresenter<View, Model>(view, model), MultipleDataLoadable<DataSources> where
    View : MultipleDataLoadingView<DataSources>,
    Model : BaseMultipleDataLoadingModel<DataSources, *>,
    DataSources : Enum<*> {


    /**
     * A boolean value denoting whether the real-time data update event has
     * been received or not.
     */
    protected var mIsRealTimeDataUpdateEventReceived = false

    /**
     * A map that stores what data of which data sources have been downloaded.
     */
    protected var mIsDataLoadingPerformedMap: MutableMap<DataSources, Boolean> = mutableMapOf()

    /**
     * A map that stores what main views of which data sources are yet
     * to be shown.
     */
    protected var mMainViewsToShowMap: MutableMap<DataSources, Boolean> = mutableMapOf()

    /**
     * A map that stores what main views of which data sources are currently
     * being shown.
     */
    protected var mMainViewsShowingMap: MutableMap<DataSources, Boolean> = mutableMapOf()




    override fun start() {
        super.start()

        showEmptyViewIfNecessary()
        loadDataOfAllSourcesIfNecessary()
    }


    private fun showEmptyViewIfNecessary() {
        for(dataSource in getDataSourcesArray()) {
            if(mView.isDataEmpty(dataSource) && !mModel.isDataLoading(dataSource)) {
                mView.hideMainView(dataSource)
                showEmptyView(dataSource)
            }
        }
    }


    private fun loadDataOfAllSourcesIfNecessary() {
        for(dataSource in getDataSourcesArray()) {
            if(isDataLoadingAllowed(dataSource)) {
                loadData(dataSource)
            }
        }
    }


    /**
     * A method that is used for determining whether to allow
     * data loading to be performed of the particular data source
     * or not.
     *
     * @param dataSource The data source
     *
     * @return true to allow; false otherwise
     */
    protected open fun isDataLoadingAllowed(dataSource: DataSources): Boolean {
        return (
            mView.isViewSelected(dataSource) &&
            mView.isDataEmpty(dataSource) &&
            mModel.isDataLoadingIntervalApplied(dataSource)
        )
    }


    /**
     * A method that starts the data loading.
     *
     * @param dataSource The data source
     */
    protected fun loadData(dataSource: DataSources) {
        if(mModel.isDataLoading(dataSource)) {
            return
        }

        mView.hideMainView(dataSource)
        mView.hideInfoView(dataSource)

        mModel.loadData(
            getTypeForLoadingData(dataSource),
            getDataParameters(dataSource),
            dataSource
        )
    }


    private fun getTypeForLoadingData(dataSource: DataSources): DataType {
        return if(isDataLoadingPerformed(dataSource)) DataType.OLD_DATA else DataType.NEW_DATA
    }


    protected open fun getDataParameters(dataSource: DataSources): Any {
        return mView.getDataParameters(dataSource)
    }


    @CallSuper
    protected open fun setDataLoadingPerformed(isDataLoadingPerformed: Boolean, dataSource: DataSources) {
        mIsDataLoadingPerformedMap[dataSource] = isDataLoadingPerformed
    }


    @CallSuper
    protected open fun isDataLoadingPerformed(dataSource: DataSources): Boolean {
        return (mIsDataLoadingPerformedMap[dataSource] == true)
    }


    protected abstract fun showEmptyView(dataSource: DataSources)


    protected open fun showErrorView(dataSource: DataSources, exception: Throwable) {
        mView.showErrorView(
            dataSource = dataSource,
            caption = mStringProvider.getErrorMessage(exception)
        )
    }


    override fun onDataLoadingStarted(dataSource: DataSources) {
        mView.showProgressBar(dataSource)
    }


    override fun onDataLoadingEnded(dataSource: DataSources) {
        if(!isMainViewCurrentlyShowing() || mView.isDataEmpty(dataSource)) {
            mView.hideProgressBar(dataSource)
        }

        if(mView.isDataEmpty(dataSource)) {
            showEmptyView(dataSource)
        } else {
            mView.hideInfoView(dataSource)
        }
    }


    override fun onDataLoadingStateChanged(dataLoadingState: DataLoadingState, dataSource: DataSources) {
        if(dataLoadingState == DataLoadingState.IDLE) {
            if(!mView.isDataEmpty(dataSource)) {
                if(!mModel.isDataLoadingCancelled(dataSource)) {
                    onShowMainViewFirstTime(dataSource)
                } else {
                    mView.hideProgressBar(dataSource)
                    mView.showMainView(dataSource, false)
                }
            }
        }
    }


    override fun onDataLoadingSucceeded(data: Any, dataSource: DataSources) {
        setDataLoadingPerformed(true, dataSource)

        mView.setData(data, false, dataSource)
    }


    override fun onDataLoadingFailed(error: Throwable, dataSource: DataSources) {
        setDataLoadingPerformed(true, dataSource)

        if(error is NotFoundException) {
            showEmptyView(dataSource)
        } else {
            showErrorView(dataSource, error)
        }
    }


    /**
     * A callback that gets invoked whenever the main view
     * should be shown the first time (usually, after first
     * successful data loading).
     *
     * @param dataSource The data source
     */
    @CallSuper
    protected open fun onShowMainViewFirstTime(dataSource: DataSources) {
        handleMainViewShowing(dataSource)
    }


    private fun handleMainViewShowing(dataSource: DataSources) {
        if(isMainViewCurrentlyShowing()) {
            mMainViewsToShowMap[dataSource] = true
        } else {
            mView.bindData(dataSource)
            mView.hideProgressBar(dataSource)
            mView.showMainView(dataSource, true)
        }
    }


    private fun showNextMainViewIfNecessary() {
        for(entry in mMainViewsToShowMap) {
            if(entry.value) {
                mMainViewsToShowMap.remove(entry.key)
                handleMainViewShowing(entry.key)

                return
            }
        }
    }


    /**
     * A callback that gets invoked whenever a main view show
     * animation has been started.
     *
     * @param dataSource The data source
     */
    open fun onMainViewShowAnimationStarted(dataSource: DataSources) {
        mMainViewsShowingMap[dataSource] = true
    }


    /**
     * A callback that gets invoked whenever a main view show
     * animation has been ended.
     *
     * @param dataSource The data source
     */
    open fun onMainViewShowAnimationEnded(dataSource: DataSources) {
        mMainViewsShowingMap[dataSource] = false

        showNextMainViewIfNecessary()
    }


    /**
     * Returns a boolean value denoting whether any of the main view is
     * currently showing up or not.
     *
     * @return true if some main view is showing up; false otherwise
     */
    protected fun isMainViewCurrentlyShowing(): Boolean {
        for(entry in mMainViewsShowingMap.entries) {
            if(entry.value) {
                return true
            }
        }

        return false
    }


    /**
     * Retrieves an array of data sources this presenter works with.
     *
     * @return The array of data sources
     */
    protected abstract fun getDataSourcesArray(): Array<DataSources>


    override fun onViewSelected() {
        loadDataOfAllSourcesIfNecessary()
    }


    /**
     * A callback that gets invoked when a user swipes down to refresh the data.
     */
    @CallSuper
    open fun onRefreshData() {
        loadDataOfAllSourcesIfNecessary()
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


    private fun performRealTimeDataUpdate() {
        resetDataLoadingPerformedFlags()

        for(dataSource in getDataSourcesArray()) {
            if(mView.isViewSelected(dataSource) &&
                (isDataSourceRealTimeDependant(dataSource) || mView.isDataEmpty(dataSource))) {
                loadData(dataSource)
            }
        }
    }


    private fun resetDataLoadingPerformedFlags() {
        for(dataSource in getDataSourcesArray()) {
            if(isDataSourceRealTimeDependant(dataSource)) {
                setDataLoadingPerformed(false, dataSource)
            }
        }
    }


    /**
     * Returns a boolean value denoting whether the passed data source is
     * real-time dependant or not, meaning whether it should have the latest
     * data as soon as possible.
     *
     * @param dataSource The data source to check
     *
     * @return true if it is real-time dependant; false otherwise
     */
    protected open fun isDataSourceRealTimeDependant(dataSource: DataSources): Boolean {
        return true
    }


    override fun canReceiveEvents(): Boolean {
        return true
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            mIsRealTimeDataUpdateEventReceived = it.isRealTimeDataUpdateEventReceived
            mIsDataLoadingPerformedMap = (it.isDataLoadingPerformedMap as MutableMap<DataSources, Boolean>)
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            isRealTimeDataUpdateEventReceived = mIsRealTimeDataUpdateEventReceived,
            isDataLoadingPerformedMap = (mIsDataLoadingPerformedMap as MutableMap<Any, Boolean>)
        ))
    }


}