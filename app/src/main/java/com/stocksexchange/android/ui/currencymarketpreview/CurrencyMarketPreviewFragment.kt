package com.stocksexchange.android.ui.currencymarketpreview

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.model.*
import com.stocksexchange.android.model.CurrencyMarketPreviewBottomTab.*
import com.stocksexchange.android.model.CurrencyMarketPreviewTopTab.DEPTH_CHART
import com.stocksexchange.android.model.CurrencyMarketPreviewTopTab.PRICE_CHART
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.alertprice.additem.AlertPriceAddAFragment
import com.stocksexchange.android.ui.alertprice.additem.newArgs
import com.stocksexchange.android.ui.currencymarketpreview.views.base.BaseTradingChartView
import com.stocksexchange.android.ui.currencymarketpreview.views.chartviews.PriceChartView.OnChartIntervalChangeListener
import com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory.items.TradeHistoryItem
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.views.base.interfaces.AdvancedView
import com.stocksexchange.android.utils.extensions.setData
import com.stocksexchange.android.utils.handlers.FiatCurrencyPriceHandler
import com.stocksexchange.android.utils.helpers.showTip
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import com.stocksexchange.android.ui.login.newInstance
import com.stocksexchange.android.ui.orderbook.OrderbookFragment
import com.stocksexchange.android.ui.orderbook.newArgs
import com.stocksexchange.android.ui.trade.TradeFragment
import com.stocksexchange.android.ui.trade.newArgs
import com.stocksexchange.android.utils.DashboardArgsCreator
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.listeners.OnMainViewShowAnimationListener
import com.stocksexchange.core.utils.listeners.adapters.OnTabSelectedListenerAdapter
import kotlinx.android.synthetic.main.currency_market_preview_fragment_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CurrencyMarketPreviewFragment : BaseFragment<CurrencyMarketPreviewPresenter>(),
    CurrencyMarketPreviewContract.View {


    companion object {

        const val TAG = "CurrencyMarketPreviewFragment"

    }


    override val mPresenter: CurrencyMarketPreviewPresenter by inject { parametersOf(this) }

    private lateinit var mMarketPreviewViewsMap: Map<CurrencyMarketPreviewDataSource, AdvancedView<*>>

    private var mChartContainerViews: Array<View> = arrayOf()

    private val mPreferenceHandler: PreferenceHandler by inject()
    private val mFiatCurrencyPriceHandler: FiatCurrencyPriceHandler by inject()

    private val mNumberFormatter: NumberFormatter by inject()




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.currencyMarket = it.currencyMarket
        }
    }


    override fun preInit() {
        super.preInit()

        mMarketPreviewViewsMap = mapOf(
            CurrencyMarketPreviewDataSource.PRICE_CHART to mRootView.priceChartView,
            CurrencyMarketPreviewDataSource.DEPTH_CHART to mRootView.depthChartView,
            CurrencyMarketPreviewDataSource.ORDERBOOK to mRootView.orderbookView,
            CurrencyMarketPreviewDataSource.TRADE_HISTORY to mRootView.tradeHistoryView,
            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS to mRootView.userActiveOrdersView,
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS to mRootView.userHistoryOrdersView
        )
    }


    override fun init() {
        super.init()

        val currencyMarket = mPresenter.currencyMarket

        initToolbar(currencyMarket)
        initSwipeRefreshLayout()
        initScrollView(currencyMarket)
        initBottomBar()
    }


    private fun initToolbar(currencyMarket: CurrencyMarket) = with(mRootView.toolbar) {
        setTitleText(String.format(
            "%s / %s",
            currencyMarket.baseCurrencySymbol,
            currencyMarket.quoteCurrencySymbol
        ))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }
        setOnPreRightButtonClickListener {
            mPresenter.onPreRightButtonClicked()
        }
        setOnRightButtonClickListener {
            mPresenter.onRightButtonClicked()
        }

        if(mSessionManager.isUserSignedIn() && Constants.IMPLEMENTATION_NOTIFICATION_TURN_ON) {
            setOnInboxButtonClickListener {
                mPresenter.onInboxButtonClicked()
            }

            updateInboxButtonItemCount()
        } else {
            hideInboxButton()
        }

        if(mSessionManager.isUserSignedIn()) {
            setOnAlertPriceButtonClickListener {
                mPresenter.onAlertPriceButtonClicked()
            }

            showAlertPriceButton()
        } else {
            hideAlertPriceButton()
        }

        ThemingUtil.CurrencyMarketPreview.toolbar(this, getAppTheme())

        initToolbarChartsVisibilityButton()
        initToolbarFavoriteButton(currencyMarket)
    }


    private fun initSwipeRefreshLayout() = with(mRootView.swipeRefreshLayout) {
        setOnRefreshListener {
            mPresenter.onRefreshData()
        }

        ThemingUtil.CurrencyMarketPreview.swipeRefreshLayout(this, getAppTheme())
    }


    override fun hideRefreshProgressBar() {
        if(mRootView.swipeRefreshLayout.isRefreshing) {
            mRootView.swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun initToolbarChartsVisibilityButton() {
        updateChartsVisibilityButtonState(getSettings().areMarketPreviewChartsVisible)
    }


    private fun initToolbarFavoriteButton(currencyMarket: CurrencyMarket) {
        mCoroutineHandler.createBgLaunchCoroutine {
            val isFavorite = get<CurrencyMarketsRepository>().isCurrencyMarketFavorite(currencyMarket)

            withContext(Dispatchers.Main) {
                updateFavoriteButtonState(isFavorite)
            }
        }
    }


    private fun initScrollView(currencyMarket: CurrencyMarket) {
        initHeadline(currencyMarket)
        initPriceInfoView(currencyMarket)
        initChartsContainer(currencyMarket)
        initTradingViewsContainer(currencyMarket)

        ThemingUtil.CurrencyMarketPreview.scrollView(mRootView.scrollView, getAppTheme())
    }


    private fun initHeadline(currencyMarket: CurrencyMarket) = with(mRootView.headlineTv) {
        if(currencyMarket.currencyPair.hasMessage) {
            text = currencyMarket.currencyPair.message
            makeVisible()
        } else {
            makeGone()
        }

        ThemingUtil.CurrencyMarketPreview.headline(this, getAppTheme())
    }


    private fun initPriceInfoView(currencyMarket: CurrencyMarket) = with(mRootView.priceInfoView) {
        setPriceInfoViewData(currencyMarket, false)

        ThemingUtil.CurrencyMarketPreview.priceInfoView(this, getAppTheme())
    }


    private fun initChartsContainer(currencyMarket: CurrencyMarket) {
        initTopTabLayout()
        initChartViews(currencyMarket)

        mChartContainerViews = arrayOf(
            mRootView.topShadow,
            mRootView.topTabLayout,
            mRootView.priceChartView,
            mRootView.depthChartView
        )

        if(getSettings().areMarketPreviewChartsVisible) {
            showChartsContainer()
        } else {
            hideChartsContainer()
        }
    }


    private fun initTopTabLayout() = with(mRootView.topTabLayout) {
        addTab(newTab().setText(getStr(R.string.price_chart)), PRICE_CHART.ordinal)
        addTab(newTab().setText(getStr(R.string.depth_chart)), DEPTH_CHART.ordinal)

        getTabAt(mPresenter.selectedTopTab.ordinal)?.select()
        addOnTabSelectedListener(object : OnTabSelectedListenerAdapter {

            override fun onTabSelected(tab: TabLayout.Tab) {
                mPresenter.onTopTabSelected(tab.position)
            }

        })

        ThemingUtil.CurrencyMarketPreview.tabLayout(this, getAppTheme())
    }


    private fun initChartViews(currencyMarket: CurrencyMarket) {
        initPriceChartView(currencyMarket)
        initDepthChartView(currencyMarket)

        showSelectedChartView()
    }


    private fun initPriceChartView(currencyMarket: CurrencyMarket) = with(mRootView.priceChartView) {
        val settings = getSettings()
        val infoViewIconProvider: InfoViewIconProvider = get()

        setChartAnimationEnabled(settings.shouldAnimateCharts)
        setChartZoomInEnabled(settings.isPriceChartZoomInEnabled)
        setCurrencyPairId(currencyMarket.pairId)
        setBullishCandleStickStyle(settings.bullishCandleStickStyle)
        setBearishCandleStickStyle(settings.bearishCandleStickStyle)
        setInfoViewIcon(infoViewIconProvider.getChartViewIcon())
        onChartTouchListener = mOnChartTouchListener
        onChartIntervalChangeListener = mOnPriceChartIntervalChangeListener
        onMainViewShowAnimationListener = OnMainViewShowAnimationListener(
            CurrencyMarketPreviewDataSource.PRICE_CHART,
            mOnMainViewShowAnimationCallback
        )

        ThemingUtil.CurrencyMarketPreview.priceChartView(this, getAppTheme())
    }


    private fun initDepthChartView(currencyMarket: CurrencyMarket) = with(mRootView.depthChartView) {
        val settings = getSettings()
        val infoViewIconProvider: InfoViewIconProvider = get()

        setChartAnimationEnabled(settings.shouldAnimateCharts)
        setCurrencyPairId(currencyMarket.pairId)
        setLineStyle(settings.depthChartLineStyle)
        setInfoViewIcon(infoViewIconProvider.getChartViewIcon())
        onChartTouchListener = mOnChartTouchListener
        onTabSelectedListener = {
            mPresenter.onDepthChartTabSelected(it)
        }
        onMainViewShowAnimationListener = OnMainViewShowAnimationListener(
            CurrencyMarketPreviewDataSource.DEPTH_CHART,
            mOnMainViewShowAnimationCallback
        )

        ThemingUtil.CurrencyMarketPreview.depthChartView(this, getAppTheme())
    }


    private fun initTradingViewsContainer(currencyMarket: CurrencyMarket) {
        initBottomTabLayout()
        initTradingViews(currencyMarket)
    }


    private fun initBottomTabLayout() = with(mRootView.bottomTabLayout) {
        addTab(newTab().setText(getStr(R.string.orderbook)), ORDERBOOK.ordinal)
        addTab(newTab().setText(getStr(R.string.all_trades)), TRADE_HISTORY.ordinal)

        if(mSessionManager.isUserSignedIn()) {
            addTab(newTab().setText(getStr(R.string.my_orders)), MY_ORDERS.ordinal)
            addTab(newTab().setText(getStr(R.string.my_history)), MY_HISTORY.ordinal)
        }

        getTabAt(mPresenter.selectedBottomTab.ordinal)?.select()
        addOnTabSelectedListener(object : OnTabSelectedListenerAdapter {

            override fun onTabSelected(tab: TabLayout.Tab) {
                mPresenter.onBottomTabSelected(tab.position)
            }

        })

        ThemingUtil.CurrencyMarketPreview.tabLayout(this, getAppTheme())
    }


    private fun initTradingViews(currencyMarket: CurrencyMarket) {
        initOrderbookView(currencyMarket)
        initTradeHistoryView(currencyMarket)
        initUserActiveOrdersView(currencyMarket)
        initUserHistoryOrdersView(currencyMarket)

        showSelectedTradingView()
    }


    private fun initOrderbookView(currencyMarket: CurrencyMarket) = with(mRootView.orderbookView) {
        val infoViewIconProvider: InfoViewIconProvider = get()

        setHighlightingEnabled(getSettings().isOrderbookHighlightingEnabled)
        setCurrencyPairId(currencyMarket.pairId)
        setInfoViewIcon(infoViewIconProvider.getOrderbookViewIcon())
        setOnHeaderMoreButtonClickListener { _, _, _ ->
            mPresenter.onOrderbookHeaderMoreButtonClicked()
        }
        setOnItemClickListener { _, orderbookOrderItem, _ ->
            mPresenter.onOrderbookOrderItemClicked(orderbookOrderItem.itemModel)
        }
        onMainViewShowAnimationListener = OnMainViewShowAnimationListener(
            CurrencyMarketPreviewDataSource.ORDERBOOK,
            mOnMainViewShowAnimationCallback
        )

        ThemingUtil.CurrencyMarketPreview.orderbookView(this, getAppTheme())
    }


    private fun initTradeHistoryView(currencyMarket: CurrencyMarket) = with(mRootView.tradeHistoryView) {
        val infoViewIconProvider: InfoViewIconProvider = get()

        setHighlightingEnabled(getSettings().isNewTradesHighlightingEnabled)
        setCurrencyPairId(currencyMarket.pairId)
        setInfoViewIcon(infoViewIconProvider.getTradeHistoryViewIcon())
        onMainViewShowAnimationListener = OnMainViewShowAnimationListener(
            CurrencyMarketPreviewDataSource.TRADE_HISTORY,
            mOnMainViewShowAnimationCallback
        )

        ThemingUtil.CurrencyMarketPreview.tradeHistoryView(this, getAppTheme())
    }


    private fun initUserActiveOrdersView(currencyMarket: CurrencyMarket) = with(mRootView.userActiveOrdersView) {
        val infoViewIconProvider: InfoViewIconProvider = get()

        setDataTruncationEnabled(false)
        setItemSwipeMenuEnabled(true)
        setHighlightingEnabled(true)
        setCurrencyPairId(currencyMarket.pairId)
        setInfoViewIcon(infoViewIconProvider.getUserActiveOrdersViewIcon())
        onMainViewShowAnimationListener = OnMainViewShowAnimationListener(
            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS,
            mOnMainViewShowAnimationCallback
        )
        onItemCancelBtnClickListener = { _, item, _ ->
            mPresenter.onActiveOrderCancelButtonClicked(item.itemModel)
        }

        ThemingUtil.CurrencyMarketPreview.tradeHistoryView(this, getAppTheme())
    }


    private fun initUserHistoryOrdersView(currencyMarket: CurrencyMarket) = with(mRootView.userHistoryOrdersView) {
        val infoViewIconProvider: InfoViewIconProvider = get()

        setHighlightingEnabled(true)
        setCurrencyPairId(currencyMarket.pairId)
        setInfoViewIcon(infoViewIconProvider.getUserHistoryOrdersViewIcon())
        onMainViewShowAnimationListener = OnMainViewShowAnimationListener(
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS,
            mOnMainViewShowAnimationCallback
        )

        ThemingUtil.CurrencyMarketPreview.tradeHistoryView(this, getAppTheme())
    }


    private fun initBottomBar() {
        with(mRootView.buyBtn) {
            text = getStr(R.string.action_buy)
            setOnClickListener {
                mPresenter.onTradeButtonClicked(TradeType.BUY)
            }
        }

        with(mRootView.sellBtn) {
            text = getStr(R.string.action_sell)
            setOnClickListener {
                mPresenter.onTradeButtonClicked(TradeType.SELL)
            }
        }

        with(ThemingUtil.CurrencyMarketPreview) {
            val theme = getAppTheme()

            bottomBar(mRootView.bottomBarLl, theme)
            buyButton(mRootView.buyBtn, theme)
            sellButton(mRootView.sellBtn, theme)
        }
    }


    private fun showSelectedChartView() {
        val chartViewDataSources = CurrencyMarketPreviewDataSource.values().filter { it.isChartView }
        val selectedChartViewDataSource = chartViewDataSources.first { isViewSelected(it) }

        for(dataSource in chartViewDataSources) {
            if(dataSource == selectedChartViewDataSource) {
                showView(dataSource)
            } else {
                hideView(dataSource)
            }
        }
    }


    private fun showSelectedTradingView() {
        val tradingViewDataSources = CurrencyMarketPreviewDataSource.values().filter { it.isTradingView }
        val selectedTradingViewDataSource = tradingViewDataSources.first { isViewSelected(it) }

        for(dataSource in tradingViewDataSources) {
            if(dataSource == selectedTradingViewDataSource) {
                showView(dataSource)
            } else {
                hideView(dataSource)
            }
        }
    }


    override fun getSelectedAmount(orderbookOrderData: OrderbookOrderData): Double {
        return mRootView.orderbookView.getSelectedAmount(orderbookOrderData)
    }


    override fun showChartsContainer() {
        mChartContainerViews.forEach {
            it.makeVisible()
        }

        showSelectedChartView()
    }


    override fun hideChartsContainer() {
        mChartContainerViews.forEach {
            it.makeGone()
        }
    }


    override fun showView(dataSource: CurrencyMarketPreviewDataSource) {
        getMarketPreviewView(dataSource)
            .takeIf { it.isInvisible }
            ?.apply { makeVisible() }
    }


    override fun hideView(dataSource: CurrencyMarketPreviewDataSource) {
        getMarketPreviewView(dataSource)
            .takeIf { it.isVisible }
            ?.apply { makeInvisible() }
    }


    override fun showUserActiveOrdersViewItemProgressBar(item: TradeData) {
        mRootView.userActiveOrdersView.showItemProgressBar(item)
    }


    override fun hideUserActiveOrdersViewItemProgressBar(item: TradeData) {
        mRootView.userActiveOrdersView.hideItemProgressBar(item)
    }


    override fun showOrderCancellationTip() {
        val viewHolder = mRootView.userActiveOrdersView.getRecyclerViewChildViewHolder(1)
        val tradeHistoryViewHolder = ((viewHolder as? TradeHistoryItem.ViewHolder) ?: return)
        val targetView = tradeHistoryViewHolder.mTimeTv
        val listener = object : TapTargetView.Listener() {

            override fun onTargetDismissed(view: TapTargetView, userInitiated: Boolean) {
                tradeHistoryViewHolder.mSwipeRevealLayout.open(true)
            }

        }

        showTip(
            activity = act,
            targetView = targetView,
            theme = getAppTheme(),
            title = getStr(R.string.tip_preview_order_cancellation_title),
            description = getStr(R.string.tip_preview_order_cancellation_description),
            listener = listener
        ).apply {
            setOnTouchListener { _, _ ->
                setOnTouchListener(null)
                dismiss(true)
                true
            }
        }
    }


    override fun enableScrollViewScrolling() {
        mRootView.scrollView.isScrollable = true
    }


    override fun disableScrollViewScrolling() {
        mRootView.scrollView.isScrollable = false
    }


    override fun showMainView(dataSource: CurrencyMarketPreviewDataSource, shouldAnimate: Boolean) {
        mMarketPreviewViewsMap[dataSource]?.showMainView(shouldAnimate)
    }


    override fun hideMainView(dataSource: CurrencyMarketPreviewDataSource) {
        mMarketPreviewViewsMap[dataSource]?.hideMainView()
    }


    override fun showProgressBar(dataSource: CurrencyMarketPreviewDataSource) {
        mMarketPreviewViewsMap[dataSource]?.showProgressBar()
    }


    override fun hideProgressBar(dataSource: CurrencyMarketPreviewDataSource) {
        mMarketPreviewViewsMap[dataSource]?.hideProgressBar()
    }


    override fun showEmptyView(dataSource: CurrencyMarketPreviewDataSource, caption: String) {
        mMarketPreviewViewsMap[dataSource]?.showEmptyView(caption)
    }


    override fun showErrorView(dataSource: CurrencyMarketPreviewDataSource, caption: String) {
        mMarketPreviewViewsMap[dataSource]?.showErrorView(caption)
    }


    override fun hideInfoView(dataSource: CurrencyMarketPreviewDataSource) {
        mMarketPreviewViewsMap[dataSource]?.hideInfoView()
    }


    @Suppress("UNCHECKED_CAST")
    override fun setData(data: Any, shouldBindData: Boolean, dataSource: CurrencyMarketPreviewDataSource) {
        when(dataSource) {

            CurrencyMarketPreviewDataSource.PRICE_CHART -> {
                mRootView.priceChartView.setData((data as List<CandleStick>), shouldBindData)
            }

            CurrencyMarketPreviewDataSource.DEPTH_CHART -> {
                mRootView.depthChartView.setData((data as Orderbook), shouldBindData)
            }

            CurrencyMarketPreviewDataSource.ORDERBOOK -> {
                mRootView.orderbookView.setData((data as Orderbook), shouldBindData)
            }

            CurrencyMarketPreviewDataSource.TRADE_HISTORY -> {
                mRootView.tradeHistoryView.setData((data as List<Trade>), shouldBindData)
            }

            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS -> {
                mRootView.userActiveOrdersView.setData((data as List<Trade>), shouldBindData)
            }

            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> {
                mRootView.userHistoryOrdersView.setData((data as List<Trade>), shouldBindData)
            }

        }
    }


    @Suppress("UNCHECKED_CAST", "NON_EXHAUSTIVE_WHEN")
    override fun updateData(data: Any, dataActionItems: List<Any>, dataSource: CurrencyMarketPreviewDataSource) {
        when(dataSource) {

            CurrencyMarketPreviewDataSource.PRICE_CHART -> {
                mRootView.priceChartView.updateData((data as List<CandleStick>), (dataActionItems as List<DataActionItem<CandleStick>>))
            }

            CurrencyMarketPreviewDataSource.DEPTH_CHART -> {
                mRootView.depthChartView.updateData((data as Orderbook), (dataActionItems as List<DataActionItem<OrderbookOrder>>))
            }

            CurrencyMarketPreviewDataSource.ORDERBOOK -> {
                mRootView.orderbookView.updateData((data as Orderbook), (dataActionItems as List<DataActionItem<OrderbookOrder>>))
            }

            CurrencyMarketPreviewDataSource.TRADE_HISTORY -> {
                mRootView.tradeHistoryView.updateData((data as List<Trade>), (dataActionItems as List<DataActionItem<Trade>>))
            }

        }
    }


    override fun bindData(dataSource: CurrencyMarketPreviewDataSource) {
        mMarketPreviewViewsMap[dataSource]?.bindData()
    }


    override fun clearData(dataSource: CurrencyMarketPreviewDataSource) {
        mMarketPreviewViewsMap[dataSource]?.clearData()
    }


    override fun updateInboxButtonItemCount() {
        mRootView.toolbar.setInboxButtonCountMessage(mPreferenceHandler.getInboxUnreadCount())
    }


    override fun updateChartsVisibilityButtonState(isVisible: Boolean) {
        with(ThemingUtil.CurrencyMarketPreview) {
            val preRightButtonIv: ImageView = mRootView.toolbar.getPreRightButtonIv()

            if(isVisible) {
                visibleChartsButton(preRightButtonIv, getAppTheme())
            } else {
                hiddenChartsButton(preRightButtonIv)
            }
        }
    }


    override fun updateFavoriteButtonState(isFavorite: Boolean) {
        with(ThemingUtil.CurrencyMarketPreview) {
            val rightButtonIv: ImageView = mRootView.toolbar.getRightButtonIv()

            if(isFavorite) {
                favoriteButton(rightButtonIv)
            } else {
                unfavoriteButton(rightButtonIv, getAppTheme())
            }
        }
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun addUserOrderItem(dataSource: CurrencyMarketPreviewDataSource, item: Trade) {
        when(dataSource) {
            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS -> mRootView.userActiveOrdersView.addItem(item)
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> mRootView.userHistoryOrdersView.addItem(item)
        }
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun deleteUserOrderItem(dataSource: CurrencyMarketPreviewDataSource, item: Trade) {
        when(dataSource) {
            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS -> mRootView.userActiveOrdersView.removeItem(item)
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> mRootView.userHistoryOrdersView.removeItem(item)
        }
    }


    override fun navigateToInboxScreen() {
        navigate(R.id.inboxDest)
    }


    override fun navigateToPriceAlertCreationScreen(currencyMarket: CurrencyMarket) {
        navigate(
            destinationId = R.id.priceAlertCreationDest,
            arguments = AlertPriceAddAFragment.newArgs(currencyMarket)
        )
    }


    override fun navigateToOrderbookScreen(currencyMarket: CurrencyMarket) {
        navigate(
            destinationId = R.id.orderbookDest,
            arguments = OrderbookFragment.newArgs(currencyMarket)
        )
    }


    override fun navigateToNextScreen(tradeType: TradeType, currencyMarket: CurrencyMarket,
                                      selectedPrice: Double, selectedAmount: Double) {
        val tradeArguments = TradeFragment.newArgs(
            tradeScreenOpener = TradeScreenOpener.PREVIEW,
            tradeType = tradeType,
            currencyMarket = currencyMarket,
            selectedPrice = selectedPrice,
            selectedAmount = selectedAmount
        )

        if(mSessionManager.isUserSignedIn()) {
            navigate(
                destinationId = R.id.tradeDest,
                arguments = tradeArguments
            )
        } else {
            val dashboardArgs = get<DashboardArgsCreator>().getTradeScreenOpeningArgs(
                tradeArguments
            )

            startActivity(LoginActivity.newInstance(
                context = ctx,
                destinationIntent = DashboardActivity.newInstance(
                    context = ctx,
                    dashboardArgs = dashboardArgs
                )
            ))
        }
    }


    override fun setPriceInfoViewData(currencyMarket: CurrencyMarket, animate: Boolean) {
        val performDataBinding = {
            mRootView.priceInfoView?.setData(
                currencyMarket = currencyMarket,
                formatter = mNumberFormatter,
                currentFiatCurrency = getSettings().fiatCurrency,
                fiatCurrencyPriceHandler = mFiatCurrencyPriceHandler
            )
        }

        if(animate) {
            mRootView.priceInfoView.crossFadeItself {
                performDataBinding()
            }
        } else {
            performDataBinding()
        }
    }


    override fun isScrollViewAtTheBottom(): Boolean = !mRootView.scrollView.canScrollVertically(1)


    override fun containsUserOrder(dataSource: CurrencyMarketPreviewDataSource, trade: Trade): Boolean {
        return when(dataSource) {
            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS -> mRootView.userActiveOrdersView.containsItem(trade)
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> mRootView.userHistoryOrdersView.containsItem(trade)

            else -> false
        }
    }


    override fun isViewSelected(dataSource: CurrencyMarketPreviewDataSource): Boolean {
        val selectedTopTab = mPresenter.selectedTopTab
        val selectedBottomTab = mPresenter.selectedBottomTab

        return when(dataSource) {
            CurrencyMarketPreviewDataSource.PRICE_CHART -> (selectedTopTab == PRICE_CHART)
            CurrencyMarketPreviewDataSource.DEPTH_CHART -> (selectedTopTab == DEPTH_CHART)
            CurrencyMarketPreviewDataSource.ORDERBOOK -> (selectedBottomTab == ORDERBOOK)
            CurrencyMarketPreviewDataSource.TRADE_HISTORY -> (selectedBottomTab == TRADE_HISTORY)
            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS -> (selectedBottomTab == MY_ORDERS)
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> (selectedBottomTab == MY_HISTORY)
        }
    }


    override fun isDataEmpty(dataSource: CurrencyMarketPreviewDataSource): Boolean {
        return (mMarketPreviewViewsMap[dataSource]?.isDataEmpty() ?: true)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.currency_market_preview_fragment_layout


    override fun getDataParameters(dataSource: CurrencyMarketPreviewDataSource): Any {
        return when(dataSource) {
            CurrencyMarketPreviewDataSource.PRICE_CHART -> mRootView.priceChartView.getDataParameters()
            CurrencyMarketPreviewDataSource.DEPTH_CHART -> mRootView.depthChartView.getDataParameters()
            CurrencyMarketPreviewDataSource.ORDERBOOK -> mRootView.orderbookView.getDataParameters()
            CurrencyMarketPreviewDataSource.TRADE_HISTORY -> mRootView.tradeHistoryView.getDataParameters()
            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS -> mRootView.userActiveOrdersView.getDataParameters()
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> mRootView.userHistoryOrdersView.getDataParameters()
        }
    }


    private fun getMarketPreviewView(dataSource: CurrencyMarketPreviewDataSource): View {
        return when(dataSource) {
            CurrencyMarketPreviewDataSource.PRICE_CHART -> mRootView.priceChartView
            CurrencyMarketPreviewDataSource.DEPTH_CHART -> mRootView.depthChartView
            CurrencyMarketPreviewDataSource.ORDERBOOK -> mRootView.orderbookView
            CurrencyMarketPreviewDataSource.TRADE_HISTORY -> mRootView.tradeHistoryView
            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS -> mRootView.userActiveOrdersView
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> mRootView.userHistoryOrdersView
        }
    }


    private val mOnChartTouchListener = object : BaseTradingChartView.OnChartTouchListener {

        override fun onChartPressed() = mPresenter.onChartPressed()

        override fun onChartReleased() = mPresenter.onChartReleased()

    }


    private val mOnPriceChartIntervalChangeListener = object : OnChartIntervalChangeListener {

        override fun onIntervalChanged(oldInterval: PriceChartDataInterval,
                                       newInterval: PriceChartDataInterval) {
            mPresenter.onPriceChartDataIntervalChanged(oldInterval, newInterval)
        }

    }


    private val mOnMainViewShowAnimationCallback = object : OnMainViewShowAnimationListener.Callback<CurrencyMarketPreviewDataSource> {

        override fun onMainViewShowAnimationStarted(dataSource: CurrencyMarketPreviewDataSource) {
            mPresenter.onMainViewShowAnimationStarted(dataSource)
        }


        override fun onMainViewShowAnimationEnded(dataSource: CurrencyMarketPreviewDataSource) {
            mPresenter.onMainViewShowAnimationEnded(dataSource)
        }

    }


}