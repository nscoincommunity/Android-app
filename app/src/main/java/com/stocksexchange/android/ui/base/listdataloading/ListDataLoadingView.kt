package com.stocksexchange.android.ui.base.listdataloading

import com.stocksexchange.android.ui.base.dataloading.DataLoadingView
import com.stocksexchange.core.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.core.utils.interfaces.Scrollable

/**
 * A view of the MVP architecture that contains methods
 * related to loading list data.
 */
interface ListDataLoadingView<Data> : DataLoadingView<Data>, Scrollable,
    CanObserveNetworkStateChanges {


    /**
     * Returns the size of the data set of the main view.
     *
     * @return The size of the data set
     */
    fun getDataSetSize(): Int


}