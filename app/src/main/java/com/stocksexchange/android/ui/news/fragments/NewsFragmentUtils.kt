package com.stocksexchange.android.ui.news.fragments

import com.stocksexchange.android.ui.news.fragments.blog.BlogNewsFragment
import com.stocksexchange.android.ui.news.fragments.twitter.TwitterNewsFragment
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.parameters.NewsParameters


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        newsParameters = getOrThrow(PresenterStateKeys.KEY_NEWS_PARAMETERS)
    )
}


internal object PresenterStateKeys {

    const val KEY_NEWS_PARAMETERS = "news_parameters"

}


internal data class PresenterState(
    val newsParameters: NewsParameters
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_NEWS_PARAMETERS, state.newsParameters)
}


fun TwitterNewsFragment.Companion.newInstance(): TwitterNewsFragment {
    return TwitterNewsFragment()
}


fun BlogNewsFragment.Companion.newInstance(): BlogNewsFragment {
    return BlogNewsFragment()
}