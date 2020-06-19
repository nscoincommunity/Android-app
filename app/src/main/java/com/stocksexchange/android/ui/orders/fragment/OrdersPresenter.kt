package com.stocksexchange.android.ui.orders.fragment

import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.events.OrderEvent
import com.stocksexchange.android.events.ReloadOrdersEvent
import com.stocksexchange.android.events.ReloadWalletsEvent
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingPresenter
import com.stocksexchange.android.utils.ReloadProvider
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrdersPresenter(
    view: OrdersContract.View,
    model: OrdersModel,
    private val sessionManager: SessionManager,
    private val reloadProvider: ReloadProvider
) : BaseListDataLoadingPresenter<OrdersContract.View, OrdersModel, List<OrderData>, OrderParameters>(view, model),
    OrdersContract.ActionListener, OrdersModel.ActionListener {


    var orderParameters: OrderParameters = OrderParameters.getDefaultParameters()




    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        reloadOrders()
    }


    override fun startListeningToSocketEvents() = with(mSocketConnection) {
        if(shouldListenToSocketUpdates()) {
            startListeningToActiveOrdersUpdates(
                subscriberKey = this@OrdersPresenter.toString(),
                userId = sessionManager.getProfileInfo()?.id.toString(),
                currencyPairId = orderParameters.currencyPairId.toString()
            )
        }
    }


    override fun stopListeningToSocketEvents() = with(mSocketConnection) {
        if(shouldListenToSocketUpdates()) {
            stopListeningToActiveOrdersUpdates(
                subscriberKey = this@OrdersPresenter.toString(),
                userId = sessionManager.getProfileInfo()?.id.toString(),
                currencyPairId = orderParameters.currencyPairId.toString()
            )
        }
    }


    private fun shouldListenToSocketUpdates(): Boolean {
        val userId = sessionManager.getProfileInfo()?.id?.toString()

        return ((orderParameters.lifecycleType == OrderLifecycleType.ACTIVE) &&
                (orderParameters.selectivityType == OrderSelectivityType.SPECIFIC_PAIR_ID) &&
                (userId != null))
    }


    private fun updateDataLoadingParams(getNewParams: (OrderParameters) -> OrderParameters) {
        orderParameters = getNewParams(getDataLoadingParams())
    }


    override fun getSearchQuery(): String = orderParameters.searchQuery


    override fun onSearchQueryChanged(query: String) {
        orderParameters = orderParameters.copy(searchQuery = query)
    }


    override fun resetParameters() {
        super.resetParameters()

        updateDataLoadingParams {
            it.resetOffset()
        }
    }


    override fun getDataTypeForTrigger(trigger: DataLoadingTrigger): DataType {
        return when(trigger) {
            DataLoadingTrigger.START,
            DataLoadingTrigger.VIEW_SELECTION -> DataType.NEW_DATA

            else -> super.getDataTypeForTrigger(trigger)
        }
    }


    override fun getEmptyViewCaption(params: OrderParameters): String {
        return mStringProvider.getOrdersEmptyCaption(params)
    }


    override fun getDataLoadingParams(): OrderParameters {
        return orderParameters
    }


    override fun onRefreshData() {
        resetParameters()
        mView.clearAdapter()

        updateDataLoadingParams {
            it.resetOffset()
        }

        super.onRefreshData()
    }


    override fun onBottomReached(reachedCompletely: Boolean) {
        if(mView.isViewSelected()) {
            updateDataLoadingParams {
                it.copy(offset = mView.getDataSetSize())
            }

            loadData(DataType.OLD_DATA, DataLoadingTrigger.BOTTOM_REACH, true)
        }
    }


    override fun onDataLoadingSucceeded(data: List<OrderData>) {
        super.onDataLoadingSucceeded(data)

        updateDataLoadingParams {
            it.increaseOffset(data.size)
        }
    }


    override fun onMarketNameClicked(currencyMarket: CurrencyMarket) {
        if(currencyMarket.isStub) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        mView.navigateToCurrencyMarketPreviewScreen(currencyMarket)
    }


    override fun onCancelButtonClicked(orderData: OrderData) {
        showOrderCancellationConfirmationDialog(orderData)
    }


    private fun showOrderCancellationConfirmationDialog(orderData: OrderData) {
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
                onOrderCancellationConfirmed(orderData)
            }
        ))
    }


    private fun onOrderCancellationConfirmed(orderData: OrderData) {
        mModel.performOrderCancellationRequest(
            order = orderData.order,
            metadata = orderData
        )
    }


    override fun onRequestSent(requestType: Int) {
        mView.showSecondaryProgressBar()
    }


    override fun onResponseReceived(requestType: Int) {
        mView.hideSecondaryProgressBar()
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            OrdersModel.REQUEST_TYPE_ORDER_CANCELLATION -> {
                onOrderCancellationSucceeded(
                    response = (response as OrdersCancellationResponse),
                    originalOrderData = (metadata as OrderData)
                )

                postReloadWalletsEvent()
            }

        }
    }


    private fun onOrderCancellationSucceeded(response: OrdersCancellationResponse,
                                             originalOrderData: OrderData) {
        if(response.isEmpty) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        val originalOrder = originalOrderData.order

        when {
            response.wasPutIntoProcessingQueue(originalOrder.id) -> {
                mView.deleteOrder(originalOrderData)
                mView.showToast(mStringProvider.getString(R.string.order_cancelled))

                val updatedOrder = Order.cancelActiveOrder(originalOrder)
                val updatedOrderData = originalOrderData.copy(order = updatedOrder)

                EventBus.getDefault().post(OrderEvent.updateStatus(updatedOrderData, this))
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


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when(requestType) {

            OrdersModel.REQUEST_TYPE_ORDER_CANCELLATION -> {
                mView.showToast(mStringProvider.getErrorMessage(exception))
            }

        }
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: OrderEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed(this)) {
            return
        }

        val orderMode = orderParameters.mode
        val orderLifecycleType = orderParameters.lifecycleType
        val orderData = event.attachment
        val order = orderData.order

        val deleteOrderIfNeededFunc = {
            if(!mView.isDataSourceEmpty() && mView.containsOrder(orderData)) {
                mView.deleteOrder(orderData)
            }
        }
        val addOrderIfNeededFunc = {
            if(mModel.wasDataLoadingPerformed() && !mView.containsOrder(orderData)) {
                mView.addOrderChronologically(orderData, orderParameters.sortOrder)
                mView.scrollToTop()
            }
        }
        val updateOrderIfNeededFunc = {
            if(mView.containsOrder(orderData)) {
                mView.updateOrder(mView.getOrderPosition(orderData) ?: 0, orderData)
            } else {
                addOrderIfNeededFunc()
            }
        }

        when(orderMode) {
            OrderMode.STANDARD -> when(event.action) {

                OrderEvent.Action.STATUS_UPDATED -> when(order.status) {
                    OrderStatus.PENDING -> when(orderLifecycleType) {
                        OrderLifecycleType.ACTIVE -> addOrderIfNeededFunc()
                    }

                    OrderStatus.FINISHED, OrderStatus.PARTIAL -> when(orderLifecycleType) {
                        OrderLifecycleType.ACTIVE -> deleteOrderIfNeededFunc()
                        OrderLifecycleType.COMPLETED -> addOrderIfNeededFunc()
                    }

                    OrderStatus.CANCELLED -> when(orderLifecycleType) {
                        OrderLifecycleType.ACTIVE -> deleteOrderIfNeededFunc()
                        OrderLifecycleType.CANCELLED -> addOrderIfNeededFunc()
                    }
                }

                OrderEvent.Action.FILLED_AMOUNT_UPDATED -> when(orderLifecycleType) {
                    OrderLifecycleType.ACTIVE -> updateOrderIfNeededFunc()
                }

            }
        }

        event.consume(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: ReloadOrdersEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        when(event.action) {
            ReloadOrdersEvent.Action.ORDER_ACTIVE -> {
                reloadOrders()
            }
        }

        event.consume()
    }


    private fun reloadOrders() {
        if (reloadProvider.activeOrders()) {
            onRefreshData()
            reloadProvider.setActiveOrders(false)
        }
    }


    private fun postReloadWalletsEvent() {
        reloadProvider.setWallets(true)
        mView.postActionDelayed(2000) {
            if (EventBus.getDefault().hasSubscriberForEvent(ReloadWalletsEvent::class.java)) {
                EventBus.getDefault().post(ReloadWalletsEvent.reloadWallets(this))
            }
        }
    }


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            orderParameters = it.orderParameters
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            orderParameters = orderParameters
        ))
    }


    override fun toString(): String {
        val mode = orderParameters.mode.name
        val lifecycleType = orderParameters.lifecycleType.name

        return "${super.toString()}_${mode}_$lifecycleType"
    }


}