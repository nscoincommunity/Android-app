package com.stocksexchange.android.ui.wallets.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.stocksexchange.android.R
import com.stocksexchange.android.mappings.mapToWalletItem
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.mappings.mapToWalletItemList
import com.stocksexchange.android.model.VerificationPromptDescriptionType
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.currencymarkets.search.CurrencyMarketsSearchFragment
import com.stocksexchange.android.ui.currencymarkets.search.newArgs
import com.stocksexchange.android.ui.protocolselection.ProtocolSelectionFragment
import com.stocksexchange.android.ui.protocolselection.newArgs
import com.stocksexchange.android.ui.transactioncreation.depositcreation.DepositCreationFragment
import com.stocksexchange.android.ui.transactioncreation.newArgs
import com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.WithdrawalCreationFragment
import com.stocksexchange.android.utils.diffcallbacks.WalletsDiffCallback
import com.stocksexchange.android.ui.verification.prompt.VerificationPromptActivity
import com.stocksexchange.android.ui.verification.prompt.newInstance
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.core.decorators.CardViewItemDecorator
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.wallets_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class WalletsFragment : BaseListDataLoadingFragment<
    WalletsPresenter,
    List<Wallet>,
    WalletItem,
    WalletsRecyclerViewAdapter
    >(), WalletsContract.View {


    companion object {}


    override val mPresenter: WalletsPresenter by inject { parametersOf(this) }




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.setWalletParamsFromExtras(it.walletParameters)
        }
    }


    override fun init() {
        super.init()

        initContentContainer()
    }


    override fun initAdapter(): WalletsRecyclerViewAdapter {
        return WalletsRecyclerViewAdapter(ctx, mItems).apply {
            setResources(WalletsResources.newInstance(
                stringProvider = mStringProvider,
                numberFormatter = get(),
                settings = getSettings()
            ))

            onCurrencyNameClickListener = { _, walletItem, _ ->
                mPresenter.onCurrencyNameClicked(walletItem.itemModel)
            }
            onDepositButtonClickListener = { _, walletItem, _ ->
                mPresenter.onDepositButtonClicked(walletItem.itemModel)
            }
            onWithdrawButtonClickListener = { _, walletItem, _ ->
                mPresenter.onWithdrawButtonClicked(walletItem.itemModel)
            }
        }
    }


    private fun initContentContainer() = with(mRootView.contentContainerFl) {
        ThemingUtil.Wallets.contentContainer(this, getAppTheme())
    }


    override fun addData(data: List<Wallet>) {
        val walletItemList = data.mapToWalletItemList().toMutableList()

        if(isDataSourceEmpty()) {
            mAdapter.items = walletItemList
        } else {
            mAdapter.setItems(walletItemList, WalletsDiffCallback(mItems, walletItemList))
        }

        mItems = walletItemList
    }


    override fun updateItemWith(item: Wallet, position: Int) {
        mAdapter.updateItemWith(position, item.mapToWalletItem())
    }


    override fun navigateToCurrencyMarketsSearchScreen(searchQuery: String) {
        navigate(
            destinationId = R.id.currencyMarketsSearchDest,
            arguments = CurrencyMarketsSearchFragment.newArgs(searchQuery)
        )
    }


    override fun launchVerificationPromptActivity() {
        startActivity(VerificationPromptActivity.newInstance(
            context = ctx,
            descriptionType = VerificationPromptDescriptionType.SHORT
        ))
    }


    override fun navigateToProtocolSelectionScreen(transactionType: TransactionType, wallet: Wallet) {
        navigate(
            destinationId = R.id.protocolSelectionDest,
            arguments = ProtocolSelectionFragment.newArgs(transactionType, wallet)
        )
    }


    override fun navigateToDepositCreationScreen(wallet: Wallet, protocol: Protocol) {
        navigate(
            destinationId = R.id.depositCreationDest,
            arguments = DepositCreationFragment.newArgs(wallet, protocol)
        )
    }


    override fun navigateToWithdrawalCreationScreen(wallet: Wallet, protocol: Protocol) {
        navigate(
            destinationId = R.id.withdrawalCreationDest,
            arguments = WithdrawalCreationFragment.newArgs(wallet, protocol)
        )
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(ctx, url, getAppTheme())
    }


    override fun getDataSetIndexForCurrencyId(currencyId: Int): Int? {
        return mAdapter.getDataSetIndexForCurrencyId(currencyId)
    }


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getWalletsIcon(mPresenter.walletParameters)
    }


    override fun getRecyclerViewItemDecoration(): RecyclerView.ItemDecoration {
        return CardViewItemDecorator(
            dimenInPx(R.dimen.recycler_view_divider_size),
            dimenInPx(R.dimen.card_view_elevation)
        )
    }


    override fun clearAdapter() {
        mAdapter.clear()
    }


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getContentLayoutResourceId(): Int = R.layout.wallets_fragment_layout


    fun onShowEmptyWalletsFlagChanged(showEmptyWallets: Boolean) {
        mPresenter.onShowEmptyWalletsFlagChanged(showEmptyWallets)
    }


    fun onSortColumnChanged(sortColumn: WalletBalanceType) {
        mPresenter.onSortColumnChanged(sortColumn)
    }


}