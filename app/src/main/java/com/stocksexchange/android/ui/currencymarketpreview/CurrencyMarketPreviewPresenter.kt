package com.stocksexchange.android.ui.currencymarketpreview

import com.stocksexchange.android.R
import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.android.events.*
import com.stocksexchange.android.mappings.mapToTrade
import com.stocksexchange.android.model.*
import com.stocksexchange.android.model.CurrencyMarketPreviewDataSource.*
import com.stocksexchange.android.ui.base.multipledataloading.BaseMultipleDataLoadingPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.TipShowingTracker
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.helpers.handleCurrencyMarketEvent
import com.stocksexchange.android.utils.helpers.handlePerformedCurrencyMarketActionsEvent
import com.stocksexchange.android.utils.helpers.handlePerformedOrderActionsEvent
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CurrencyMarketPreviewPresenter(
    view: CurrencyMarketPreviewContract.View,
    model: CurrencyMarketPreviewModel,
    private val sessionManager: SessionManager,
    private val firebaseEventLogger: FirebaseEventLogger,
    private val tipShowingTracker: TipShowingTracker
) : BaseMultipleDataLoadingPresenter<
    CurrencyMarketPreviewContract.View,
    CurrencyMarketPreviewModel,
    CurrencyMarketPreviewDataSource
    >(view, model),
    CurrencyMarketPreviewContract.ActionListener, CurrencyMarketPreviewModel.ActionListener {


    var currencyMarket: CurrencyMarket = CurrencyMarket.STUB_CURRENCY_MARKET

    var selectedTopTab: CurrencyMarketPreviewTopTab = CurrencyMarketPreviewTopTab.PRICE_CHART
    var selectedBottomTab: CurrencyMarketPreviewBottomTab = CurrencyMarketPreviewBottomTab.ORDERBOOK

    private var mPerformedActions: PerformedCurrencyMarketActions = PerformedCurrencyMarketActions()




    init {
        model.setActionListener(this)
    }


    override fun startListeningToSocketEvents() = with(mSocketConnection) {
        val subscriberKey = this@CurrencyMarketPreviewPresenter.toString()
        val pairId = currencyMarket.pairId.toString()
        val priceChartParams = (mView.getDataParameters(PRICE_CHART) as PriceChartDataParameters)
        val intervalName = priceChartParams.interval.intervalName
        val userId = sessionManager.getProfileInfo()?.id.toString()

        startListeningToMarketPreviewUpdates(
            subscriberKey = subscriberKey,
            priceChartIntervalName = intervalName,
            currencyPairId = pairId
        )

        if(sessionManager.isUserSignedIn()) {
            startListeningToActiveOrdersUpdates(
                subscriberKey = subscriberKey,
                userId = userId,
                currencyPairId = pairId
            )
        }
    }


    override fun onRefreshData() {
        super.onRefreshData()

        mIsDataLoadingPerformedMap[PRICE_CHART] = false
        mIsDataLoadingPerformedMap[DEPTH_CHART] = false
        mIsDataLoadingPerformedMap[ORDERBOOK] = false
        mIsDataLoadingPerformedMap[TRADE_HISTORY] = false
        mIsDataLoadingPerformedMap[USER_ACTIVE_ORDERS] = false
        mIsDataLoadingPerformedMap[USER_HISTORY_ORDERS] == false

        loadData(PRICE_CHART)
        loadData(DEPTH_CHART)
        loadData(ORDERBOOK)
        loadData(TRADE_HISTORY)
        loadData(USER_ACTIVE_ORDERS)
        loadData(USER_HISTORY_ORDERS)

        mView.hideRefreshProgressBar()
    }


    override fun stopListeningToSocketEvents() = with(mSocketConnection) {
        val subscriberKey = this@CurrencyMarketPreviewPresenter.toString()
        val pairId = currencyMarket.pairId.toString()
        val userId = sessionManager.getProfileInfo()?.id.toString()

        stopListeningToMarketPreviewUpdates(
            subscriberKey = subscriberKey,
            currencyPairId = pairId
        )

        if(sessionManager.isUserSignedIn()) {
            stopListeningToActiveOrdersUpdates(
                subscriberKey = subscriberKey,
                userId = userId,
                currencyPairId = pairId
            )
        }
    }


    override fun start() {
        super.start()

        mView.updateInboxButtonItemCount()
    }


    override fun setDataLoadingPerformed(isDataLoadingPerformed: Boolean,
                                         dataSource: CurrencyMarketPreviewDataSource) {
        super.setDataLoadingPerformed(isDataLoadingPerformed, dataSource)

        if(dataSource == ORDERBOOK) {
            mIsDataLoadingPerformedMap[DEPTH_CHART] = isDataLoadingPerformed
        } else if(dataSource == DEPTH_CHART) {
            mIsDataLoadingPerformedMap[ORDERBOOK] = isDataLoadingPerformed
        }
    }


    override fun isDataLoadingPerformed(dataSource: CurrencyMarketPreviewDataSource): Boolean {
        return if((dataSource == DEPTH_CHART) || (dataSource == ORDERBOOK)) {
            (mIsDataLoadingPerformedMap[DEPTH_CHART] == true) || (mIsDataLoadingPerformedMap[ORDERBOOK] == true)
        } else {
            super.isDataLoadingPerformed(dataSource)
        }
    }


    override fun showEmptyView(dataSource: CurrencyMarketPreviewDataSource) {
        mView.showEmptyView(
            dataSource = dataSource,
            caption = when(dataSource) {
                PRICE_CHART -> mStringProvider.getPriceChartViewEmptyCaption()
                DEPTH_CHART -> mStringProvider.getDepthChartViewEmptyCaption()
                ORDERBOOK -> mStringProvider.getOrderbookViewEmptyCaption()
                TRADE_HISTORY -> mStringProvider.getTradeHistoryViewEmptyCaption()
                USER_ACTIVE_ORDERS -> mStringProvider.getUserActiveOrdersViewEmptyCaption()
                USER_HISTORY_ORDERS -> mStringProvider.getUserHistoryOrdersViewEmptyCaption()
            }
        )
    }


    override fun showErrorView(dataSource: CurrencyMarketPreviewDataSource, exception: Throwable) {
        mView.showErrorView(
            dataSource = dataSource,
            caption = mStringProvider.getErrorMessage(exception)
        )
    }


    override fun onMainViewShowAnimationEnded(dataSource: CurrencyMarketPreviewDataSource) {
        if((dataSource == USER_ACTIVE_ORDERS) &&
            !tipShowingTracker.wasPreviewOrderCancellationTipShown() &&
            (mView.isScrollViewAtTheBottom() || !sessionManager.getSettings().areMarketPreviewChartsVisible)) {
            tipShowingTracker.setPreviewOrderCancellationTipShown()

            mView.showOrderCancellationTip()
        }

        super.onMainViewShowAnimationEnded(dataSource)
    }


    override fun isDataRealTimeDependant(): Boolean = true


    override fun getDataParameters(dataSource: CurrencyMarketPreviewDataSource): Any {
        return when(dataSource) {
            USER_ACTIVE_ORDERS,
            USER_HISTORY_ORDERS -> {
                val tradeHistoryParams = (mView.getDataParameters(dataSource) as TradeHistoryParameters)

                if(dataSource == USER_ACTIVE_ORDERS) {
                    OrderParameters.getSpecificActiveOrdersParams(tradeHistoryParams.currencyPairId)
                } else {
                    OrderParameters
                        .getSpecificCompletedOrdersParams(tradeHistoryParams.currencyPairId)
                        .copy(limit = tradeHistoryParams.count)
                }
            }

            else -> super.getDataParameters(dataSource)
        }
    }


    override fun getDataSourcesArray(): Array<CurrencyMarketPreviewDataSource> = values()


    private fun getDataSourceForTopTab(tab: CurrencyMarketPreviewTopTab): CurrencyMarketPreviewDataSource {
        return when(tab) {
            CurrencyMarketPreviewTopTab.PRICE_CHART -> PRICE_CHART
            CurrencyMarketPreviewTopTab.DEPTH_CHART -> DEPTH_CHART
        }
    }


    private fun getDataSourcesForTopTabs(
        firstTab: CurrencyMarketPreviewTopTab,
        secondTab: CurrencyMarketPreviewTopTab
    ) : Pair<CurrencyMarketPreviewDataSource, CurrencyMarketPreviewDataSource> {
        return (getDataSourceForTopTab(firstTab) to getDataSourceForTopTab(secondTab))
    }


    private fun getDataSourceForBottomTab(tab: CurrencyMarketPreviewBottomTab): CurrencyMarketPreviewDataSource {
        return when(tab) {
            CurrencyMarketPreviewBottomTab.ORDERBOOK -> ORDERBOOK
            CurrencyMarketPreviewBottomTab.TRADE_HISTORY -> TRADE_HISTORY
            CurrencyMarketPreviewBottomTab.MY_ORDERS -> USER_ACTIVE_ORDERS
            CurrencyMarketPreviewBottomTab.MY_HISTORY -> USER_HISTORY_ORDERS
        }
    }


    private fun getDataSourcesForBottomTabs(
        firstTab: CurrencyMarketPreviewBottomTab,
        secondTab: CurrencyMarketPreviewBottomTab
    ) : Pair<CurrencyMarketPreviewDataSource, CurrencyMarketPreviewDataSource> {
        return (getDataSourceForBottomTab(firstTab) to getDataSourceForBottomTab(secondTab))
    }


    override fun onPreRightButtonClicked() {
        mModel.toggleChartsVisibilitySettingOption(sessionManager.getSettings())
    }


    override fun onChartsVisibilitySettingEnabled(settings: Settings) {
        firebaseEventLogger.onMarketPreviewChartsBecameVisible()

        mView.updateChartsVisibilityButtonState(true)
        sessionManager.setSettings(settings)
        mView.showChartsContainer()
    }


    override fun onChartsVisibilitySettingDisabled(settings: Settings) {
        firebaseEventLogger.onMarketPreviewChartsBecameInvisible()

        mView.updateChartsVisibilityButtonState(false)
        sessionManager.setSettings(settings)
        mView.hideChartsContainer()
    }


    override fun onRightButtonClicked() {
        mModel.toggleFavoriteState(currencyMarket)
    }


    override fun onCurrencyMarketFavorited(currencyMarket: CurrencyMarket) {
        firebaseEventLogger.onMarketPreviewCurrencyMarketFavorited(currencyMarket.pairName)

        mView.updateFavoriteButtonState(true)

        handleCurrencyMarketEvent(
            event = CurrencyMarketEvent.favorite(currencyMarket, this),
            performedActions = mPerformedActions
        )
    }


    override fun onCurrencyMarketUnfavorited(currencyMarket: CurrencyMarket) {
        mView.updateFavoriteButtonState(false)

        handleCurrencyMarketEvent(
            event = CurrencyMarketEvent.unfavorite(currencyMarket, this),
            performedActions = mPerformedActions
        )
    }


    override fun onTopTabSelected(position: Int) {
        when(position) {
            CurrencyMarketPreviewTopTab.PRICE_CHART.ordinal -> onPriceChartSelected()
            CurrencyMarketPreviewTopTab.DEPTH_CHART.ordinal -> onDepthChartSelected()
        }
    }


    override fun onChartPressed() {
        mView.disableScrollViewScrolling()
    }


    override fun onChartReleased() {
        mView.enableScrollViewScrolling()
    }


    override fun onPriceChartDataIntervalChanged(oldInterval: PriceChartDataInterval,
                                                 newInterval: PriceChartDataInterval) {
        mMainViewsToShowMap[PRICE_CHART] = false

        mView.clearData(PRICE_CHART)
        mModel.cancelDataLoadingAndWait(PRICE_CHART) {
            loadData(PRICE_CHART)
        }

        mSocketConnection.startListeningToPriceChartDataUpdates(
            subscriberKey = toString(),
            intervalName = newInterval.intervalName,
            currencyPairId = currencyMarket.pairId.toString()
        )
    }


    override fun onDepthChartTabSelected(tab: DepthChartTab) {
        mMainViewsToShowMap[DEPTH_CHART] = false

        mView.clearData(DEPTH_CHART)
        mModel.cancelDataLoadingAndWait(DEPTH_CHART) {
            loadData(DEPTH_CHART)
        }
    }


    private fun onPriceChartSelected() {
        val newSelectedTab = CurrencyMarketPreviewTopTab.PRICE_CHART
        val dataSources = getDataSourcesForTopTabs(selectedTopTab, newSelectedTab)

        selectedTopTab = newSelectedTab
        firebaseEventLogger.onMarketPreviewPriceChartSelected()

        onViewSelected(
            oldDataSource = dataSources.first,
            newDataSource = dataSources.second
        )
    }


    private fun onDepthChartSelected() {
        val newSelectedTab = CurrencyMarketPreviewTopTab.DEPTH_CHART
        val dataSources = getDataSourcesForTopTabs(selectedTopTab, newSelectedTab)

        selectedTopTab = newSelectedTab
        firebaseEventLogger.onMarketPreviewDepthChartSelected()

        onViewSelected(
            oldDataSource = dataSources.first,
            newDataSource = dataSources.second
        )
    }


    override fun onBottomTabSelected(position: Int) {
        when(position) {
            CurrencyMarketPreviewBottomTab.ORDERBOOK.ordinal -> onOrderbookSelected()
            CurrencyMarketPreviewBottomTab.TRADE_HISTORY.ordinal -> onTradeHistorySelected()
            CurrencyMarketPreviewBottomTab.MY_ORDERS.ordinal -> onMyOrdersSelected()
            CurrencyMarketPreviewBottomTab.MY_HISTORY.ordinal -> onMyHistorySelected()
        }
    }


    private fun onOrderbookSelected() {
        val newSelectedTab = CurrencyMarketPreviewBottomTab.ORDERBOOK
        val dataSources = getDataSourcesForBottomTabs(selectedBottomTab, newSelectedTab)

        selectedBottomTab = newSelectedTab
        firebaseEventLogger.onMarketPreviewOrderbookSelected()

        onViewSelected(
            oldDataSource = dataSources.first,
            newDataSource = dataSources.second
        )
    }


    private fun onTradeHistorySelected() {
        val newSelectedTab = CurrencyMarketPreviewBottomTab.TRADE_HISTORY
        val dataSources = getDataSourcesForBottomTabs(selectedBottomTab, newSelectedTab)

        selectedBottomTab = newSelectedTab
        firebaseEventLogger.onMarketPreviewTradeHistorySelected()

        onViewSelected(
            oldDataSource = dataSources.first,
            newDataSource = dataSources.second
        )
    }


    private fun onMyOrdersSelected() {
        val newSelectedTab = CurrencyMarketPreviewBottomTab.MY_ORDERS
        val dataSources = getDataSourcesForBottomTabs(selectedBottomTab, newSelectedTab)

        selectedBottomTab = newSelectedTab
        firebaseEventLogger.onMarketPreviewMyOrdersSelected()

        onViewSelected(
            oldDataSource = dataSources.first,
            newDataSource = dataSources.second
        )
    }


    private fun onMyHistorySelected() {
        val newSelectedTab = CurrencyMarketPreviewBottomTab.MY_HISTORY
        val dataSources = getDataSourcesForBottomTabs(selectedBottomTab, newSelectedTab)

        selectedBottomTab = newSelectedTab
        firebaseEventLogger.onMarketPreviewMyHistorySelected()

        onViewSelected(
            oldDataSource = dataSources.first,
            newDataSource = dataSources.second
        )
    }


    private fun onViewSelected(oldDataSource: CurrencyMarketPreviewDataSource,
                               newDataSource: CurrencyMarketPreviewDataSource) {
        mView.hideView(oldDataSource)

        mView.hideMainView(newDataSource)
        mView.showProgressBar(newDataSource)
        mView.showView(newDataSource)

        mModel.cancelDataLoadingAndWait(oldDataSource) {
            if(mView.isDataEmpty(newDataSource) || !isDataLoadingPerformed(newDataSource)) {
                loadData(newDataSource)
            } else {
                mView.hideProgressBar(newDataSource)
                mView.showMainView(newDataSource, true)
            }
        }
    }


    override fun onOrderbookHeaderMoreButtonClicked() {
        mView.navigateToOrderbookScreen(currencyMarket)
    }


    override fun onOrderbookOrderItemClicked(orderbookOrderData: OrderbookOrderData) {
        if(orderbookOrderData.order.isStub) {
            return
        }

        navigateToNextScreen(
            tradeType = when(orderbookOrderData.type) {
                OrderbookOrderType.BID -> TradeType.SELL
                OrderbookOrderType.ASK -> TradeType.BUY
            },
            selectedPrice = orderbookOrderData.order.price,
            selectedAmount = mView.getSelectedAmount(orderbookOrderData)
        )
    }


    override fun onActiveOrderCancelButtonClicked(tradeData: TradeData) {
        val title = mStringProvider.getString(
            R.string.orders_fragment_order_cancellation_dialog_title
        )
        val content = mStringProvider.getString(
            R.string.orders_fragment_order_cancellation_dialog_content
        )

        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = title,
            content = content,
            negativeBtnText = mStringProvider.getString(R.string.no),
            positiveBtnText = mStringProvider.getString(R.string.yes),
            positiveBtnClick = {
                onOrderCancellationConfirmed(tradeData)
            }
        ))
    }


    private fun onOrderCancellationConfirmed(tradeData: TradeData) {
        mView.showUserActiveOrdersViewItemProgressBar(tradeData)

        mModel.performOrderRetrievalRequest(
            orderId = tradeData.trade.id,
            metadata = tradeData
        )
    }


    override fun onTradeButtonClicked(tradeType: TradeType) {
        if(!currencyMarket.isActive) {
            mView.showToast(mStringProvider.getString(R.string.error_market_disabled))
            return
        }

        navigateToNextScreen(tradeType = tradeType)
    }


    private fun navigateToNextScreen(tradeType: TradeType,
                                     selectedPrice: Double = 0.0, selectedAmount: Double = 0.0) {
        mView.navigateToNextScreen(
            tradeType = tradeType,
            currencyMarket = currencyMarket,
            selectedPrice = selectedPrice,
            selectedAmount = selectedAmount
        )
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            CurrencyMarketPreviewModel.REQUEST_TYPE_ORDER_RETRIEVAL -> {
                onOrderRetrieved(
                    order = (response as Order),
                    tradeData = (metadata as TradeData)
                )
            }

            CurrencyMarketPreviewModel.REQUEST_TYPE_ORDER_CANCELLATION -> {
                val metadataPair = (metadata as Pair<Order, TradeData>)

                onOrderCancelled(
                    response = (response as OrdersCancellationResponse),
                    originalOrder = metadataPair.first,
                    tradeData = metadataPair.second
                )
            }

        }
    }


    private fun onOrderRetrieved(order: Order, tradeData: TradeData) {
        mModel.performOrderCancellationRequest(
            order = order,
            metadata = (order to tradeData)
        )
    }


    private fun onOrderCancelled(response: OrdersCancellationResponse,
                                 originalOrder: Order,
                                 tradeData: TradeData) {
        if(response.isEmpty) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        when {
            response.wasPutIntoProcessingQueue(originalOrder.id) -> {
                mView.hideUserActiveOrdersViewItemProgressBar(tradeData)

                updateUserOrdersViewItemCount(USER_ACTIVE_ORDERS) {
                    mView.deleteUserOrderItem(USER_ACTIVE_ORDERS, tradeData.trade)
                }

                mView.showToast(mStringProvider.getString(R.string.order_cancelled))

                val updatedOrder = Order.cancelActiveOrder(originalOrder)
                val orderData = OrderData(
                    order = updatedOrder,
                    currencyMarket = currencyMarket
                )

                EventBus.getDefault().post(OrderEvent.updateStatus(orderData, this))
            }

            response.wasNotPutIntoProcessingQueue(originalOrder.id) -> {
                showOrderCancellationErrorDialog()
            }

            else -> {
                mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            }
        }
    }


    private fun showOrderCancellationErrorDialog() {
        val title = mStringProvider.getString(
            R.string.orders_fragment_order_cancellation_dialog_title
        )
        val content = mStringProvider.getString(
            R.string.orders_fragment_order_cancellation_error_dialog_content
        )

        showInfoDialog(
            title = title,
            content = content
        )
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when(requestType) {

            CurrencyMarketPreviewModel.REQUEST_TYPE_ORDER_RETRIEVAL -> {
                onOrderRetrievalFailed(metadata as TradeData)
            }

            CurrencyMarketPreviewModel.REQUEST_TYPE_ORDER_CANCELLATION -> {
                onOrderCancellationFailed(
                    exception = exception,
                    tradeData = (metadata as Pair<Order, TradeData>).second
                )
            }

        }
    }


    private fun onOrderRetrievalFailed(tradeData: TradeData) {
        mView.hideUserActiveOrdersViewItemProgressBar(tradeData)
        mView.showToast(mStringProvider.getSomethingWentWrongMessage())
    }


    private fun onOrderCancellationFailed(exception: Throwable, tradeData: TradeData) {
        mView.hideUserActiveOrdersViewItemProgressBar(tradeData)
        mView.showToast(mStringProvider.getErrorMessage(exception))
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: InboxCountItemChangeEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        mView.updateInboxButtonItemCount()

        event.consume()
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: CurrencyMarketEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        val attachment = event.attachment

        when(event.action) {
            CurrencyMarketEvent.Action.FAVORITED -> {
                mView.updateFavoriteButtonState(true)
                mPerformedActions.addFavoriteCurrencyMarket(attachment)
            }

            CurrencyMarketEvent.Action.UNFAVORITED -> {
                mView.updateFavoriteButtonState(false)
                mPerformedActions.addUnfavoriteCurrencyMarket(attachment)
            }

            CurrencyMarketEvent.Action.UPDATED -> {
                if((currencyMarket.pairId == attachment.pairId) &&
                    (currencyMarket.lastPrice != attachment.lastPrice)) {
                    currencyMarket = attachment

                    mView.setPriceInfoViewData(attachment, true)
                }

                mPerformedActions.addUpdatedCurrencyMarket(attachment)
            }
        }

        event.consume()
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: OrderEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        val orderData = event.attachment
        val order = orderData.order
        val trade = order.mapToTrade()

        val addOrderIfNeededFunc: ((CurrencyMarketPreviewDataSource) -> Unit) = {
            if(isDataLoadingPerformed(it) && !mView.containsUserOrder(it, trade)) {
                updateUserOrdersViewItemCount(it) {
                    mView.addUserOrderItem(it, trade)
                }
            }
        }
        val deleteOrderIfNeededFunc: ((CurrencyMarketPreviewDataSource) -> Unit) = {
            if(!mView.isDataEmpty(it) && mView.containsUserOrder(it, trade)) {
                updateUserOrdersViewItemCount(it) {
                    mView.deleteUserOrderItem(it, trade)
                }
            }
        }

        when(event.action) {

            OrderEvent.Action.STATUS_UPDATED -> when(order.status) {
                OrderStatus.PENDING -> addOrderIfNeededFunc(USER_ACTIVE_ORDERS)

                OrderStatus.FINISHED, OrderStatus.PARTIAL -> {
                    deleteOrderIfNeededFunc(USER_ACTIVE_ORDERS)
                    addOrderIfNeededFunc(USER_HISTORY_ORDERS)
                }

                OrderStatus.CANCELLED -> deleteOrderIfNeededFunc(USER_ACTIVE_ORDERS)
            }

            OrderEvent.Action.FILLED_AMOUNT_UPDATED -> addOrderIfNeededFunc(USER_ACTIVE_ORDERS)

        }
    }


    private fun updateUserOrdersViewItemCount(dataSource: CurrencyMarketPreviewDataSource,
                                              task: (() -> Unit)) {
        val wasDataEmpty = mView.isDataEmpty(dataSource)

        task()

        val isDataEmpty = mView.isDataEmpty(dataSource)

        if(wasDataEmpty && !isDataEmpty) {
            mView.hideInfoView(dataSource)
            mView.showMainView(dataSource, true)
        } else if(isDataEmpty) {
            mView.hideMainView(dataSource)
            showEmptyView(dataSource)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PriceChartDataUpdateEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        updateData(event.attachment, event.dataActionItems, PRICE_CHART)

        event.consume()
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: OrderbookDataUpdateEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        updateData(event.attachment, event.dataActionItems, DEPTH_CHART)
        updateData(event.attachment, event.dataActionItems, ORDERBOOK)

        event.consume()
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: TradeHistoryDataUpdateEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        updateData(event.attachment, event.dataActionItems, TRADE_HISTORY)

        event.consume()
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: OrderbookDataReloadEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        for(dataSource in listOf(DEPTH_CHART, ORDERBOOK)) {
            if(mView.isViewSelected(dataSource)) {
                loadData(dataSource)
            }
        }

        event.consume()
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedCurrencyMarketActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        // Handling the event
        handlePerformedCurrencyMarketActionsEvent(event)

        // Merging with this instance's actions in order to pass it
        // backwards in the stack
        mPerformedActions.merge(event.attachment)

        // Consuming the event
        event.consume()
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedOrderActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        handlePerformedOrderActionsEvent(event)

        event.consume()
    }


    private fun updateData(newData: Any, dataActionItems: List<Any>,
                           dataSource: CurrencyMarketPreviewDataSource) {
        if(!isDataLoadingPerformed(dataSource) ||
            mModel.isDataLoading(dataSource) ||
            isMainViewCurrentlyShowing()) {
            return
        }

        val wasDataEmpty = mView.isDataEmpty(dataSource)

        mView.updateData(newData, dataActionItems, dataSource)

        val isDataEmpty = mView.isDataEmpty(dataSource)

        if(wasDataEmpty && !isDataEmpty) {
            mView.hideInfoView(dataSource)
            mView.showMainView(dataSource, true)
        } else if(isDataEmpty) {
            mView.hideMainView(dataSource)
            showEmptyView(dataSource)
        }
    }


    override fun onNavigateUpPressed(): Boolean {
        if(!mPerformedActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedCurrencyMarketActionsEvent.init(
                mPerformedActions, this
            ))
        }

        return super.onNavigateUpPressed()
    }


    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            selectedTopTab = it.selectedTopTab
            selectedBottomTab = it.selectedBottomTab
            currencyMarket = it.currencyMarket
            mPerformedActions = it.performedActions
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            selectedTopTab = selectedTopTab,
            selectedBottomTab = selectedBottomTab,
            currencyMarket = currencyMarket,
            performedActions = mPerformedActions
        ))
    }


    override fun toString(): String {
        return "${super.toString()}_${currencyMarket.pairName}"
    }



    override fun onInboxButtonClicked() {
        if(!sessionManager.isUserSignedIn()) {
            return
        }

        mView.navigateToInboxScreen()
    }


    override fun onAlertPriceButtonClicked() {
        if(!sessionManager.isUserSignedIn()) {
            return
        }

        if (!sessionManager.getSettings().isNotificationEnabled) {
            showAllowNotificationDialog()
        } else {
            mView.navigateToPriceAlertCreationScreen(currencyMarket)
        }
    }


    private fun showAllowNotificationDialog() {
        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = mStringProvider.getString(R.string.notification_title),
            content = mStringProvider.getString(R.string.notification_body),
            positiveBtnText = mStringProvider.getString(R.string.notification_allow),
            negativeBtnText = mStringProvider.getString(R.string.action_cancel),
            positiveBtnClick = {
                turnOnNotificationsAndNavigateToPriceAlerts()
            }
        ))
    }


    private fun turnOnNotificationsAndNavigateToPriceAlerts() {
        mModel.updateNotificationStatus(NotificationStatus.ENABLE)

        val newSettings = sessionManager.getSettings().copy(
            isNotificationEnabled = true
        )

        mModel.updateSettings(newSettings) {
            sessionManager.setSettings(newSettings)

            mView.navigateToPriceAlertCreationScreen(currencyMarket)
        }
    }


}