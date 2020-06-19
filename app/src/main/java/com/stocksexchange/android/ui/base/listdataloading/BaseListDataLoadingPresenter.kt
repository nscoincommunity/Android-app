package com.stocksexchange.android.ui.base.listdataloading

import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingModel
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingPresenter

/**
 * A base presenter of the MVP architecture that has built-in
 * support for loading list data.
 */
abstract class BaseListDataLoadingPresenter<out View, out Model, Data, Parameters>(
    view: View,
    model: Model
) : BaseDataLoadingPresenter<View, Model, Data, Parameters>(view, model) where
        View : ListDataLoadingView<Data>,
        Model : BaseDataLoadingModel<Data, Parameters, *> {


    open fun onDataSetSizeChanged(size: Int) {
        if(size > 0) {
            mView.hideInfoView()
            mView.hideProgressBar()
        } else {
            showEmptyView()
        }
    }


    fun onPerformSearch(query: String) {
        if(!mView.isInitialized() || (query == getSearchQuery())) return

        onSearchQueryChanged(query)
        onCancelDataLoading()

        if(query.isBlank()) {
            mView.hideMainView()
        } else {
            reloadData(DataLoadingTrigger.SEARCH_QUERY_CHANGE)
        }
    }


    open fun onSearchQueryChanged(query: String) {
        // Stub
    }


    /**
     * A callback that gets invoked whenever a user has reached the
     * bottom of the data set.
     *
     * @param reachedCompletely Whether a user has reached the
     * bottom completely
     */
    open fun onBottomReached(reachedCompletely: Boolean) {
        // Stub
    }


    open fun onCancelDataLoading() {
        mModel.cancelDataLoading()
    }


    /**
     * Retrieves the current search query.
     */
    protected open fun getSearchQuery(): String = ""


}