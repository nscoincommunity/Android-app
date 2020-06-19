package com.stocksexchange.android.ui.currencymarkets.fragment

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.android.mappings.mapToCurrencyMarketItem
import com.stocksexchange.android.mappings.mapToCurrencyMarketItemList
import com.stocksexchange.android.model.TradeScreenOpener
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.utils.diffcallbacks.CurrencyMarketsDiffCallback
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewFragment
import com.stocksexchange.android.ui.currencymarketpreview.newArgs
import com.stocksexchange.android.ui.trade.TradeFragment
import com.stocksexchange.android.ui.trade.newArgs
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.interfaces.Searchable
import com.stocksexchange.core.utils.interfaces.Sortable
import kotlinx.android.synthetic.main.currency_markets_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CurrencyMarketsFragment : BaseListDataLoadingFragment<
    CurrencyMarketsPresenter,
    List<CurrencyMarket>,
    CurrencyMarketItem,
    CurrencyMarketsRecyclerViewAdapter
    >(), CurrencyMarketsContract.View, Sortable, Searchable {


    companion object {}


    override val mPresenter: CurrencyMarketsPresenter by inject { parametersOf(this) }




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.currencyMarketParameters = it.currencyMarketParameters
        }
    }


    override fun init() {
        super.init()

        initContentContainer()
    }


    override fun initAdapter(): CurrencyMarketsRecyclerViewAdapter {
        return CurrencyMarketsRecyclerViewAdapter(ctx, mItems).apply {
            setResources(CurrencyMarketResources.newInstance(
                stringProvider = mStringProvider,
                numberFormatter = get()
            ))
            setBaseCurrencySymbolCharacterLimit(getBaseCurrencySymbolCharacterLimit())
            setFiatCurrencyPriceHandler(get())
            setSettings(getSettings())

            onItemClickListener = { _, item, _ ->
                mPresenter.onCurrencyMarketItemClicked(item.itemModel)
            }

            onItemLongClickListener = { _, item, _ ->
                mPresenter.onCurrencyMarketItemLongClicked(item.itemModel)
            }
        }
    }


    private fun initContentContainer() {
        ThemingUtil.CurrencyMarkets.contentContainer(mRootView.contentContainerFl, getAppTheme())
    }


    override fun sort(payload: Any) {
        mPresenter.onSortPayloadChanged(payload)
    }


    override fun sortDataSet(comparator: CurrencyMarketComparator) {
        mAdapter.sort(comparator)
    }


    override fun setItems(items: List<CurrencyMarket>, notifyAboutChange: Boolean) {
        val currencyMarketList = items.mapToCurrencyMarketItemList()
            .toMutableList()
            .also { mItems = it }

        mAdapter.setItems(currencyMarketList, false)
    }


    override fun setItemsAndSort(items: List<CurrencyMarket>, comparator: CurrencyMarketComparator?) {
        val currencyMarketItemList = if(comparator != null) {
            items.sortedWith(comparator)
        } else {
            items
        }.mapToCurrencyMarketItemList().toMutableList()

        mAdapter.setItems(
            currencyMarketItemList,
            CurrencyMarketsDiffCallback(mItems, currencyMarketItemList)
        )

        mItems = currencyMarketItemList
    }


    override fun updateItemWith(item: CurrencyMarket, position: Int) {
        mAdapter.updateItemWith(position, item.mapToCurrencyMarketItem())
    }


    override fun navigateToCurrencyMarketPreviewScreen(currencyMarket: CurrencyMarket) {
        navigate(
            destinationId = R.id.currencyMarketPreviewDest,
            arguments = CurrencyMarketPreviewFragment.newArgs(currencyMarket)
        )
    }


    override fun navigateToTradeScreen(tradeType: TradeType, currencyMarket: CurrencyMarket, selectedPrice: Double) {
        navigate(
            destinationId = R.id.tradeDest,
            arguments = TradeFragment.newArgs(
                tradeScreenOpener = TradeScreenOpener.CURRENCY_MARKETS,
                tradeType = tradeType,
                currencyMarket = currencyMarket,
                selectedPrice = selectedPrice,
                selectedAmount = 0.0
            )
        )
    }


    override fun addData(data: List<CurrencyMarket>) {
        // Not used
    }


    override fun addCurrencyMarketChronologically(currencyMarket: CurrencyMarket,
                                                  comparator: CurrencyMarketComparator?) {
        mAdapter.addItem(
            mAdapter.getChronologicalPositionForCurrencyMarket(currencyMarket, comparator),
            currencyMarket.mapToCurrencyMarketItem()
        )
    }


    override fun deleteCurrencyMarket(currencyMarket: CurrencyMarket) {
        mAdapter.deleteItem(currencyMarket.mapToCurrencyMarketItem())
    }


    override fun canReceiveRealTimeDataUpdateEvent(): Boolean = true


    override fun containsCurrencyMarket(currencyMarket: CurrencyMarket): Boolean {
        return mAdapter.contains(currencyMarket.mapToCurrencyMarketItem())
    }


    override fun getDataSetPositionForPairId(id: Int): Int? {
        return mAdapter.getDataSetPositionForPairId(id)
    }


    override fun getItem(position: Int): CurrencyMarket? {
        return mAdapter.getItem(position)?.itemModel
    }


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getCurrencyMarketsIcon(mPresenter.currencyMarketParameters)
    }


    private fun getBaseCurrencySymbolCharacterLimit(): Int {
        return if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val screenWidthInDp = ctx.getSmallestScreenWidthInDp()

            when {
                (screenWidthInDp < 450) -> 4
                (screenWidthInDp < 500) -> 6

                else -> -1
            }
        } else {
            -1
        }
    }


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getContentLayoutResourceId() = R.layout.currency_markets_fragment_layout


}