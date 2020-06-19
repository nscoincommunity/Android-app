package com.stocksexchange.android.ui.orders

import com.stocksexchange.android.events.PerformedOrderActionsEvent
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.viewpager.BaseViewPagerPresenter
import com.stocksexchange.android.utils.helpers.handlePerformedOrderActionsEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrdersContainerPresenter(
    view: OrdersContainerContract.View,
    model: StubModel
) : BaseViewPagerPresenter<OrdersContainerContract.View, StubModel>(view, model),
    OrdersContainerContract.ActionListener {


    override fun onToolbarRightButtonClicked() {
        mView.navigateToOrdersSearchScreen()
    }


    override fun onScrollToTopRequested(position: Int) {
        mView.showAppBar(true)

        super.onScrollToTopRequested(position)
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedOrderActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        handlePerformedOrderActionsEvent(
            performedActionsEvent = event,
            consumerCount = mView.getPageCount(),
            stickyMode = true
        )

        event.consume()
    }


    override fun canReceiveEvents(): Boolean = true


}