package com.stocksexchange.android.ui.base.multipledataloading

import com.stocksexchange.android.ui.base.mvp.views.BaseView

/**
 * A view of the MVP architecture that contains methods
 * related to loading multiple types of data simultaneously.
 */
interface MultipleDataLoadingView<DataSources : Enum<*>> : BaseView {


    /**
     * Shows the main view of the particular data source.
     *
     * @param dataSource The source of data
     * @param shouldAnimate Whether to animate the showing process
     */
    fun showMainView(dataSource: DataSources, shouldAnimate: Boolean)


    /**
     * Hides the main view of the particular data source.
     *
     * @param dataSource The source of data
     */
    fun hideMainView(dataSource: DataSources)


    /**
     * Shows the progress bar of the particular data source.
     *
     * @param dataSource The source of data
     */
    fun showProgressBar(dataSource: DataSources)


    /**
     * Hides the progress bar of the particular data source.
     *
     * @param dataSource The source of data
     */
    fun hideProgressBar(dataSource: DataSources)


    /**
     * Shows the empty view of the particular data source.
     *
     * @param dataSource The source of data
     * @param caption The caption of the empty view
     */
    fun showEmptyView(dataSource: DataSources, caption: String)


    /**
     * Shows the error view of the particular data source.
     *
     * @param dataSource The source of data
     * @param caption The caption of the error view
     */
    fun showErrorView(dataSource: DataSources, caption: String)


    /**
     * Hides the info view of the particular data source.
     *
     * @param dataSource The source of data
     */
    fun hideInfoView(dataSource: DataSources)


    /**
     * Sets the data of the particular data source.
     *
     * @param data The data to set
     * @param shouldBindData Whether to bind data or not
     * @param dataSource The source of data
     */
    fun setData(data: Any, shouldBindData: Boolean, dataSource: DataSources)


    /**
     * Updates the data of the particular data source.
     *
     * @param data The new data
     * @param dataActionItems The items that have been changed
     * @param dataSource The source of data
     */
    fun updateData(data: Any, dataActionItems: List<Any>, dataSource: DataSources)


    /**
     * Binds the data of the particular data source.
     *
     * @param dataSource The source of data
     */
    fun bindData(dataSource: DataSources)


    /**
     * Clears the data of the particular data source.
     *
     * @param dataSource The source of data
     */
    fun clearData(dataSource: DataSources)


    /**
     * Checks whether the view of the particular data source is
     * selected (visible) or not.
     *
     * @param dataSource The source of data
     *
     * @return true if selected; false otherwise
     */
    fun isViewSelected(dataSource: DataSources): Boolean


    /**
     * Checks whether the data of the particular data source is
     * empty or not.
     *
     * @param dataSource The source of data
     *
     * @return true if empty; false otherwise
     */
    fun isDataEmpty(dataSource: DataSources): Boolean


    /**
     * Retrieves parameters of the particular data source for loading data.
     *
     * @param dataSource The source of data
     *
     * @return The parameters for loading data
     */
    fun getDataParameters(dataSource: DataSources): Any


}