package com.stocksexchange.android.ui.base.viewpager

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import com.stocksexchange.core.utils.extensions.attemptToScrollUp
import com.stocksexchange.core.utils.extensions.attemptToSelect
import com.stocksexchange.core.utils.interfaces.Selectable
import com.stocksexchange.core.utils.listeners.adapters.OnTabSelectedListenerAdapter

/**
 * A base activity that hosts a ViewPager widget.
 */
abstract class BaseViewPagerFragment<VPA : BaseViewPagerAdapter, P : BaseViewPagerPresenter<*, *>> : BaseFragment<P>(),
    ViewPagerView {


    companion object {

        private const val DEFAULT_INITIAL_TAB_POSITION = 0

    }


    protected lateinit var mAdapter: VPA




    override fun init() {
        super.init()

        initToolbar()
        initViewPager()
        initTabLayout()
    }


    protected open fun initToolbar() {
        getToolbar()?.apply {
            setOnLeftButtonClickListener {
                onBackPressed()
            }
            setTitleText(getToolbarTitle())

            ThemingUtil.Main.toolbar(this, getAppTheme())
        }
    }


    protected open fun initViewPager() {
        getViewPager().apply {
            mAdapter = getViewPagerAdapter()
            mAdapter.viewPagerId = this.id

            populateAdapter()

            adapter = mAdapter
            offscreenPageLimit = mAdapter.count
            addOnPageChangeListener(object : SimpleOnPageChangeListener() {

                override fun onPageSelected(position: Int) {
                    mPresenter.onViewPagerPageSelected(position)
                }

            })
        }
    }


    protected abstract fun populateAdapter()


    protected open fun initTabLayout() {
        getTabLayout().apply {
            setupWithViewPager(getViewPager())
            getTabAt(getInitialTabPosition())?.select()
            addOnTabSelectedListener(mOnTabSelectedListener)

            initTabLayoutTabs()

            ThemingUtil.TabBar.tabLayout(this, getAppTheme())
        }
    }


    protected abstract fun initTabLayoutTabs()


    override fun scrollToTop() {
        mPresenter.onScrollToTopRequested(getViewPager().currentItem)
    }


    override fun setFragmentSelected(position: Int, isSelected: Boolean, source: Selectable.Source) {
        mAdapter.getFragment(position)?.attemptToSelect(isSelected, source)
    }


    override fun scrollFragmentToTop(position: Int) {
        mAdapter.getFragment(position)?.attemptToScrollUp()
    }


    protected open fun getInitialTabPosition(): Int = DEFAULT_INITIAL_TAB_POSITION


    protected abstract fun getToolbarTitle(): String


    protected abstract fun getToolbar(): Toolbar?


    protected abstract fun getViewPager(): ViewPager


    protected abstract fun getViewPagerAdapter(): VPA


    protected abstract fun getTabLayout(): TabLayout


    override fun getCurrentlyVisibleChildFragment(): Fragment? {
        return mAdapter.getFragment(getViewPager().currentItem)
    }


    @CallSuper
    protected open fun onTabSelected(tab: TabLayout.Tab) {
        mPresenter.onTabSelected(tab.position)
    }


    @CallSuper
    protected open fun onTabUnselected(tab: TabLayout.Tab) {
        mPresenter.onTabUnselected(tab.position)
    }


    @CallSuper
    protected open fun onTabReselected(tab: TabLayout.Tab) {
        mPresenter.onTabReselected(tab.position)
    }


    private val mOnTabSelectedListener: OnTabSelectedListenerAdapter = object : OnTabSelectedListenerAdapter {

        override fun onTabSelected(tab: TabLayout.Tab) {
            this@BaseViewPagerFragment.onTabSelected(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            this@BaseViewPagerFragment.onTabUnselected(tab)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            this@BaseViewPagerFragment.onTabReselected(tab)
        }

    }


}