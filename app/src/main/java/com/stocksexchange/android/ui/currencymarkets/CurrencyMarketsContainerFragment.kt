package com.stocksexchange.android.ui.currencymarkets

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.viewpager.BaseViewPagerFragment
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsFragment
import com.stocksexchange.android.ui.currencymarkets.fragment.newFavoritesInstance
import com.stocksexchange.android.ui.currencymarkets.fragment.newNormalInstance
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.extensions.attemptToSort
import kotlinx.android.synthetic.main.currency_markets_container_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CurrencyMarketsContainerFragment : BaseViewPagerFragment<
    CurrencyMarketsContainerViewPagerAdapter,
    CurrencyMarketsContainerPresenter
    >(), CurrencyMarketsContainerContract.View {


    companion object {

        private const val TAB_POSITION_FAVORITE_MARKETS = 0

    }


    override val mPresenter: CurrencyMarketsContainerPresenter by inject { parametersOf(this) }

    private val mPreferenceHandler: PreferenceHandler by inject()




    override fun init() {
        super.init()

        initCurrencyMarketsSortPanel()
    }


    override fun initToolbar() = with(mRootView.toolbar) {
        super.initToolbar()

        setOnRightButtonClickListener {
            mPresenter.onToolbarRightButtonClicked()
        }

        if(mSessionManager.isUserSignedIn() && Constants.IMPLEMENTATION_NOTIFICATION_TURN_ON) {
            setOnInboxButtonClickListener {
                mPresenter.onToolbarInboxButtonClicked()
            }

            updateInboxButtonItemCount()
        } else {
            hideInboxButton()
        }

        if(mSessionManager.isUserSignedIn()) {
            setOnAlertPriceButtonClickListener {
                mPresenter.onToolbarAlertPriceButtonClicked()
            }

            showAlertPriceButton()
        } else {
            hideAlertPriceButton()
        }
    }


    override fun populateAdapter() = with(mAdapter) {
        val currencyPairGroups = mPresenter.currencyPairGroups

        addFragment(getFragment(TAB_POSITION_FAVORITE_MARKETS) ?: CurrencyMarketsFragment.newFavoritesInstance())

        for(i in currencyPairGroups.indices) {
            addFragment(getFragment(i + 1) ?: CurrencyMarketsFragment.newNormalInstance(currencyPairGroups[i]))
        }
    }


    override fun initTabLayoutTabs() = with(mRootView.tabLayout) {
        val currencyPairGroups = mPresenter.currencyPairGroups

        getTabAt(TAB_POSITION_FAVORITE_MARKETS)?.text = getStr(R.string.favorites)

        for(i in currencyPairGroups.indices) {
            getTabAt(i + 1)?.text = currencyPairGroups[i].name
        }
    }


    private fun initCurrencyMarketsSortPanel() = with(mRootView.currencyMarketsSortPanel) {
        onSortPanelTitleClickListener = {
            mPresenter.onSortPanelTitleClicked(it.comparator)
        }

        initComparator()

        ThemingUtil.CurrencyMarketsContainer.sortPanel(this, getAppTheme())
    }


    override fun showAppBar(animate: Boolean) = with(mRootView.appBarLayout) {
        setExpanded(true, animate)
    }


    override fun sortAdapterItems(comparator: CurrencyMarketComparator) {
        mAdapter.fragments.attemptToSort(comparator)
    }


    override fun updateInboxButtonItemCount() {
        mRootView.toolbar.setInboxButtonCountMessage(mPreferenceHandler.getInboxUnreadCount())
    }


    override fun navigateToSearchScreen() {
        navigate(R.id.currencyMarketsSearchDest)
    }


    override fun navigateToPriceAlertsScreen() {
        navigate(R.id.priceAlertsDest)
    }


    override fun navigateToInboxScreen() {
        navigate(R.id.inboxDest)
    }


    override fun getInitialTabPosition(): Int {
        val hasFavoriteCurrencyPairs = (mSessionManager.getFavoriteCurrencyPairsCount() > 0)
        val isCurrencyPairGroupsEmpty = mPresenter.currencyPairGroups.isEmpty()

        return if(hasFavoriteCurrencyPairs || isCurrencyPairGroupsEmpty) {
            TAB_POSITION_FAVORITE_MARKETS
        } else {
            (TAB_POSITION_FAVORITE_MARKETS + 1)
        }
    }


    override fun getTabCount(): Int = mRootView.tabLayout.tabCount


    override fun getContentLayoutResourceId(): Int = R.layout.currency_markets_container_fragment_layout


    override fun getToolbarTitle(): String = getStr(R.string.markets)


    override fun getToolbar(): Toolbar? = mRootView.toolbar


    override fun getViewPager(): ViewPager = mRootView.viewPager


    override fun getViewPagerAdapter(): CurrencyMarketsContainerViewPagerAdapter {
        return CurrencyMarketsContainerViewPagerAdapter(childFragmentManager)
    }


    override fun getTabLayout(): TabLayout = mRootView.tabLayout


}