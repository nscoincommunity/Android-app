package com.stocksexchange.android.ui.news.fragments.twitter

import android.graphics.drawable.Drawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.NewsTwitterItemModel
import com.stocksexchange.android.mappings.mapToTwitterNewsList
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.core.utils.extensions.ctx
import com.stocksexchange.core.utils.extensions.dimenInPx
import kotlinx.android.synthetic.main.news_twitter_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class TwitterNewsFragment : BaseListDataLoadingFragment<
    TwitterNewsPresenter,
    List<NewsTwitterItemModel>,
    TwitterNewsItem,
    TwitterNewsRecyclerViewAdapter
    >(), TwitterNewsContract.View {


    companion object {}


    override val mPresenter: TwitterNewsPresenter by inject { parametersOf(this) }




    override fun init() {
        super.init()

        initContentContainer()
    }


    override fun initAdapter(): TwitterNewsRecyclerViewAdapter {
        return TwitterNewsRecyclerViewAdapter(ctx, mItems).apply {
            setResources(TwitterNewsResources.newInstance(
                settings = getSettings(),
                timeFormatter = get(),
                imageDownloader = get(),
                imageHeight = getImageHeight(),
                imageWidth = getImageWidth()
            ))
        }
    }


    private fun getImageWidth() : Int {
        val screenWidth = resources.displayMetrics.widthPixels

        return screenWidth - (dimenInPx(R.dimen.news_card_padding) * 2)
    }


    private fun getImageHeight() : Int{
        val imageHeightCoefficient = 675F / 1200

        return (getImageWidth() * imageHeightCoefficient).toInt()
    }


    private fun initContentContainer() {
        ThemingUtil.News.contentContainer(mRootView.contentContainerFl, getAppTheme())
    }


    override fun addData(data: List<NewsTwitterItemModel>) {
        var addedItemCount = 0
        val newsItemList = data.mapToTwitterNewsList().toMutableList()

        for(newsItem in newsItemList) {
            if(mAdapter.contains(newsItem)) {
                mAdapter.updateItemWith(newsItem)
            } else {
                mAdapter.addItem(newsItem, false)
                addedItemCount++
            }
        }

        if(addedItemCount > 0) {
            mAdapter.notifyItemRangeChanged(mAdapter.itemCount, addedItemCount)
        }
    }


    override fun shouldDisableRVAnimations(): Boolean {
        return false
    }


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getTwitterNewsIcon()
    }


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getContentLayoutResourceId(): Int = R.layout.news_twitter_fragment_layout


}