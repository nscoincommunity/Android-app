package com.stocksexchange.android.ui.news.fragments.twitter

import com.stocksexchange.api.model.rest.parameters.NewsParameters
import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingPresenter
import com.stocksexchange.android.ui.news.fragments.PresenterState
import com.stocksexchange.android.ui.news.fragments.presenterStateExtractor
import com.stocksexchange.android.ui.news.fragments.saveState
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract

class TwitterNewsPresenter(
    view: TwitterNewsContract.View,
    model: TwitterNewsModel
) : BaseListDataLoadingPresenter<
    TwitterNewsContract.View,
    TwitterNewsModel,
    List<NewsTwitterItemModel>,
    NewsParameters
    >(view, model), TwitterNewsContract.ActionListener, TwitterNewsModel.ActionListener {


    private var mNewsParameters: NewsParameters = NewsParameters.getDefaultParameters()




    init {
        model.setActionListener(this)
    }


    override fun getDataTypeForTrigger(trigger: DataLoadingTrigger): DataType {
        return when(trigger) {
            DataLoadingTrigger.START -> DataType.NEW_DATA

            else -> super.getDataTypeForTrigger(trigger)
        }
    }


    override fun getEmptyViewCaption(params: NewsParameters): String {
        return mStringProvider.getTwitterNewsEmptyCaption()
    }


    override fun getDataLoadingParams(): NewsParameters {
        return mNewsParameters
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            mNewsParameters = it.newsParameters
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            newsParameters = mNewsParameters
        ))
    }


}