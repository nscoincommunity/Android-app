package com.stocksexchange.android.ui.news.fragments.blog

import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.android.ui.base.listdataloading.ListDataLoadingView

interface BlogNewsContract {


    interface View : ListDataLoadingView<NewsBlogRssModel> {

        fun launchBrowser(url: String)

    }


    interface ActionListener {

        fun onNewsItemClickListener(item: BlogNewsItem)

    }


}