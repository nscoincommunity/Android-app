package com.stocksexchange.android.ui.base.dataloading

import com.stocksexchange.android.ui.base.mvp.views.BaseView

/**
 * A view of the MVP architecture that contains methods
 * related to loading data.
 */
interface DataLoadingView<in Data> : BaseView {


    /**
     * Shows the main view holding the loaded data.
     */
    fun showMainView()


    /**
     * Hides the main view holding the loaded data.
     */
    fun hideMainView()


    /**
     * Shows the empty view in case there is no data available.
     *
     * @param caption The caption of the empty view
     */
    fun showEmptyView(caption: String)


    /**
     * Shows the error view in case an error occurred while loading data.
     *
     * @param caption The caption of the error view
     */
    fun showErrorView(caption: String)


    /**
     * Hides the info view (i.e., empty and error views).
     */
    fun hideInfoView()


    /**
     * Shows the progress bar to notify that the data loading has started.
     */
    fun showProgressBar()


    /**
     * Hides the progress bar to notify that the data loading has ended.
     */
    fun hideProgressBar()


    /**
     * Shows the refresh progress bar to notify that the data loading has started.
     */
    fun showRefreshProgressBar()


    /**
     * Hides the refresh progress bar to notify that the data loading has ended.
     */
    fun hideRefreshProgressBar()


    /**
     * Enables the refresh progress bar.
     */
    fun enableRefreshProgressBar()


    /**
     * Disables the refresh progress bar.
     */
    fun disableRefreshProgressBar()


    /**
     * Adds the data to the main view.
     *
     * @param data The data to add to the main view
     */
    fun addData(data: Data)


    /**
     * Checks whether the data source is empty (i.e., whether the data
     * has already been downloaded or not).
     *
     * @return true if empty; false otherwise
     */
    fun isDataSourceEmpty(): Boolean


    /**
     * Checks whether the view is currently selected (visible) or not.
     *
     * @return true if selected; false otherwise
     */
    fun isViewSelected(): Boolean


}