package com.stocksexchange.android.ui.dashboard

import android.os.Bundle
import androidx.core.view.isVisible
import com.stocksexchange.android.R
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.balance.BalanceContainerFragment
import com.stocksexchange.android.ui.balance.newArgs
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.base.fragments.NavigationFragment
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.login.newInstance
import com.stocksexchange.android.ui.orders.OrdersContainerFragment
import com.stocksexchange.android.ui.verification.prompt.VerificationPromptActivity
import com.stocksexchange.android.ui.verification.prompt.newInstance
import com.stocksexchange.android.utils.listeners.handleSettingsChangeEvent
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.interfaces.Selectable
import com.stocksexchange.core.utils.listeners.BottomNavigationManager
import kotlinx.android.synthetic.main.dashboard_activity_layout.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class DashboardActivity : BaseActivity<DashboardPresenter>(), DashboardContract.View {


    companion object {}


    override val mPresenter: DashboardPresenter by inject { parametersOf(this) }

    private lateinit var mAdapter: DashboardViewPagerAdapter




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.dashboardArgs = it.dashboardArgs
        }
    }


    override fun init() {
        super.init()

        initViewPager()
        initBottomNavigationView()
    }


    private fun initViewPager() = with(viewPager) {
        initViewPagerAdapter()

        isSwipeable = false
        adapter = mAdapter
        offscreenPageLimit = mAdapter.count
    }


    private fun initViewPagerAdapter() {
        mAdapter = DashboardViewPagerAdapter(supportFragmentManager).apply {
            viewPagerId = viewPager.id

            for(page in DashboardPage.values()) {
                val nullableFragment = getFragment(page.ordinal)
                val fragment = if(nullableFragment == null) {
                    NavigationFragment.newInstance(
                        graphResId = page.navigationGraphId,
                        startDestinationArgs = page.getInitialArgs()
                    )
                } else {
                    (nullableFragment as NavigationFragment)
                }.apply {
                    bottomNavigationManager = this@DashboardActivity.bottomNavigationManager
                }

                addFragment(fragment)
            }
        }
    }


    private fun initBottomNavigationView() = with(bottomNavView) {
        with(menu) {
            for(item in DashboardBottomMenuItem.values()) {
                addItem(
                    id = item.id,
                    title = getStr(item.stringId),
                    icon = getCompatDrawable(item.iconId)
                )
            }
        }

        setOnNavigationItemSelectedListener {
            mPresenter.onNavigationItemSelected(it.itemId)
        }
        setOnNavigationItemReselectedListener {
            mPresenter.onNavigationItemReselected(it.itemId)
        }

        // Selecting after initialization of fragments
        postAction {
            selectedItemId = mPresenter.dashboardArgs.selectedBottomMenuItem.id
        }

        isVisible = mPresenter.isBottomNavigationVisible

        ThemingUtil.Dashboard.bottomNavigationView(this, getAppTheme())
    }


    override fun showBottomNavigation() {
        bottomNavShadowView.makeVisible()
        bottomNavView.makeVisible()
    }


    override fun hideBottomNavigation() {
        bottomNavShadowView.makeGone()
        bottomNavView.makeGone()
    }


    override fun scrollPageToTop(position: Int) {
        mAdapter.getFragment(position)?.attemptToScrollUp()
    }


    override fun restartActivity(dashboardArgs: DashboardArgs) {
        startActivity(newInstance(
            context = this,
            dashboardArgs = dashboardArgs
        ))
        finishActivity()
    }


    override fun launchVerificationPromptActivity(descriptionType: VerificationPromptDescriptionType) {
        startActivity(VerificationPromptActivity.newInstance(
            context = this,
            descriptionType = descriptionType
        ))
    }


    override fun launchLoginActivity(dashboardArgs: DashboardArgs) {
        startActivity(LoginActivity.newInstance(
            context = ctx,
            destinationIntent = newInstance(ctx, dashboardArgs)
        ))
    }


    override fun notifyPageAboutSettingsChange(position: Int, newSettings: Settings) {
        mAdapter.getFragment(position)?.handleSettingsChangeEvent(newSettings)
    }


    override fun applyNewTheme(theme: Theme) {
        initSystemWindowColors()

        ThemingUtil.Dashboard.bottomNavigationView(bottomNavView, theme)
    }


    override fun setPageSelected(position: Int, isSelected: Boolean, source: Selectable.Source) {
        mAdapter.getFragment(position)?.attemptToSelect(isSelected, source)
    }


    override fun setViewPagerCurrentItemPosition(position: Int, smoothScroll: Boolean) {
        viewPager.setCurrentItem(position, smoothScroll)
    }


    override fun canObserveNetworkStateChanges(): Boolean = true


    override fun isViewPagerAdapterEmpty(): Boolean = mAdapter.isEmpty()


    override fun getViewPagerCurrentItemPosition(): Int = viewPager.currentItem


    override fun getContentLayoutResourceId(): Int = R.layout.dashboard_activity_layout


    override fun getEnterTransitionAnimations(): TransitionAnimations {
        return TransitionAnimations.FADING_ANIMATIONS
    }


    override fun getExitTransitionAnimations(): TransitionAnimations {
        return TransitionAnimations.DEFAULT_ANIMATIONS
    }


    private fun DashboardPage.getInitialArgs(): Bundle? {
        val args = mPresenter.dashboardArgs.startDestinationsArgsMap[this]

        return args ?: when(this) {
            DashboardPage.BALANCE -> BalanceContainerFragment.newArgs()
            DashboardPage.ORDERS -> OrdersContainerFragment.newArgs()

            else -> null
        }
    }


    override fun handleBackPressEvent(): Boolean {
        return (mAdapter.getFragment(viewPager.currentItem)?.handleBackPressEvent() ?: false)
    }


    private val bottomNavigationManager = object : BottomNavigationManager {

        override fun showBottomNavigation() {
            mPresenter.onShowBottomNavigation()
        }

        override fun hideBottomNavigation() {
            mPresenter.onHideBottomNavigation()
        }

    }


}