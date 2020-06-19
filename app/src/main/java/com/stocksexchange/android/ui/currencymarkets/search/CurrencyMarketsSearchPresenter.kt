package com.stocksexchange.android.ui.currencymarkets.search

import com.stocksexchange.android.events.CurrencyMarketEvent
import com.stocksexchange.android.events.PerformedCurrencyMarketActionsEvent
import com.stocksexchange.android.utils.helpers.handlePerformedCurrencyMarketActionsEvent
import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.helpers.tag
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CurrencyMarketsSearchPresenter(
    view: CurrencyMarketsSearchContract.View,
    model: StubModel
) : BasePresenter<CurrencyMarketsSearchContract.View, StubModel>(view, model), CurrencyMarketsSearchContract.ActionListener {


    companion object {

        private val CLASS = CurrencyMarketsSearchPresenter::class.java

        private val KEY_PERFORMED_ACTIONS = tag(CLASS, "performed_actions")

    }


    private var mPerformedActions = PerformedCurrencyMarketActions()




    override fun onSortPanelTitleClicked(comparator: CurrencyMarketComparator) {
        mView.sortAdapterItems(comparator)
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
    fun onEvent(event: CurrencyMarketEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        // Adding to the performed actions
        mPerformedActions.addUpdatedCurrencyMarket(event.attachment)

        // Consuming the event
        event.consume()
    }


    override fun onNavigateUpPressed(): Boolean {
        if(!mPerformedActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedCurrencyMarketActionsEvent.init(
                performedActions = mPerformedActions,
                source = this
            ))
        }

        return super.onNavigateUpPressed()
    }


    override fun onBackPressed(): Boolean = onNavigateUpPressed()


    override fun canReceiveEvents(): Boolean = true


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        with(savedState) {
            mPerformedActions = get(KEY_PERFORMED_ACTIONS, PerformedCurrencyMarketActions())
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        with(savedState) {
            save(KEY_PERFORMED_ACTIONS, mPerformedActions)
        }
    }


}