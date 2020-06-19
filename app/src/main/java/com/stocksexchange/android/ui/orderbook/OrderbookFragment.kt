package com.stocksexchange.android.ui.orderbook

import android.os.Bundle
import androidx.core.view.updateLayoutParams
import com.google.android.material.appbar.AppBarLayout
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.login.newInstance
import com.stocksexchange.android.ui.trade.TradeFragment
import com.stocksexchange.android.ui.trade.newArgs
import com.stocksexchange.android.utils.DashboardArgsCreator
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.orderbook_fragment_layout.view.*
import kotlinx.android.synthetic.main.orderbook_fragment_layout.view.orderbookView
import kotlinx.android.synthetic.main.orderbook_fragment_layout.view.toolbar
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class OrderbookFragment : BaseFragment<OrderbookPresenter>(), OrderbookContract.View {


    companion object {}


    override val mPresenter: OrderbookPresenter by inject { parametersOf(this) }




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.currencyMarket = it.currencyMarket
        }
    }


    override fun init() {
        super.init()

        val currencyMarket = mPresenter.currencyMarket

        initCoordinatorLayout()
        initAppBarLayout(currencyMarket)
        initOrderbookView(currencyMarket)
    }


    private fun initCoordinatorLayout() = with(mRootView.contentContainerCl) {
        ThemingUtil.Orderbook.contentContainer(this, getAppTheme())
    }


    private fun initAppBarLayout(currencyMarket: CurrencyMarket) {
        initToolbar(currencyMarket)
        initOrderbookDetailsView(currencyMarket)
        initOrderbookViewTitle()

        if(mPresenter.isAppBarScrollingEnabled) {
            enableAppBarScrolling()
        } else {
            disableAppBarScrolling()
        }

        ThemingUtil.Orderbook.appBarLayout(mRootView.appBarLayout, getAppTheme())
    }


    private fun initToolbar(currencyMarket: CurrencyMarket) = with(mRootView.toolbar) {
        setTitleText(mStringProvider.getString(
            R.string.orderbook_fragment_toolbar_title_template,
            String.format(
                "%s / %s",
                currencyMarket.baseCurrencySymbol,
                currencyMarket.quoteCurrencySymbol
            )
        ))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }

        ThemingUtil.Orderbook.toolbar(this, getAppTheme())
    }


    private fun initOrderbookDetailsView(currencyMarket: CurrencyMarket) = with(mRootView.orderbookDetailsView) {
        setBaseCurrencySymbol(currencyMarket.baseCurrencySymbol)

        ThemingUtil.Orderbook.orderbookDetailsView(this, getAppTheme())
    }


    private fun initOrderbookViewTitle() = with(mRootView.orderbookViewTitleTv) {
        text = getStr(R.string.orders)

        ThemingUtil.Orderbook.orderbookViewTitle(this, getAppTheme())
    }


    private fun initOrderbookView(currencyMarket: CurrencyMarket) = with(mRootView.orderbookView) {
        val infoViewIconProvider: InfoViewIconProvider = get()

        setHighlightingEnabled(getSettings().isOrderbookHighlightingEnabled)
        setCurrencyPairId(currencyMarket.pairId)
        setInfoViewIcon(infoViewIconProvider.getOrderbookViewIcon())
        setOnItemClickListener { _, orderbookOrderItem, _ ->
            mPresenter.onOrderbookOrderItemClicked(orderbookOrderItem.itemModel)
        }

        ThemingUtil.Orderbook.orderbookView(this, getAppTheme())
    }


    override fun showAppBar(animate: Boolean) {
        mRootView.appBarLayout.setExpanded(true, animate)
    }


    override fun enableAppBarScrolling() {
        mRootView.toolbarContainerRl.updateLayoutParams<AppBarLayout.LayoutParams> {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
        }
    }


    override fun disableAppBarScrolling() {
        mRootView.toolbarContainerRl.updateLayoutParams<AppBarLayout.LayoutParams> {
            scrollFlags = 0
        }
    }


    override fun showProgressBar() {
        mRootView.orderbookDetailsView.showProgressBar()
        mRootView.orderbookView.showProgressBar()
    }


    override fun hideProgressBar() {
        mRootView.orderbookDetailsView.hideProgressBar()
        mRootView.orderbookView.hideProgressBar()
    }


    override fun showMainView() {
        mRootView.orderbookDetailsView.showMainView(true)
        mRootView.orderbookView.showMainView(true)
    }


    override fun hideMainView() {
        mRootView.orderbookDetailsView.hideMainView()
        mRootView.orderbookView.hideMainView()
    }


    override fun showEmptyView(orderbookDetailsCaption: String, orderbookViewCaption: String) {
        mRootView.orderbookDetailsView.showEmptyView(orderbookDetailsCaption)
        mRootView.orderbookView.showEmptyView(orderbookViewCaption)
    }


    override fun showErrorView(orderbookDetailsCaption: String, orderbookViewCaption: String) {
        mRootView.orderbookDetailsView.showErrorView(orderbookDetailsCaption)
        mRootView.orderbookView.showErrorView(orderbookViewCaption)
    }


    override fun hideInfoView() {
        mRootView.orderbookDetailsView.hideInfoView()
        mRootView.orderbookView.hideInfoView()
    }


    override fun setData(info: OrderbookInfo, orderbook: Orderbook, shouldBindData: Boolean) {
        mRootView.orderbookDetailsView.setData(info, shouldBindData)
        mRootView.orderbookView.setData(orderbook, shouldBindData)
    }


    override fun updateData(info: OrderbookInfo, orderbook: Orderbook,
                            dataActionItems: List<DataActionItem<OrderbookOrder>>) {
        mRootView.orderbookDetailsView.updateData(info)
        mRootView.orderbookView.updateData(orderbook, dataActionItems)
    }


    override fun bindData() {
        mRootView.orderbookDetailsView.bindData()
        mRootView.orderbookView.bindData()
    }


    override fun scrollOrderbookViewToTop() {
        mRootView.orderbookView.scrollToTop()
    }


    override fun getSelectedAmount(orderbookOrderData: OrderbookOrderData): Double {
        return mRootView.orderbookView.getSelectedAmount(orderbookOrderData)
    }


    override fun navigateToNextScreen(tradeType: TradeType, currencyMarket: CurrencyMarket,
                                      selectedPrice: Double, selectedAmount: Double) {
        val tradeArguments = TradeFragment.newArgs(
            tradeScreenOpener = TradeScreenOpener.ORDERBOOK,
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


    override fun canReceiveRealTimeDataUpdateEvent(): Boolean = true


    override fun isOrderbookEmpty(): Boolean {
        return mRootView.orderbookView.isDataEmpty()
    }


    override fun getOrderbookParameters(): OrderbookParameters {
        return mRootView.orderbookView.getDataParameters()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.orderbook_fragment_layout


}