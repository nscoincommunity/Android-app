package com.stocksexchange.android.ui.alertprice.additem

import com.stocksexchange.android.R
import com.stocksexchange.android.events.CurrencyMarketEvent
import com.stocksexchange.android.events.PerformedCurrencyMarketActionsEvent
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.api.exceptions.rest.AlertPriceException
import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.helpers.handleCurrencyMarketEvent
import com.stocksexchange.api.model.rest.AlertPriceComparison
import com.stocksexchange.api.model.rest.CurrencyMarket
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AlertPriceAddPresenter(
    view: AlertPriceAddContract.View,
    model: AlertPriceAddModel
) : BasePresenter<
    AlertPriceAddContract.View,
    AlertPriceAddModel
    >(view, model), AlertPriceAddContract.ActionListener, AlertPriceAddModel.ActionListener {


    var currencyMarket: CurrencyMarket = CurrencyMarket.STUB_CURRENCY_MARKET

    private var mPerformedActions: PerformedCurrencyMarketActions = PerformedCurrencyMarketActions()

    private var mAlertPriceItemIdForDelete: Int = -1



    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        mView.showKeyboard()
    }


    override fun onShowMoreLessItems(id: Int) {
        mModel.getAlertPriceListByPairId(id)
    }


    override fun onDeletePriceItem(id: Int) {
        mModel.deleteAlertPriceItem(id)
    }


    override fun onClickCreateAlertPrice() {
        val price: Double
        try {
            price = mView.getInputText().toDouble()
        } catch (e: Exception) {
            showDialog(mStringProvider.getString(R.string.alert_price_incorrect_price))
            return
        }

        if (price <= 0) {
            showDialog(mStringProvider.getString(R.string.alert_price_should_positive))
            return
        }

        var comparison = AlertPriceComparison.LESS.title
        if (price > currencyMarket.lastPrice) {
            comparison = AlertPriceComparison.GREATER.title
        }

        if ((mView.getLessAlertPriceItem() != null) && (comparison == AlertPriceComparison.LESS.title)) {
            showDialog(mStringProvider.getString(R.string.alert_price_already_have_less))
            return
        }
        if ((mView.getMoreAlertPriceItem() != null) && (comparison == AlertPriceComparison.GREATER.title)) {
            showDialog(mStringProvider.getString(R.string.alert_price_already_have_greater))
            return
        }

        mModel.createAlertPriceItem(
            AlertPriceParameters(
                currencyPairId = currencyMarket.pairId,
                comparison = comparison,
                price = price.toString()
            )
        )
    }


    override fun onRightButtonClicked() {
        mModel.toggleFavoriteState(currencyMarket)
    }


    override fun onCurrencyMarketFavorited(currencyMarket: CurrencyMarket) {
        mView.updateFavoriteButtonState(true)

        handleCurrencyMarketEvent(
            event = CurrencyMarketEvent.favorite(currencyMarket, this),
            performedActions = mPerformedActions
        )
    }


    override fun onClickClearButton() {
        mView.setTextToInputField(0.0)
    }


    override fun onClickDeleteLessItem() {
        mView.getLessAlertPriceItem()?.let {
            mAlertPriceItemIdForDelete = it.id
            onDeletePriceItem(it.id)
        }
    }


    override fun onClickDeleteMoreItem() {
        mView.getMoreAlertPriceItem()?.let {
            mAlertPriceItemIdForDelete = it.id
            onDeletePriceItem(it.id)
        }
    }

    override fun onCurrencyMarketUnfavorited(currencyMarket: CurrencyMarket) {
        mView.updateFavoriteButtonState(false)

        handleCurrencyMarketEvent(
            event = CurrencyMarketEvent.unfavorite(currencyMarket, this),
            performedActions = mPerformedActions
        )
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when (requestType) {

            AlertPriceAddModel.REQUEST_TYPE_GET_ALERT_PRICE_BY_PAIR_ID_RETRIEVAL -> {
                onGettingMoreLessAlertPriceByPairId(response as List<AlertPrice>)
            }

            AlertPriceAddModel.REQUEST_TYPE_CREATE_ALERT_PRICE_RETRIEVAL -> {
                onCreatedAlertPrice()
            }

        }
    }


    override fun onResponseReceived(requestType: Int) {
        when (requestType) {
            AlertPriceAddModel.REQUEST_TYPE_DELETE_ALERT_PRICE_RETRIEVAL -> {
                mView.hideToolbarProgressBar()
                onDeleteAlertPrice()
            }
        }
    }


    override fun onRequestSent(requestType: Int) {
        when (requestType) {
            AlertPriceAddModel.REQUEST_TYPE_DELETE_ALERT_PRICE_RETRIEVAL -> {
                mView.showToolbarProgressBar()
            }
        }
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when (requestType) {
            AlertPriceAddModel.REQUEST_TYPE_CREATE_ALERT_PRICE_RETRIEVAL -> {
                onErrorReceived(exception)
            }
        }
    }


    override fun onNavigateUpPressed(): Boolean {
        if (!mPerformedActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedCurrencyMarketActionsEvent.init(
                mPerformedActions, this
            ))
        }

        mView.hideKeyboard()

        return super.onNavigateUpPressed()
    }


    override fun onBackPressed(): Boolean = onNavigateUpPressed()


    private fun onErrorReceived(exception: Throwable) {
        when (exception) {
            is AlertPriceException -> when (exception.error) {
                AlertPriceException.Error.UNKNOWN -> onUnknownErrorReceived(exception.message)
                AlertPriceException.Error.ALREADY_LESS -> onLessExistErrorReceived(exception.errorMessages)
                AlertPriceException.Error.ALREADY_MORE -> onMoreExistErrorReceived(exception.errorMessages)
                AlertPriceException.Error.ALREADY_EXIST -> onAlreadyExistErrorReceived()
            }
        }
    }


    private fun onLessExistErrorReceived(errors: LinkedHashMap<String, List<String>>) {
        showDialog(
            mStringProvider.getString(
                R.string.alert_price_already_less,
                getPriceFromErrors(errors)
            )
        )
    }


    private fun onMoreExistErrorReceived(errors: LinkedHashMap<String, List<String>>) {
        showDialog(
            mStringProvider.getString(
                R.string.alert_price_already_more,
                getPriceFromErrors(errors)
            )
        )
    }


    private fun onAlreadyExistErrorReceived() {
        showDialog(
            mStringProvider.getString(
                R.string.alert_price_already_exist
            )
        )
    }


    private fun getPriceFromErrors(errors: LinkedHashMap<String, List<String>>): String {
        var lessPrice = ""

        if (errors.containsKey("current_price")) {
            val message = errors["current_price"]
            message?.forEach {
                lessPrice += it
            }
        }

        return lessPrice
    }


    private fun onUnknownErrorReceived(message: String) {
        if (message.isNotEmpty()) {
            showDialog(message)
        }
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: CurrencyMarketEvent) {
        if (event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        val attachment = event.attachment

        when (event.action) {
            CurrencyMarketEvent.Action.FAVORITED -> {
                mView.updateFavoriteButtonState(true)
                mPerformedActions.addFavoriteCurrencyMarket(attachment)
            }

            CurrencyMarketEvent.Action.UNFAVORITED -> {
                mView.updateFavoriteButtonState(false)
                mPerformedActions.addUnfavoriteCurrencyMarket(attachment)
            }

            CurrencyMarketEvent.Action.UPDATED -> {
                if ((currencyMarket.pairId == attachment.pairId) &&
                    (currencyMarket.lastPrice != attachment.lastPrice)) {
                    currencyMarket = attachment

                    mView.updateLastPrice(attachment.lastPrice)
                }

                mPerformedActions.addUpdatedCurrencyMarket(attachment)
            }
        }

        event.consume()
    }


    private fun onCreatedAlertPrice() {
        mView.navigateBack()
    }


    private fun onGettingMoreLessAlertPriceByPairId(alertPriceItems: List<AlertPrice>) {
        mView.showMoreLessItems(alertPriceItems)
    }


    private fun onDeleteAlertPrice() {
        mView.deleteAlertPriceItem(mAlertPriceItemIdForDelete)
        mAlertPriceItemIdForDelete = -1
    }


    private fun showDialog(text: String) {
        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            content = text,
            positiveBtnText = mStringProvider.getString(R.string.ok),
            positiveBtnClick = {
                mView.showKeyboard()
            }
        ))
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            currencyMarket = it.currencyMarket
            mPerformedActions = it.performedActions
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            currencyMarket = currencyMarket,
            performedActions = mPerformedActions
        ))
    }


    override fun canReceiveEvents(): Boolean = true


    override fun toString(): String {
        return "${super.toString()}_${currencyMarket.pairName}"
    }


}