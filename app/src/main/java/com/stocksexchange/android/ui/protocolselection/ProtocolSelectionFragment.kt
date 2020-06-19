package com.stocksexchange.android.ui.protocolselection

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.mappings.mapToProtocolSelectionItemList
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.transactioncreation.depositcreation.DepositCreationFragment
import com.stocksexchange.android.ui.transactioncreation.newArgs
import com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.WithdrawalCreationFragment
import com.stocksexchange.core.decorators.CardViewItemDecorator
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.protocol_selection_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProtocolSelectionFragment : BaseFragment<ProtocolSelectionPresenter>(),
    ProtocolSelectionContract.View {


    companion object {}


    override val mPresenter: ProtocolSelectionPresenter by inject { parametersOf(this) }

    private lateinit var mAdapter: ProtocolSelectionRecyclerViewAdapter




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.transactionType = it.transactionType
            mPresenter.wallet = it.wallet
        }
    }


    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initRecyclerView()
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.ProtocolSelection.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(mStringProvider.getTransactionCreationTitle(mPresenter.transactionType))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }

        ThemingUtil.ProtocolSelection.toolbar(this, getAppTheme())
    }


    private fun initRecyclerView() = with(mRootView.recyclerView) {
        layoutManager = LinearLayoutManager(ctx)
        addItemDecoration(CardViewItemDecorator(
            dimenInPx(R.dimen.recycler_view_divider_size),
            dimenInPx(R.dimen.card_view_elevation)
        ))
        setHasFixedSize(true)

        mAdapter = ProtocolSelectionRecyclerViewAdapter(ctx, getRecyclerViewItems())
        mAdapter.setResources(ProtocolSelectionResources.newInstance(
            currencyId = mPresenter.wallet.currencyId,
            context = ctx,
            settings = getSettings(),
            imageDownloader = get(),
            errorImage = ctx.getColoredCompatDrawable(
                drawableId = R.mipmap.ic_currency_pairs_stub,
                color = getAppTheme().generalTheme.infoViewColor
            )
        ))
        mAdapter.onItemClickListener = { _, item, _ ->
            mPresenter.onProtocolClicked(item.itemModel)
        }

        adapter = mAdapter
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


    override fun getContentLayoutResourceId(): Int = R.layout.protocol_selection_fragment_layout


    private fun getRecyclerViewItems(): MutableList<ProtocolSelectionItem> {
        return mPresenter.wallet.getProtocols(mPresenter.transactionType)
            .mapToProtocolSelectionItemList()
            .toMutableList()
    }


}