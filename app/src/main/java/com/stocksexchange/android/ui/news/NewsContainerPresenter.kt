package com.stocksexchange.android.ui.news

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.viewpager.BaseViewPagerPresenter

class NewsContainerPresenter(
    view: NewsContainerContract.View,
    model: StubModel
) : BaseViewPagerPresenter<NewsContainerContract.View, StubModel>(view, model),
    NewsContainerContract.ActionListener {


    override fun onScrollToTopRequested(position: Int) {
        mView.showAppBar(true)

        super.onScrollToTopRequested(position)
    }


}