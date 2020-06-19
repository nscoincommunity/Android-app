package com.stocksexchange.android.ui.news.fragments.blog

import android.graphics.drawable.Drawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.android.mappings.mapToBlogNewsList
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.news_blog_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class BlogNewsFragment : BaseListDataLoadingFragment<
    BlogNewsPresenter,
    NewsBlogRssModel,
    BlogNewsItem,
    BlogNewsRecyclerViewAdapter
    >(), BlogNewsContract.View {


    companion object {}


    override val mPresenter: BlogNewsPresenter by inject { parametersOf(this) }




    override fun init() {
        super.init()

        initContentContainer()
    }


    override fun initAdapter(): BlogNewsRecyclerViewAdapter {
        return BlogNewsRecyclerViewAdapter(ctx, mItems).apply {
            setResources(BlogNewsResources.newInstance(
                settings = getSettings(),
                timeFormatter = get(),
                imageDownloader = get(),
                imageHeight = getImageHeight(),
                imageWidth = getImageWidth(),
                imageError = getCompatDrawable(R.drawable.ic_logo_with_black_background)!!
            ))

            onNewsItemClickListener = { _, blogNewsItem, _ ->
                mPresenter.onNewsItemClickListener(blogNewsItem)
            }
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


    override fun addData(data: NewsBlogRssModel) {
        var addedItemCount = 0
        val newsItemList = data.rssItems.mapToBlogNewsList().toMutableList()

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


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(ctx, url, getAppTheme())
    }


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getBlogNewsIcon()
    }


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getContentLayoutResourceId(): Int = R.layout.news_blog_fragment_layout


}