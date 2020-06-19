package com.stocksexchange.android.ui.news

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.R
import com.stocksexchange.android.model.NewsTab
import com.stocksexchange.android.ui.base.viewpager.BaseViewPagerFragment
import com.stocksexchange.android.ui.news.fragments.blog.BlogNewsFragment
import com.stocksexchange.android.ui.news.fragments.twitter.TwitterNewsFragment
import com.stocksexchange.android.ui.news.fragments.newInstance
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import kotlinx.android.synthetic.main.news_container_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class NewsContainerFragment : BaseViewPagerFragment<
    NewsContainerViewPagerAdapter,
    NewsContainerPresenter
    >(), NewsContainerContract.View {


    override val mPresenter: NewsContainerPresenter by inject { parametersOf(this) }




    override fun populateAdapter() = with(mAdapter) {
        addFragment(getFragment(NewsTab.TWITTER.ordinal) ?: TwitterNewsFragment.newInstance())
        addFragment(getFragment(NewsTab.BLOG.ordinal) ?: BlogNewsFragment.newInstance())
    }


    override fun initTabLayoutTabs() = with(mRootView.tabLayout) {
        getTabAt(NewsTab.TWITTER.ordinal)?.text = getText(R.string.twitter)
        getTabAt(NewsTab.BLOG.ordinal)?.text = getText(R.string.blog)
    }


    override fun showAppBar(animate: Boolean) = with(mRootView.appBarLayout) {
        setExpanded(true, animate)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.news_container_fragment_layout


    override fun getInitialTabPosition(): Int = NewsTab.TWITTER.ordinal


    override fun getToolbarTitle(): String = getStr(R.string.news)


    override fun getToolbar(): Toolbar? = mRootView.toolbar


    override fun getTabLayout(): TabLayout = mRootView.tabLayout


    override fun getViewPager(): ViewPager = mRootView.viewPager


    override fun getViewPagerAdapter(): NewsContainerViewPagerAdapter {
        return NewsContainerViewPagerAdapter(childFragmentManager)
    }


}