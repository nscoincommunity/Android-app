package com.stocksexchange.android.ui.views.base.interfaces

/**
 * A view interface that contains advanced features like
 * an informational view (empty and error), a main view, a
 * progress bar, and so on.
 */
interface AdvancedView<Data> : BaseView {


    /**
     * Shows the main view holding the loaded data.
     *
     * @param shouldAnimate Whether to animate the view or not
     */
    fun showMainView(shouldAnimate: Boolean)


    /**
     * Hides the main view holding the loaded data.
     */
    fun hideMainView()


    /**
     * Shows the progress bar to notify that the data loading has started.
     */
    fun showProgressBar()

    /**
     * Hides the progress bar to notify that the data loading has ended.
     */

    fun hideProgressBar()


    /**
     * Shows the empty view in case there is not data available.
     *
     * @param caption The empty caption
     */
    fun showEmptyView(caption: String)


    /**
     * Shows the error view in case an error occurred.
     *
     * @param caption The error caption
     */
    fun showErrorView(caption: String)


    /**
     * Hides the info view (empty and error).
     */
    fun hideInfoView()


    /**
     * Sets the data.
     *
     * @param data The data to set
     * @param shouldBindData Whether the data should be bound or not
     */
    fun setData(data: Data?, shouldBindData: Boolean)


    /**
     * Binds the previously set data to the main view.
     */
    fun bindData()


    /**
     * Clears the previously set data.
     */
    fun clearData()


    /**
     * Returns a boolean value indicating whether the data is
     * empty or not.
     *
     * @return true if empty; false otherwise
     */
    fun isDataEmpty(): Boolean


}