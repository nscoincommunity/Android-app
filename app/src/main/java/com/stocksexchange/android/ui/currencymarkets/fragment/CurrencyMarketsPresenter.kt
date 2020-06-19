package com.stocksexchange.android.ui.currencymarkets.fragment

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.android.events.CurrencyMarketEvent
import com.stocksexchange.android.model.*
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.core.providers.ConnectionProvider
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CurrencyMarketsPresenter(
    view: CurrencyMarketsContract.View,
    model: CurrencyMarketsModel,
    private val sessionManager: SessionManager,
    private val connectionProvider: ConnectionProvider
) : BaseListDataLoadingPresenter<
    CurrencyMarketsContract.View,
    CurrencyMarketsModel,
    List<CurrencyMarket>,
    CurrencyMarketParameters
    >(view, model), CurrencyMarketsContract.ActionListener, CurrencyMarketsModel.ActionListener {


    var currencyMarketParameters: CurrencyMarketParameters = CurrencyMarketParameters.getDefaultParameters()

    private var mIsDataSetSorted: Boolean = false

    private var mComparator: CurrencyMarketComparator? = null




    init {
        model.setActionListener(this)
    }


    override fun startListeningToSocketEvents() = with(mSocketConnection) {
        val subscriberKey = this@CurrencyMarketsPresenter.toString()
        val userId = sessionManager.getProfileInfo()?.id.toString()

        startListeningToTickerUpdates(subscriberKey)

        if(sessionManager.isUserSignedIn()) {
            startListeningToUserInboxCountUpdates(
                subscriberKey = subscriberKey,
                userId = userId
            )
            startListeningUserInboxNewMessageUpdates(
                subscriberKey = subscriberKey,
                userId = userId
            )
        }
    }


    override fun stopListeningToSocketEvents() = with(mSocketConnection) {
        stopListeningToTickerUpdates(this@CurrencyMarketsPresenter.toString())
    }


    override fun getSearchQuery(): String = currencyMarketParameters.searchQuery


    override fun onSearchQueryChanged(query: String) {
        currencyMarketParameters = currencyMarketParameters.copy(searchQuery = query)
    }


    override fun onDataLoadingSucceeded(data: List<CurrencyMarket>) {
        if(mView.isDataSourceEmpty()) {
            mView.setItems(data, false)

            mIsDataSetSorted = false
            sortDataSetIfNecessary()
        } else {
            mView.setItemsAndSort(data, mComparator)
            mIsDataSetSorted = true
        }

        showNetworkCheckToastIfNeeded()
    }


    private fun showNetworkCheckToastIfNeeded() {
        try {
            if ((getDataLoadingParams().currencyMarketType != CurrencyMarketType.SEARCH) &&
                !connectionProvider.isNetworkAvailable()) {
                mView.showToast(mStringProvider.getInternetConnectionCheckMessage())
            }
        } catch (e: Exception) {

        }
    }


    override fun isDataRealTimeDependant(): Boolean = true


    override fun shouldLoadDataOnRealTimeUpdate(): Boolean {
        val isViewSelected = mView.isViewSelected()
        val type = getDataLoadingParams().currencyMarketType
        val isRightType = ((type != CurrencyMarketType.FAVORITES) && (type != CurrencyMarketType.SEARCH))

        return (isViewSelected && isRightType)
    }


    override fun getEmptyViewCaption(params: CurrencyMarketParameters): String {
        return mStringProvider.getCurrencyMarketsEmptyCaption(params)
    }


    override fun getDataLoadingParams(): CurrencyMarketParameters = currencyMarketParameters


    override fun onViewSelected() {
        if(isDataLoadingAllowed()) {
            super.onViewSelected()
            return
        }

        sortDataSetIfNecessary()
    }


    override fun onCurrencyMarketItemClicked(currencyMarket: CurrencyMarket) {
        mView.navigateToCurrencyMarketPreviewScreen(currencyMarket)
    }


    override fun onCurrencyMarketItemLongClicked(currencyMarket: CurrencyMarket) {
        if(!sessionManager.isUserSignedIn()) {
            // Returning since we do not allow to open trading screen
            // when there is no user available
            return
        }

        mView.navigateToTradeScreen(
            tradeType = TradeType.BUY,
            currencyMarket = currencyMarket
        )
    }


    override fun onSortPayloadChanged(payload: Any) {
        if((payload !is CurrencyMarketComparator) || (payload.id == mComparator?.id)) {
            return
        }

        mComparator = payload
        mIsDataSetSorted = false

        sortDataSetIfNecessary()
    }


    private fun sortDataSetIfNecessary() {
        if(shouldSortDataSet()) {
            sortDataSet()
        }
    }


    private fun shouldSortDataSet(): Boolean {
        return (!mIsDataSetSorted &&
                mView.isInitialized() &&
                mView.isViewSelected() &&
                !mView.isDataSourceEmpty())
    }


    private fun sortDataSet() {
        mComparator?.let { mView.sortDataSet(it) }

        mIsDataSetSorted = true
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: CurrencyMarketEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed(this)) {
            return
        }

        if(!mModel.wasDataLoadingPerformed()) {
            event.consume()
            return
        }

        val currencyMarketType = currencyMarketParameters.currencyMarketType
        val currencyMarket = event.attachment

        when(event.action) {

            CurrencyMarketEvent.Action.UPDATED -> {
                mView.getDataSetPositionForPairId(currencyMarket.pairId)?.also {
                    mView.updateItemWith(currencyMarket, it)
                }
            }

            CurrencyMarketEvent.Action.FAVORITED -> {
                if(currencyMarketType == CurrencyMarketType.FAVORITES) {
                    if(!mView.containsCurrencyMarket(currencyMarket)) {
                        mView.addCurrencyMarketChronologically(currencyMarket, mComparator)
                    }
                }
            }

            CurrencyMarketEvent.Action.UNFAVORITED -> {
                if(currencyMarketType == CurrencyMarketType.FAVORITES) {
                    if(mView.containsCurrencyMarket(currencyMarket)) {
                        mView.deleteCurrencyMarket(currencyMarket)
                    }
                }
            }

        }

        event.consume(this)
    }


    override fun canReceiveEvents(): Boolean {
        return true
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            mIsDataSetSorted = it.isDataSetSorted
            currencyMarketParameters = it.currencyMarketParameters
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            isDataSetSorted = mIsDataSetSorted,
            currencyMarketParameters = currencyMarketParameters
        ))
    }


    override fun toString(): String {
        val type = currencyMarketParameters.currencyMarketType.name
        val groupName = currencyMarketParameters.currencyPairGroup.name

        return "${super.toString()}_${type}_$groupName"
    }


}