package com.stocksexchange.android.ui.news.fragments.twitter

import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.android.ui.base.listdataloading.ListDataLoadingView

interface TwitterNewsContract {


    interface View : ListDataLoadingView<List<NewsTwitterItemModel>>


    interface ActionListener


}