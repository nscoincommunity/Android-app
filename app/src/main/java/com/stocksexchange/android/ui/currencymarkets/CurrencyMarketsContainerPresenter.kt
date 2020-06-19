package com.stocksexchange.android.ui.currencymarkets

import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.android.events.InboxCountItemChangeEvent
import com.stocksexchange.android.events.PerformedCurrencyMarketActionsEvent
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.viewpager.BaseViewPagerPresenter
import com.stocksexchange.android.utils.helpers.handlePerformedCurrencyMarketActionsEvent
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.model.rest.CurrencyPairGroup
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CurrencyMarketsContainerPresenter(
    view: CurrencyMarketsContainerContract.View,
    model: StubModel,
    private val sessionManager: SessionManager,
    private val firebaseEventLogger: FirebaseEventLogger
) : BaseViewPagerPresenter<CurrencyMarketsContainerContract.View, StubModel>(view, model),
    CurrencyMarketsContainerContract.ActionListener {


    val currencyPairGroups: List<CurrencyPairGroup> by lazy { sessionManager.getCurrencyPairGroups() }




    override fun start() {
        super.start()

        mView.updateInboxButtonItemCount()
    }


    override fun onToolbarRightButtonClicked() {
        firebaseEventLogger.onCurrencyMarketsContainerSearchButtonClicked()

        mView.navigateToSearchScreen()
    }


    override fun onToolbarAlertPriceButtonClicked() {
        if(!sessionManager.isUserSignedIn()) {
            return
        }

        mView.navigateToPriceAlertsScreen()
    }


    override fun onToolbarInboxButtonClicked() {
        if(!sessionManager.isUserSignedIn()) {
            return
        }

        mView.navigateToInboxScreen()
    }


    override fun onSortPanelTitleClicked(comparator: CurrencyMarketComparator) {
        mView.sortAdapterItems(comparator)
    }


    override fun onScrollToTopRequested(position: Int) {
        mView.showAppBar(true)

        super.onScrollToTopRequested(position)
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedCurrencyMarketActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        handlePerformedCurrencyMarketActionsEvent(
            performedActionsEvent = event,
            consumerCount = mView.getTabCount()
        )

        event.consume()
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: InboxCountItemChangeEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        mView.updateInboxButtonItemCount()

        event.consume()
    }


    override fun canReceiveEvents(): Boolean = true


}