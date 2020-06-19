package com.stocksexchange.android.ui.orderbook

import com.stocksexchange.android.events.*
import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.OrderbookOrder
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.helpers.handleOrderEvent
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.core.exceptions.NotFoundException
import com.stocksexchange.core.model.DataLoadingState
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrderbookPresenter(
    view: OrderbookContract.View,
    model: OrderbookModel,
    private val sessionManager: SessionManager
) : BasePresenter<OrderbookContract.View, OrderbookModel>(view, model),
    OrderbookContract.ActionListener, OrderbookModel.ActionListener {


    var currencyMarket: CurrencyMarket = CurrencyMarket.STUB_CURRENCY_MARKET

    var isAppBarScrollingEnabled: Boolean = false

    private var mIsRealTimeDataUpdateEventReceived = false
    private var mIsDataLoadingPerformed = false

    private var mPerformedOrderActions: PerformedOrderActions = PerformedOrderActions()




    init {
        model.setActionListener(this)
    }


    override fun startListeningToSocketEvents() = with(mSocketConnection) {
        val subscriberKey = this@OrderbookPresenter.toString()
        val pairId = currencyMarket.pairId.toString()
        val userId = sessionManager.getProfileInfo()?.id.toString()

        startListeningToOrderbookOrdersUpdates(
            subscriberKey = subscriberKey,
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


    override fun stopListeningToSocketEvents() = with(mSocketConnection) {
        val subscriberKey = this@OrderbookPresenter.toString()
        val pairId = currencyMarket.pairId.toString()
        val userId = sessionManager.getProfileInfo()?.id.toString()

        stopListeningToOrderbookOrdersUpdates(
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

        if(mView.isOrderbookEmpty()) {
            loadData(DataType.OLD_DATA)
        }
    }


    private fun loadData(dataType: DataType) {
        if(mModel.isDataLoading) {
            return
        }

        mView.hideMainView()
        mView.hideInfoView()

        mModel.getData(
            params = mView.getOrderbookParameters(),
            dataType = dataType,
            dataLoadingTrigger = DataLoadingTrigger.OTHER,
            wasInitiatedByTheUser = false
        )
    }


    private fun showEmptyView() {
        mView.showEmptyView(
            orderbookDetailsCaption = mStringProvider.getOrderbookDetailsViewEmptyCaption(),
            orderbookViewCaption = mStringProvider.getOrderbookViewEmptyCaption()
        )
    }


    private fun showErrorView(exception: Throwable) {
        val errorCaption = mStringProvider.getErrorMessage(exception)

        mView.showErrorView(
            orderbookDetailsCaption = errorCaption,
            orderbookViewCaption = errorCaption
        )
    }


    private fun showInfoView() {
        if(!mModel.hasLastDataFetchingException()) {
            showEmptyView()
            return
        }

        val lastDataFetchingException = mModel.lastDataFetchingException!!

        if(lastDataFetchingException is NotFoundException) {
            showEmptyView()
        } else {
            showErrorView(lastDataFetchingException)
        }
    }


    override fun onDataLoadingStarted() {
        disableAppBarScrolling()
        mView.showProgressBar()
    }


    override fun onDataLoadingEnded() {
        updateAppBarScrollingState()
        mView.hideProgressBar()

        if(mView.isOrderbookEmpty()) {
            showInfoView()
        } else {
            mView.hideInfoView()
        }
    }


    override fun onDataLoadingStateChanged(dataLoadingState: DataLoadingState) {
        if(dataLoadingState == DataLoadingState.IDLE) {
            if(!mView.isOrderbookEmpty() && !mModel.isDataLoadingCancelled) {
                mView.bindData()
                mView.showMainView()
            }
        }
    }


    override fun onDataLoadingSucceeded(data: Orderbook) {
        mIsDataLoadingPerformed = true

        mView.setData(
            info = OrderbookInfo.newInstance(data),
            orderbook = data,
            shouldBindData = false
        )
    }


    override fun onDataLoadingFailed(error: Throwable) {
        mIsDataLoadingPerformed = true

        showInfoView()
    }


    override fun onOrderbookOrderItemClicked(orderbookOrderData: OrderbookOrderData) {
        if(orderbookOrderData.order.isStub) {
            return
        }

        mView.navigateToNextScreen(
            tradeType = when(orderbookOrderData.type) {
                OrderbookOrderType.BID -> TradeType.SELL
                OrderbookOrderType.ASK -> TradeType.BUY
            },
            currencyMarket = currencyMarket,
            selectedPrice = orderbookOrderData.order.price,
            selectedAmount = mView.getSelectedAmount(orderbookOrderData)
        )
    }


    private fun updateAppBarScrollingState() {
        if(mView.isOrderbookEmpty()) {
            disableAppBarScrolling()
        } else {
            enableAppBarScrolling()
        }
    }


    private fun enableAppBarScrolling() {
        if(!isAppBarScrollingEnabled) {
            isAppBarScrollingEnabled = true

            mView.enableAppBarScrolling()
        }
    }


    private fun disableAppBarScrolling() {
        if(isAppBarScrollingEnabled) {
            isAppBarScrollingEnabled = false

            mView.disableAppBarScrolling()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: RealTimeDataUpdateEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed(this)) {
            return
        }

        mIsRealTimeDataUpdateEventReceived = true

        performRealTimeDataUpdate()

        event.consume(this)
    }


    private fun performRealTimeDataUpdate() {
        mIsDataLoadingPerformed = false

        mView.showAppBar(false)
        mView.scrollOrderbookViewToTop()

        loadData(DataType.NEW_DATA)
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: OrderbookDataUpdateEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        updateData(event.attachment, event.dataActionItems)

        event.consume()
    }


    private fun updateData(orderbook: Orderbook,
                           dataActionItems: List<DataActionItem<OrderbookOrder>>) {
        if(!mIsDataLoadingPerformed) {
            return
        }

        val wasOrderbookEmpty = mView.isOrderbookEmpty()

        mView.updateData(
            info = OrderbookInfo.newInstance(orderbook),
            orderbook = orderbook,
            dataActionItems = dataActionItems
        )

        val isOrderbookEmpty = mView.isOrderbookEmpty()

        if(wasOrderbookEmpty && !isOrderbookEmpty) {
            mView.hideInfoView()
            mView.showMainView()
        } else if(isOrderbookEmpty) {
            mView.hideMainView()
            showInfoView()
        }

        updateAppBarScrollingState()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: OrderEvent) {
        if(event.isOriginatedFrom(this)) {
            return
        }

        handleOrderEvent(
            event = event,
            performedActions = mPerformedOrderActions
        )
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedOrderActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        mPerformedOrderActions.merge(event.attachment)

        event.consume()
    }


    override fun onNavigateUpPressed(): Boolean {
        if(!mIsRealTimeDataUpdateEventReceived) {
            EventBus.getDefault().postSticky(OrderbookDataReloadEvent.newInstance(this))
        }

        if(!mPerformedOrderActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedOrderActionsEvent.init(
                mPerformedOrderActions, this
            ))
        }

        return super.onNavigateUpPressed()
    }


    override fun onBackPressed(): Boolean = onNavigateUpPressed()


    override fun canReceiveEvents(): Boolean {
        return true
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            isAppBarScrollingEnabled = it.isAppBarScrollingEnabled
            mIsRealTimeDataUpdateEventReceived = it.isRealTimeDataUpdateEventReceived
            mIsDataLoadingPerformed = it.isDataLoadingPerformed
            currencyMarket = it.currencyMarket
            mPerformedOrderActions = it.performedOrderActions
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            isAppBarScrollingEnabled = isAppBarScrollingEnabled,
            isRealTimeDataUpdateEventReceived = mIsRealTimeDataUpdateEventReceived,
            isDataLoadingPerformed = mIsDataLoadingPerformed,
            currencyMarket = currencyMarket,
            performedOrderActions = mPerformedOrderActions
        ))
    }


    override fun toString(): String {
        return "${super.toString()}_${currencyMarket.pairName}"
    }


}