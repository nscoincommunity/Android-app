package com.stocksexchange.android.ui.news.fragments.blog

import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.api.model.rest.parameters.NewsParameters
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingPresenter
import com.stocksexchange.android.ui.news.fragments.PresenterState
import com.stocksexchange.android.ui.news.fragments.presenterStateExtractor
import com.stocksexchange.android.ui.news.fragments.saveState
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract

class BlogNewsPresenter(
    view: BlogNewsContract.View,
    model: BlogNewsModel
) : BaseListDataLoadingPresenter<
    BlogNewsContract.View,
    BlogNewsModel,
    NewsBlogRssModel,
    NewsParameters
    >(view, model), BlogNewsContract.ActionListener, BlogNewsModel.ActionListener {


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
        return mStringProvider.getBlogNewsEmptyCaption()
    }


    override fun getDataLoadingParams(): NewsParameters {
        return mNewsParameters
    }


    override fun onNewsItemClickListener(item: BlogNewsItem) {
        mView.launchBrowser(item.getBlogNewsLink())
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