package com.stocksexchange.android.ui.wallets.search

import com.stocksexchange.android.events.PerformedWalletActionsEvent
import com.stocksexchange.android.utils.helpers.handlePerformedWalletActionsEvent
import com.stocksexchange.android.model.PerformedWalletActions
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.ReloadProvider
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.helpers.tag
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WalletsSearchPresenter(
    view: WalletsSearchContract.View,
    model: StubModel,
    private val reloadProvider: ReloadProvider
) : BasePresenter<WalletsSearchContract.View, StubModel>(view, model), WalletsSearchContract.ActionListener {


    companion object {

        private val CLASS = WalletsSearchPresenter::class.java

        private val KEY_PERFORMED_ACTIONS = tag(CLASS, "performed_actions")

    }


    private var mPerformedActions = PerformedWalletActions()



    override fun start() {
        super.start()

        if (reloadProvider.walletsSearch()) {
            mView.reloadSearchQuery()
            reloadProvider.setWalletsSearch(false)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedWalletActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        // Handling the event
        handlePerformedWalletActionsEvent(event)

        // Merging with this instance's actions in order to pass it
        // backwards in the stack
        mPerformedActions.merge(event.attachment)

        // Consuming the event
        event.consume()
    }


    override fun onNavigateUpPressed(): Boolean {
        if(!mPerformedActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedWalletActionsEvent.init(
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
            mPerformedActions = get(KEY_PERFORMED_ACTIONS, PerformedWalletActions())
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        with(savedState) {
            save(KEY_PERFORMED_ACTIONS, mPerformedActions)
        }
    }


}