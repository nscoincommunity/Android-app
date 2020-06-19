package com.stocksexchange.android.ui.wallets.fragment

import com.stocksexchange.android.R
import com.stocksexchange.android.events.ReloadWalletsEvent
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.android.events.WalletEvent
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.model.UnsupportedTransactionCreationCurrency
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.exceptions.rest.WalletException
import com.stocksexchange.api.model.rest.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WalletsPresenter(
    view: WalletsContract.View,
    model: WalletsModel,
    private val sessionManager: SessionManager
) : BaseListDataLoadingPresenter<
    WalletsContract.View,
    WalletsModel,
    List<Wallet>,
    WalletParameters
    >(view, model), WalletsContract.ActionListener, WalletsModel.ActionListener {


    var walletParameters: WalletParameters = WalletParameters.getDefaultParameters()




    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        reloadWallets()
    }


    fun setWalletParamsFromExtras(params: WalletParameters) {
        walletParameters = params.copy(
            emptyWalletsFilter = getWalletFilterForFlag(!sessionManager.getSettings().areEmptyWalletsHidden)
        )
    }


    override fun shouldShowEmptyView(lastDataFetchingException: Throwable): Boolean {
        return (super.shouldShowEmptyView(lastDataFetchingException) ||
            (((lastDataFetchingException as? WalletException)?.isWalletNotFoundError) ?: false))
    }


    override fun getDataTypeForTrigger(trigger: DataLoadingTrigger): DataType {
        return when(trigger) {
            DataLoadingTrigger.START -> DataType.NEW_DATA

            else -> super.getDataTypeForTrigger(trigger)
        }
    }


    override fun getEmptyViewCaption(params: WalletParameters): String {
        return mStringProvider.getWalletsEmptyCaption(params)
    }


    override fun getDataLoadingParams(): WalletParameters {
        return walletParameters
    }


    override fun onCurrencyNameClicked(wallet: Wallet) {
        mView.navigateToCurrencyMarketsSearchScreen(wallet.currencySymbol)
    }


    override fun onDepositButtonClicked(wallet: Wallet) {
        if(isTransactionCreationSupportedOnlyOnWebsite(wallet)) {
            showCurrencyTransactionDeniedDialog()
            return
        }

        launchProperActivityOnButtonClick(TransactionType.DEPOSITS, wallet)
    }


    override fun onWithdrawButtonClicked(wallet: Wallet) {
        val profileInfo = sessionManager.getProfileInfo()

        if((profileInfo == null) || !profileInfo.areWithdrawalsAllowed) {
            mView.launchVerificationPromptActivity()
            return
        }

        if(isTransactionCreationSupportedOnlyOnWebsite(wallet)) {
            showCurrencyTransactionDeniedDialog()
            return
        }

        launchProperActivityOnButtonClick(TransactionType.WITHDRAWALS, wallet)
    }


    private fun launchProperActivityOnButtonClick(transactionType: TransactionType,
                                                  wallet: Wallet) {
        if(wallet.hasMultipleProtocols) {
            mView.navigateToProtocolSelectionScreen(
                transactionType = transactionType,
                wallet = wallet
            )
        } else {
            val protocol = if(wallet.hasProtocols) {
                wallet.protocols.first()
            } else {
                Protocol.STUB_PROTOCOL
            }

            when(transactionType) {
                TransactionType.DEPOSITS -> mView.navigateToDepositCreationScreen(wallet, protocol)
                TransactionType.WITHDRAWALS -> mView.navigateToWithdrawalCreationScreen(wallet, protocol)
            }
        }
    }


    private fun showCurrencyTransactionDeniedDialog() {
        val content = mStringProvider.getString(
            R.string.wallets_fragment_currency_transaction_denied_dialog_message
        )

        showInfoDialog(
            title = mStringProvider.getString(R.string.note),
            content = content
        )
    }


    private fun isTransactionCreationSupportedOnlyOnWebsite(wallet: Wallet): Boolean {
        val currencySymbols = UnsupportedTransactionCreationCurrency.values().map {
            it.name
        }

        return (wallet.currencySymbol in currencySymbols)
    }


    override fun getSearchQuery(): String = walletParameters.searchQuery


    override fun onSearchQueryChanged(query: String) {
        walletParameters = walletParameters.copy(searchQuery = query)
    }


    override fun onShowEmptyWalletsFlagChanged(showEmptyWallets: Boolean) {
        walletParameters = walletParameters.copy(emptyWalletsFilter = getWalletFilterForFlag(showEmptyWallets))

        reloadData(DataLoadingTrigger.OTHER)
    }


    private fun getWalletFilterForFlag(showEmptyWallets: Boolean): WalletFilter {
        return (if(showEmptyWallets) WalletFilter.ANY_WALLET else WalletFilter.NON_EMPTY_WALLET)
    }


    override fun onSortColumnChanged(sortColumn: WalletBalanceType) {
        walletParameters = walletParameters.copy(sortColumn = sortColumn)

        reloadData(DataLoadingTrigger.OTHER)
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: WalletEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        val wallet = event.attachment

        when(event.action) {

            WalletEvent.Action.ID_CREATED,
            WalletEvent.Action.BALANCE_UPDATED -> {
                mView.getDataSetIndexForCurrencyId(wallet.currencyId)?.also {
                    mView.updateItemWith(wallet, it)
                }
            }

        }

        event.consume()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: ReloadWalletsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        when(event.action) {
            ReloadWalletsEvent.Action.WALLETS -> {
                reloadWallets()
            }
        }

        event.consume()
    }


    private fun reloadWallets() {
        if (sessionManager.isUserSignedIn()) {
            loadData(DataType.NEW_DATA, DataLoadingTrigger.REFRESHMENT, false)
        }
    }


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            walletParameters = it.walletParameters
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            walletParameters = walletParameters
        ))
    }


    override fun toString(): String {
        return "${super.toString()}_${walletParameters.mode.name}"
    }


}