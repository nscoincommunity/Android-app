package com.stocksexchange.android.ui.orders.search

import com.stocksexchange.android.events.OrderEvent
import com.stocksexchange.android.events.PerformedOrderActionsEvent
import com.stocksexchange.android.utils.helpers.handleOrderEvent
import com.stocksexchange.android.model.PerformedOrderActions
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.api.model.rest.OrderLifecycleType
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrdersSearchPresenter(
    view: OrdersSearchContract.View,
    model: StubModel
) : BasePresenter<OrdersSearchContract.View, StubModel>(view, model), OrdersSearchContract.ActionListener {


    var orderLifecycleType: OrderLifecycleType = OrderLifecycleType.ACTIVE

    private var mPerformedActions = PerformedOrderActions()




    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: OrderEvent) {
        if(event.isOriginatedFrom(this)) {
            return
        }

        // Storing the event inside performed actions container
        handleOrderEvent(event, mPerformedActions)
    }


    override fun onNavigateUpPressed(): Boolean {
        if(!mPerformedActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedOrderActionsEvent.init(
                performedActions = mPerformedActions,
                source = this
            ))
        }

        return super.onNavigateUpPressed()
    }


    override fun onBackPressed(): Boolean = onNavigateUpPressed()


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            orderLifecycleType = it.orderLifecycleType
            mPerformedActions = it.performedOrderActions
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            orderLifecycleType = orderLifecycleType,
            performedOrderActions = mPerformedActions
        ))
    }


}