package com.stocksexchange.android.ui.protocolselection

import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.android.events.PerformedWalletActionsEvent
import com.stocksexchange.android.events.WalletEvent
import com.stocksexchange.android.model.PerformedWalletActions
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.helpers.handlePerformedWalletActionsEvent
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.api.model.rest.Wallet
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ProtocolSelectionPresenter(
    view: ProtocolSelectionContract.View,
    model: StubModel
) : BasePresenter<ProtocolSelectionContract.View, StubModel>(view, model),
    ProtocolSelectionContract.ActionListener {


    var transactionType: TransactionType = TransactionType.DEPOSITS

    var wallet: Wallet = Wallet.STUB_WALLET

    private var mPerformedWalletActions = PerformedWalletActions()




    override fun onProtocolClicked(protocol: Protocol) {
        when(transactionType) {
            TransactionType.DEPOSITS -> mView.navigateToDepositCreationScreen(wallet, protocol)
            TransactionType.WITHDRAWALS -> mView.navigateToWithdrawalCreationScreen(wallet, protocol)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedWalletActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        handlePerformedWalletActionsEvent(event)

        event.consume()
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: WalletEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        val wallet = event.attachment

        when(event.action) {

            WalletEvent.Action.ID_CREATED -> {
                mPerformedWalletActions.addIdCreatedWallet(wallet)

                this.wallet = wallet
            }

            WalletEvent.Action.BALANCE_UPDATED -> {
                mPerformedWalletActions.addBalanceChangedWallet(wallet)
            }

        }

        event.consume()
    }


    override fun canReceiveEvents(): Boolean = true


    override fun onNavigateUpPressed(): Boolean {
        if(!mPerformedWalletActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedWalletActionsEvent.init(
                performedActions = mPerformedWalletActions,
                source = this
            ))
        }

        return super.onNavigateUpPressed()
    }


    override fun onBackPressed(): Boolean = onNavigateUpPressed()


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            transactionType = it.transactionType
            wallet = it.wallet
            mPerformedWalletActions = it.performedWalletActions
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            transactionType = transactionType,
            wallet = wallet,
            performedWalletActions = mPerformedWalletActions
        ))
    }


    override fun toString(): String {
        return "${super.toString()}_${wallet.currencyName}"
    }


}