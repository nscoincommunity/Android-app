package com.stocksexchange.android.ui.trade

import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.android.events.*
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.ui.views.mapviews.data.MapViewData
import com.stocksexchange.android.utils.ReloadProvider
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.handlers.TradeFormViewHandler
import com.stocksexchange.android.utils.helpers.handleCurrencyMarketEvent
import com.stocksexchange.android.utils.helpers.handleOrderEvent
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.exceptions.rest.OrderCreationException
import com.stocksexchange.api.exceptions.rest.WalletException
import com.stocksexchange.api.model.rest.parameters.OrderCreationParameters
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NotFoundException
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.appendExclamationMark
import com.stocksexchange.core.utils.extensions.clamp
import com.stocksexchange.core.utils.extensions.replaceCommaSpaceWithPeriod
import com.stocksexchange.core.utils.helpers.composeInvalidDataDialogMessage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.roundToInt

class TradePresenter(
    view: TradeContract.View,
    model: TradeModel,
    private val sessionManager: SessionManager,
    private val numberFormatter: NumberFormatter,
    private val connectionProvider: ConnectionProvider,
    private val firebaseEventLogger: FirebaseEventLogger,
    private val reloadProvider: ReloadProvider
) : BasePresenter<TradeContract.View, TradeModel>(view, model), TradeContract.ActionListener,
    TradeModel.ActionListener {


    var selectedPrice: Double = 0.0
    var selectedAmount: Double = 0.0
    var tradeScreenOpener: TradeScreenOpener = TradeScreenOpener.PREVIEW
    var selectedTradeType: TradeType = TradeType.BUY
    var selectedOrderType: OrderType = OrderType.LIMIT
    var currencyMarket: CurrencyMarket = CurrencyMarket.STUB_CURRENCY_MARKET

    private var mAreWalletsInitialized: Boolean = false
    private var mIsInitialPriceSet: Boolean = false

    private var mBaseCurrencyWallet: Wallet = Wallet.STUB_WALLET
    private var mQuoteCurrencyWallet: Wallet = Wallet.STUB_WALLET

    private var mOrderCreationParameters: OrderCreationParameters = OrderCreationParameters.STUB_PARAMS

    private var mPerformedCurrencyMarketActions: PerformedCurrencyMarketActions = PerformedCurrencyMarketActions()
    private var mPerformedOrderActions: PerformedOrderActions = PerformedOrderActions()

    private var mSeekBarTrackingMap: MutableMap<TradeSeekBarType, Boolean> = mutableMapOf()
    private var mTradeTypeValuableFormDataMap: MutableMap<TradeType, ValuableTradeFormData> = mutableMapOf()

    private var mTradeFormViewHandler: TradeFormViewHandler? = null




    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        initTradeFormViewHandler()
        initializeWallets()
        initSocketEvents()
        initInitialPrice()

        mView.updateInboxButtonItemCount()
    }


    private fun initTradeFormViewHandler() {
        if(mTradeFormViewHandler != null) {
            return
        }

        mTradeFormViewHandler = TradeFormViewHandler(
            numberFormatter = numberFormatter,
            listener = mHandlerListener
        )
    }


    private fun initializeWallets() {
        if(mAreWalletsInitialized) {
            return
        }

        loadWallets()
    }


    private fun initSocketEvents() {
        if(mAreWalletsInitialized) {
            startListeningToWalletsBalancesUpdates()
        }
    }


    private fun initInitialPrice() {
        if((selectedPrice != 0.0) || mIsInitialPriceSet) {
            return
        }

        val params = OrderbookParameters.getDefaultParameters(
            currencyPairId = currencyMarket.pairId
        )

        mModel.performOrderbookRetrievalRequest(params)
    }


    private fun loadWallets() {
        mView.hideUserBalancesMainViews()
        mView.hideUserBalancesErrorViews()

        mModel.performWalletsRetrievalRequest(listOf(
            currencyMarket.baseCurrencyId,
            currencyMarket.quoteCurrencyId
        ))
    }


    override fun stop() {
        super.stop()

        mView.clearTradeFormFocus()
    }


    override fun startListeningToSocketEvents() = with(mSocketConnection) {
        val subscriberKey = this@TradePresenter.toString()

        startListeningToTickerUpdates(subscriberKey)

        if(sessionManager.isUserSignedIn()) {
            startListeningToActiveOrdersUpdates(
                subscriberKey = subscriberKey,
                userId = sessionManager.getProfileInfo()?.id.toString(),
                currencyPairId = currencyMarket.pairId.toString()
            )
        }
    }


    override fun stopListeningToSocketEvents() = with(mSocketConnection) {
        val subscriberKey = this@TradePresenter.toString()

        stopListeningToTickerUpdates(subscriberKey)

        if(sessionManager.isUserSignedIn()) {
            stopListeningToActiveOrdersUpdates(
                subscriberKey = subscriberKey,
                userId = sessionManager.getProfileInfo()?.id.toString(),
                currencyPairId = currencyMarket.pairId.toString()
            )
        }
    }


    private fun startListeningToWalletsBalancesUpdates() {
        mSocketConnection.startListeningToWalletsBalancesUpdates(
            subscriberKey = toString(),
            walletIds = getWalletIdsList()
        )
    }


    private fun stopListeningToWalletsBalancesUpdates() {
        mSocketConnection.stopListeningToWalletsBalancesUpdates(
            subscriberKey = toString(),
            walletIds = getWalletIdsList()
        )
    }


    private fun showInvalidDataDialog(errorsList: List<String>) {
        if(errorsList.isEmpty()) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        val title = mStringProvider.getString(R.string.invalid_data_dialog_title)
        val message = composeInvalidDataDialogMessage(
            errorsList,
            mStringProvider.getString(R.string.invalid_data_dialog_footer_text)
        )

        showInfoDialog(title, message)
    }


    /**
     * Shows the stop-limit order warning dialog if necessary.
     *
     * @return true if dialog has been shown; false otherwise
     */
    private fun showStopLimitOrderWarningDialogIfNeeded(): Boolean {
        if(selectedOrderType != OrderType.STOP_LIMIT) {
            return false
        }

        val stopPrice = getInputViewNumberValue(TradeInputView.STOP_PRICE)
        val bestPrice = when(selectedTradeType) {
            TradeType.BUY -> currencyMarket.bestAskPrice
            TradeType.SELL -> currencyMarket.bestBidPrice
        }
        val buyTradeTypeCheck = ((selectedTradeType == TradeType.BUY) && (bestPrice > stopPrice))
        val sellTradeTypeCheck = ((selectedTradeType == TradeType.SELL) && (bestPrice < stopPrice))

        return if(buyTradeTypeCheck || sellTradeTypeCheck) {
            val content = mStringProvider.getStopLimitOrderWarningDialogText(
                selectedTradeType,
                numberFormatter.formatPrice(bestPrice),
                numberFormatter.formatPrice(stopPrice)
            )

            // Showing the dialog to the user to confirm the order
            showStopLimitOrderWarningDialog(content)

            true
        } else {
            false
        }
    }


    private fun showStopLimitOrderWarningDialog(content: String) {
        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = mStringProvider.getString(R.string.warning),
            content = content,
            negativeBtnText = mStringProvider.getString(R.string.action_cancel),
            positiveBtnText = mStringProvider.getString(R.string.ok),
            positiveBtnClick = {
                onStopLimitDialogPositiveButtonClicked()
            }
        ))
    }


    private fun updateSeekBarsState() {
        if(!mAreWalletsInitialized) {
            return
        }

        mView.setSeekBarsEnabled(!getExpenseWalletForCurrentTradeType().isCurrentBalanceEmpty)
    }


    private fun updateFormViewAfterWalletsRetrieval() {
        when (selectedTradeType) {
            TradeType.BUY -> {
                if (mQuoteCurrencyWallet.currentBalance > 0.0) {
                    var inputView = TradeInputView.AMOUNT
                    var value = selectedAmount
                    if ((selectedAmount * selectedPrice) > mQuoteCurrencyWallet.currentBalance) {
                        inputView = TradeInputView.TOTAL
                        value = mQuoteCurrencyWallet.currentBalance
                    }

                    mView.setInputViewValue(
                        inputView = inputView,
                        value = numberFormatter.formatAmount(value),
                        shouldNotifyListener = true
                    )
                }
            }

            TradeType.SELL -> {
                if (mBaseCurrencyWallet.currentBalance > 0.0) {
                    var value = selectedAmount
                    if (selectedAmount > mBaseCurrencyWallet.currentBalance) {
                        value = mBaseCurrencyWallet.currentBalance
                    }

                    mView.setInputViewValue(
                        inputView = TradeInputView.AMOUNT,
                        value = numberFormatter.formatAmount(value),
                        shouldNotifyListener = true
                    )
                }
            }
        }
    }


    private fun updateSeekBarProgress(seekBarType: TradeSeekBarType, progress: Int) {
        mView.setSeekBarProgress(seekBarType, progress)
        mView.setSeekBarPercentLabelProgress(seekBarType, progress)
    }


    private fun createOrderCreationParams(amount: Double,
                                          price: Double,
                                          stopPrice: Double): OrderCreationParameters {
        val isStopLimitOrder = (selectedOrderType == OrderType.STOP_LIMIT)
        val type = when(selectedTradeType) {
            TradeType.BUY -> if(isStopLimitOrder) {
                ApiOrderCreationType.STOP_LIMIT_BUY
            } else {
                ApiOrderCreationType.BUY
            }

            TradeType.SELL -> if(isStopLimitOrder) {
                ApiOrderCreationType.STOP_LIMIT_SELL
            } else {
                ApiOrderCreationType.SELL
            }
        }

        return OrderCreationParameters(
            currencyPairId = currencyMarket.pairId,
            type = type,
            amount = numberFormatter.toApiDataFormatter { formatAmount(amount) },
            price = numberFormatter.toApiDataFormatter { formatPrice(price) },
            stopPrice = numberFormatter.toApiDataFormatter { formatPrice(stopPrice) }
        )
    }


    private fun resetInputViewsState(orderType: OrderType) {
        mView.setInputViewState(
            inputViews = getTradeInputViewsListForOrderType(orderType),
            state = InputViewState.NORMAL
        )
    }


    private fun fillInputViewValueAfterBalanceClick(inputView: TradeInputView) {
        if(getExpenseWalletForCurrentTradeType().isCurrentBalanceEmpty) {
            return
        }

        val expenseWallet = getExpenseWalletForCurrentTradeType()
        val value = expenseWallet.currentBalance
        val valueStr = numberFormatter.formatAmount(value)
        val seekBarType = when(inputView) {
            TradeInputView.AMOUNT -> TradeSeekBarType.AMOUNT
            TradeInputView.TOTAL -> TradeSeekBarType.TOTAL

            else -> throw IllegalStateException()
        }

        mView.setInputViewValue(inputView, valueStr, true)

        updateSeekBarProgress(
            seekBarType = seekBarType,
            progress = calculateSeekBarProgressForValue(value)
        )
    }


    private fun saveValuableFormDataOfOldTradeType(oldTradeType: TradeType) {
        mTradeTypeValuableFormDataMap[oldTradeType] = ValuableTradeFormData(
            price = mView.getInputViewValue(TradeInputView.PRICE),
            amount = mView.getInputViewValue(TradeInputView.AMOUNT),
            total = mView.getInputViewValue(TradeInputView.TOTAL),
            seekBarProgress = mView.getSeekBarProgress(getSeekBarTypeForTradeType(oldTradeType))
        )
    }


    private fun restoreValuableFormDataOfNewTradeType(newTradeType: TradeType) {
        val data = (mTradeTypeValuableFormDataMap[newTradeType] ?: ValuableTradeFormData.STUB)
        val oldPriceStr = data.price
        val newPriceStr = mView.getInputViewValue(TradeInputView.PRICE)

        mView.setInputViewValue(TradeInputView.AMOUNT, data.amount, false)
        mView.setInputViewValue(TradeInputView.TOTAL, data.total, false)

        if(oldPriceStr != newPriceStr) {
            // If the price was changed, then we need to recalculate the amount
            // or total, thus notifying the listener
            mView.setInputViewValue(TradeInputView.PRICE, newPriceStr)
        } else {
            // Otherwise just updating the seek bar progress
            updateSeekBarProgress(
                seekBarType = getSeekBarTypeForTradeType(newTradeType),
                progress = data.seekBarProgress
            )
        }
    }


    @Suppress("UnnecessaryVariable")
    private fun calculateValueForSeekBarProgress(progress: Int): Double {
        val currentBalance = getExpenseWalletForCurrentTradeType().currentBalance
        val value = ((currentBalance / Constants.TRADING_AMOUNT_SEEK_BAR_MAX_PROGRESS) * progress)

        return value
    }


    private fun calculateSeekBarProgressForValue(value: Double): Int {
        val currentBalance = getExpenseWalletForCurrentTradeType().currentBalance
        val min = Constants.TRADING_AMOUNT_SEEK_BAR_MIN_PROGRESS
        val max = Constants.TRADING_AMOUNT_SEEK_BAR_MAX_PROGRESS

        return if((value <= 0.0) || (currentBalance <= 0.0)) {
            min
        } else {
            ((max * value) / currentBalance).roundToInt().clamp(min, max)
        }
    }


    private fun isInputViewValueValid(inputView: TradeInputView): Boolean {
        val valueStr = mView.getInputViewValue(inputView)
        val value = getInputViewNumberValue(inputView)

        return (valueStr.isNotBlank() && (value > 0.0))
    }


    private fun isSeekBarBeingTracked(seekBarType: TradeSeekBarType): Boolean {
        return (mSeekBarTrackingMap[seekBarType] == true)
    }


    private fun getInputViewNumberValue(inputView: TradeInputView,
                                        defaultValue: Double = 0.0): Double {
        return numberFormatter.parse(
            source = mView.getInputViewValue(inputView).replaceCommaSpaceWithPeriod(),
            default = defaultValue
        )
    }


    private fun getOrderExpense(): Double {
        return getInputViewNumberValue(when(selectedTradeType) {
            TradeType.BUY -> TradeInputView.TOTAL
            TradeType.SELL -> TradeInputView.AMOUNT
        })
    }


    private fun getSeekBarTypeForTradeType(tradeType: TradeType): TradeSeekBarType {
        return when(tradeType) {
            TradeType.BUY -> TradeSeekBarType.TOTAL
            TradeType.SELL -> TradeSeekBarType.AMOUNT
        }
    }


    private fun getUserBalanceData(wallet: Wallet): MapViewData {
        return MapViewData(
            title = mStringProvider.getString(
                R.string.currency_balance_template,
                wallet.currencyName
            ),
            subtitle = numberFormatter.formatBalance(wallet.currentBalance)
        )
    }


    private fun getExpenseWalletForCurrentTradeType(): Wallet {
        return when(selectedTradeType) {
            TradeType.BUY -> mQuoteCurrencyWallet
            TradeType.SELL -> mBaseCurrencyWallet
        }
    }


    private fun getTradeInputViewsListForOrderType(orderType: OrderType): List<TradeInputView> {
        val list = when(orderType) {
            OrderType.LIMIT -> mutableListOf(
                TradeInputView.PRICE
            )

            OrderType.STOP_LIMIT -> mutableListOf(
                TradeInputView.STOP_PRICE,
                TradeInputView.PRICE
            )
        }

        return list.apply {
            add(TradeInputView.AMOUNT)
            add(TradeInputView.TOTAL)
        }
    }


    private fun getWalletIdsList(): List<String> {
        if(!mAreWalletsInitialized) {
            return listOf()
        }

        return mutableListOf<String>().apply {
            if(mBaseCurrencyWallet.hasId) {
                add(mBaseCurrencyWallet.id.toString())
            }

            if(mQuoteCurrencyWallet.hasId) {
                add(mQuoteCurrencyWallet.id.toString())
            }
        }
    }


    override fun onToolbarRightButtonClicked() {
        mModel.toggleFavoriteState(currencyMarket)
    }


    override fun onCurrencyMarketFavorited(currencyMarket: CurrencyMarket) {
        firebaseEventLogger.onMarketPreviewCurrencyMarketFavorited(currencyMarket.pairName)

        mView.updateFavoriteButtonState(true)

        handleCurrencyMarketEvent(
            event = CurrencyMarketEvent.favorite(currencyMarket, this),
            performedActions = mPerformedCurrencyMarketActions
        )
    }


    override fun onCurrencyMarketUnfavorited(currencyMarket: CurrencyMarket) {
        mView.updateFavoriteButtonState(false)

        handleCurrencyMarketEvent(
            event = CurrencyMarketEvent.unfavorite(currencyMarket, this),
            performedActions = mPerformedCurrencyMarketActions
        )
    }


    override fun onToolbarInboxButtonClicked() {
        mView.navigateToInboxScreen()
    }


    override fun onUserBalanceViewClicked(viewId: Int) {
        if(!mAreWalletsInitialized) {
            initializeWallets()
            return
        }

        val isBuyType = (selectedTradeType == TradeType.BUY)
        val isSellType = (selectedTradeType == TradeType.SELL)

        if((viewId == R.id.baseCurrencyUserBalanceVmv) && isSellType) {
            fillInputViewValueAfterBalanceClick(TradeInputView.AMOUNT)
        }

        if((viewId == R.id.quoteCurrencyUserBalanceVmv) && isBuyType) {
            fillInputViewValueAfterBalanceClick(TradeInputView.TOTAL)
        }
    }


    override fun onTradeTypeTabSelected(type: TradeType) {
        val oldTradeType = selectedTradeType

        // Saving the valuable form data of the old trade type
        saveValuableFormDataOfOldTradeType(oldTradeType)

        // Setting the new trade type
        selectedTradeType = type

        // Updating the seek bars visibility
        mView.updateSeekBarsVisibility(type)

        // Updating the seek bars state
        updateSeekBarsState()

        // Restoring the valuable form data of the new trade type
        restoreValuableFormDataOfNewTradeType(type)

        // Resetting the input view state
        resetInputViewsState(selectedOrderType)

        // Updating the trade button
        mView.updateTradeButton(type)
    }


    override fun onOrderTypeSelected(type: OrderType) {
        if(type == OrderType.STOP_LIMIT) {
            firebaseEventLogger.onTradingStopLimitOrderTypeSelected()
        }

        val oldOrderType = selectedOrderType
        selectedOrderType = type

        mView.updateMainContainer {
            // Resetting the input views state on order type change
            resetInputViewsState(oldOrderType)

            // Updating the form
            mView.updateFormViewFields(type)
        }
    }


    override fun onInputViewValueChanged(type: TradeInputView) {
        mTradeFormViewHandler?.onInputViewValueChanged(type)
    }


    override fun onStartedTrackingSeekBar(type: TradeSeekBarType) {
        mSeekBarTrackingMap[type] = true
    }


    override fun onSeekBarProgressChanged(type: TradeSeekBarType, progress: Int,
                                          fromUser: Boolean) {
        mTradeFormViewHandler?.onSeekBarProgressChanged(type, progress, fromUser)
    }


    override fun onStoppedTrackingSeekBar(type: TradeSeekBarType) {
        mSeekBarTrackingMap[type] = false
    }


    override fun onTradeButtonClicked() {
        if(!mAreWalletsInitialized) {
            mView.showToast(mStringProvider.getString(R.string.error_order_creation))
            return
        }

        // Clearing the focus
        mView.clearTradeFormFocus()

        val errorsList = mutableListOf<String>()
        val erroneousInputViews = mutableListOf<TradeInputView>()

        // Checking for emptiness and invalid values
        for(inputView in getTradeInputViewsListForOrderType(selectedOrderType)) {
            if(!isInputViewValueValid(inputView)) {
                errorsList.add(mStringProvider.getTradeInputViewInvalidValueErrorMessage(inputView))
                erroneousInputViews.add(inputView)
            }
        }

        // Checking if form validation is successful and performing
        // min order amount and enough user balance checks
        if(errorsList.isEmpty()) {
            val orderTotal = getInputViewNumberValue(TradeInputView.TOTAL)
            val orderExpense = getOrderExpense()
            val expenseWallet = getExpenseWalletForCurrentTradeType()
            val expenseWalletBalance = expenseWallet.currentBalance

            val isTotalLessThanMin = (currencyMarket.minOrderAmount > orderTotal)
            val notEnoughBalance = (orderExpense > expenseWalletBalance)

            if(isTotalLessThanMin || notEnoughBalance) {
                errorsList.add(when {
                    isTotalLessThanMin  -> mStringProvider.getString(
                        R.string.error_min_total_amount,
                        numberFormatter.formatMinOrderAmount(currencyMarket.minOrderAmount)
                    )
                    notEnoughBalance -> mStringProvider.getString(
                        R.string.error_not_enough_balance,
                        expenseWallet.currencySymbol
                    )

                    else -> throw IllegalStateException()
                })
            }
        }

        // Checking if there are any errors
        if(errorsList.isNotEmpty()) {
            showInvalidDataDialog(errorsList)

            mView.setInputViewState(
                inputViews = erroneousInputViews,
                state = InputViewState.ERRONEOUS
            )

            return
        }

        // Showing the stop-limit order warning dialog if necessary
        if(showStopLimitOrderWarningDialogIfNeeded()) {
            return
        }

        // Creating parameters for an order creation
        mOrderCreationParameters = createOrderCreationParams(
            amount = getInputViewNumberValue(TradeInputView.AMOUNT),
            price = getInputViewNumberValue(TradeInputView.PRICE),
            stopPrice = getInputViewNumberValue(TradeInputView.STOP_PRICE)
        )

        // Sending the request
        mModel.performOrderCreationRequest(mOrderCreationParameters)
    }


    private fun onStopLimitDialogPositiveButtonClicked() {
        if(mOrderCreationParameters.isStub) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        mModel.performOrderCreationRequest(mOrderCreationParameters)
    }


    override fun onRequestSent(requestType: Int) {
        when(requestType) {

            TradeModel.REQUEST_TYPE_WALLETS_RETRIEVAL -> {
                mView.showProgressBar(TradeProgressBarType.WALLETS)
            }

            TradeModel.REQUEST_TYPE_ORDER_CREATION -> {
                mView.showProgressBar(TradeProgressBarType.TOOLBAR)
            }

        }
    }


    override fun onResponseReceived(requestType: Int) {
        when(requestType) {

            TradeModel.REQUEST_TYPE_WALLETS_RETRIEVAL -> {
                mView.hideProgressBar(TradeProgressBarType.WALLETS)
            }

            TradeModel.REQUEST_TYPE_ORDER_CREATION -> {
                mView.hideProgressBar(TradeProgressBarType.TOOLBAR)
            }

        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            TradeModel.REQUEST_TYPE_WALLETS_RETRIEVAL -> {
                onWalletsRetrievalRequestSucceeded(response as List<Wallet>)
            }

            TradeModel.REQUEST_TYPE_ORDERBOOK_RETRIEVAL -> {
                onOrderbookRetrieved(response as Orderbook)
            }

            TradeModel.REQUEST_TYPE_ORDER_CREATION -> {
                onOrderCreationRequestSucceeded()
            }

        }
    }


    private fun onWalletsRetrievalRequestSucceeded(response: List<Wallet>) {
        if(response.size != 2) {
            mView.showUserBalancesErrorViews(mStringProvider.getTradingWalletsErrorCaption())
            mView.showToast(mStringProvider.getString(R.string.error_wallets_retrieval))

            return
        }

        val baseCurrencyWallet = response.first().also { mBaseCurrencyWallet = it }
        val quoteCurrencyWallet = response.last().also { mQuoteCurrencyWallet = it }

        val baseCurrencyData = getUserBalanceData(baseCurrencyWallet)
        val quoteCurrencyData = getUserBalanceData(quoteCurrencyWallet)

        mAreWalletsInitialized = true

        mView.setUserBalancesData(baseCurrencyData, quoteCurrencyData)
        mView.showUserBalancesMainViews(true)

        startListeningToWalletsBalancesUpdates()
        updateSeekBarsState()
        updateFormViewAfterWalletsRetrieval()

        if(!connectionProvider.isNetworkAvailable()) {
            mView.showToast(mStringProvider.getInternetConnectionCheckMessage())
        }
    }


    private fun onOrderbookRetrieved(orderbook: Orderbook) {
        mIsInitialPriceSet = true

        if(orderbook.isEmpty) {
            return
        }

        val initialPrice = when(selectedTradeType) {
            TradeType.BUY -> orderbook.sellOrders.firstOrNull()?.price
            TradeType.SELL -> orderbook.buyOrders.firstOrNull()?.price
        }

        if(initialPrice != null) {
            selectedPrice = initialPrice

            mView.setSelectedPrice(initialPrice)
        }
    }


    private fun onOrderCreationRequestSucceeded() {
        mView.showToast(mStringProvider.getString(R.string.order_created))

        postEventForReload()
        onBackPressed()
    }


    private fun postEventForReload() {
        mView.postActionDelayed(1000) {
            loadWallets()
        }

        reloadProvider.setWallets(true)
        reloadProvider.setActiveOrders(true)
        reloadProvider.setWalletsSearch(true)
        mView.postActionDelayed(1800) {
            if (EventBus.getDefault().hasSubscriberForEvent(ReloadWalletsEvent::class.java)) {
                EventBus.getDefault().post(ReloadWalletsEvent.reloadWallets(this))
            }
            if (EventBus.getDefault().hasSubscriberForEvent(ReloadOrdersEvent::class.java)) {
                EventBus.getDefault().post(ReloadOrdersEvent.reloadOrders(this))
            }
        }
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when(requestType) {

            TradeModel.REQUEST_TYPE_WALLETS_RETRIEVAL -> {
                onWalletsRetrievalRequestFailed(exception)
            }

            TradeModel.REQUEST_TYPE_ORDERBOOK_RETRIEVAL -> {
                onOrderbookRetrievalFailed()
            }

            TradeModel.REQUEST_TYPE_ORDER_CREATION -> {
                onOrderCreationRequestFailed(exception)
            }

        }
    }


    private fun onWalletsRetrievalRequestFailed(exception: Throwable) {
        mView.showUserBalancesErrorViews(mStringProvider.getTradingWalletsErrorCaption())

        val isNotFoundError = (
            ((exception is WalletException) &&
            (exception.error == WalletException.Error.WALLET_NOT_FOUND)) ||
            (exception is NotFoundException)
        )

        if(isNotFoundError && !connectionProvider.isNetworkAvailable()) {
            mView.showToast(mStringProvider.getInternetConnectionCheckMessage())
        } else {
            mView.showToast(mStringProvider.getErrorMessage(exception))
        }
    }


    private fun onOrderbookRetrievalFailed() {
        mIsInitialPriceSet = true
    }


    private fun onOrderCreationRequestFailed(exception: Throwable) {
        when(exception) {
            is OrderCreationException -> showErrorDialog(when(exception.error) {
                OrderCreationException.Error.AMOUNT_TOO_SMALL -> mStringProvider.getString(
                    R.string.error_amount_too_small
                )

                OrderCreationException.Error.MAX_NUM_OF_OPEN_ORDERS -> mStringProvider.getString(
                    R.string.error_pair_open_orders_limit_reached
                )

                OrderCreationException.Error.UNKNOWN -> exception.message.appendExclamationMark()
            })

            else -> mView.showToast(mStringProvider.getErrorMessage(exception))
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CurrencyMarketEvent) {
        if(event.isOriginatedFrom(this)) {
            return
        }

        if(event.action == CurrencyMarketEvent.Action.UPDATED) {
            val attachment = event.attachment

            if((currencyMarket.pairId == attachment.pairId) &&
                (currencyMarket.lastPrice != attachment.lastPrice)) {
                currencyMarket = attachment

                mView.setPriceInfoViewData(attachment, true)
            }

            mPerformedCurrencyMarketActions.addUpdatedCurrencyMarket(attachment)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: WalletEvent) {
        if(event.isOriginatedFrom(this) ||
            !mAreWalletsInitialized ||
            mModel.isRequestSent(TradeModel.REQUEST_TYPE_WALLETS_RETRIEVAL)) {
            return
        }

        when(event.action) {

            WalletEvent.Action.BALANCE_UPDATED -> {
                if((mBaseCurrencyWallet.id != event.attachment.id) &&
                    (mQuoteCurrencyWallet.id != event.attachment.id)) {
                    return
                }

                val newWallet = event.attachment
                val updatedUserBalanceData = getUserBalanceData(newWallet)

                if(mBaseCurrencyWallet.id == newWallet.id) {
                    mBaseCurrencyWallet = newWallet
                    mView.updateBaseCurrencyUserBalanceData(updatedUserBalanceData)
                } else {
                    mQuoteCurrencyWallet = newWallet
                    mView.updateQuoteCurrencyUserBalanceData(updatedUserBalanceData)
                }
            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: OrderEvent) {
        if(event.isOriginatedFrom(this)) {
            return
        }

        handleOrderEvent(
            event = event,
            performedActions = mPerformedOrderActions
        )
    }


    override fun onNavigateUpPressed(): Boolean {
        if(!mPerformedCurrencyMarketActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedCurrencyMarketActionsEvent.init(
                mPerformedCurrencyMarketActions, this
            ))
        }

        if(!mPerformedOrderActions.isEmpty() &&
            (tradeScreenOpener != TradeScreenOpener.CURRENCY_MARKETS)) {
            EventBus.getDefault().postSticky(PerformedOrderActionsEvent.init(
                mPerformedOrderActions, this
            ))
        }

        return super.onNavigateUpPressed()
    }


    override fun onBackPressed(): Boolean = onNavigateUpPressed()


    private val mHandlerListener = object : TradeFormViewHandler.Listener {


        override fun updateSeekBarProgress(seekBarType: TradeSeekBarType, progress: Int) {
            this@TradePresenter.updateSeekBarProgress(seekBarType, progress)
        }


        override fun clearInputViewText(inputView: TradeInputView,
                                        shouldNotifyListener: Boolean) {
            mView.clearInputViewText(inputView, shouldNotifyListener)
        }


        override fun setSeekBarPercentLabelProgress(type: TradeSeekBarType, progress: Int) {
            mView.setSeekBarPercentLabelProgress(type, progress)
        }


        override fun setTradeFormStopPriceLabelText(stopPrice: Double) {
            mView.setTradeFormStopPriceLabelText(stopPrice)
        }


        override fun setTradeFormPriceLabelText(price: Double) {
            mView.setTradeFormPriceLabelText(price)
        }


        override fun setInputViewValue(inputView: TradeInputView, value: String,
                                       shouldNotifyListener: Boolean) {
            mView.setInputViewValue(inputView, value, shouldNotifyListener)
        }


        override fun calculateValueForSeekBarProgress(progress: Int): Double {
            return this@TradePresenter.calculateValueForSeekBarProgress(progress)
        }


        override fun calculateSeekBarProgressForValue(value: Double): Int {
            return this@TradePresenter.calculateSeekBarProgressForValue(value)
        }


        override fun isInputViewValueValid(inputView: TradeInputView): Boolean {
            return this@TradePresenter.isInputViewValueValid(inputView)
        }


        override fun isSeekBarBeingTracked(seekBarType: TradeSeekBarType): Boolean {
            return this@TradePresenter.isSeekBarBeingTracked(seekBarType)
        }


        override fun areWalletsInitialized(): Boolean = mAreWalletsInitialized


        override fun getInputViewNumberValue(inputView: TradeInputView,
                                             defaultValue: Double): Double {
            return this@TradePresenter.getInputViewNumberValue(inputView, defaultValue)
        }


        override fun getOrderType(): OrderType = selectedOrderType


        override fun getTradeType(): TradeType = selectedTradeType


        override fun getExpenseWalletForCurrentTradeType(): Wallet {
            return this@TradePresenter.getExpenseWalletForCurrentTradeType()
        }


    }


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            mAreWalletsInitialized = it.areWalletsInitialized
            mIsInitialPriceSet = it.isInitialPriceSet
            selectedPrice = it.selectedPrice
            tradeScreenOpener = it.tradeScreenOpener
            selectedTradeType = it.selectedTradeType
            selectedOrderType = it.selectedOrderType
            currencyMarket = it.currencyMarket
            mBaseCurrencyWallet = it.baseCurrencyWallet
            mQuoteCurrencyWallet = it.quoteCurrencyWallet
            mPerformedCurrencyMarketActions = it.performedCurrencyMarketActions
            mPerformedOrderActions = it.performedOrderActions
            mTradeTypeValuableFormDataMap = it.tradeTypeValuableFormDataMap
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            areWalletsInitialized = mAreWalletsInitialized,
            isInitialPriceSet = mIsInitialPriceSet,
            selectedPrice = selectedPrice,
            selectedAmount = selectedAmount,
            tradeScreenOpener = tradeScreenOpener,
            selectedTradeType = selectedTradeType,
            selectedOrderType = selectedOrderType,
            currencyMarket = currencyMarket,
            baseCurrencyWallet = mBaseCurrencyWallet,
            quoteCurrencyWallet = mQuoteCurrencyWallet,
            performedCurrencyMarketActions = mPerformedCurrencyMarketActions,
            performedOrderActions = mPerformedOrderActions,
            tradeTypeValuableFormDataMap = mTradeTypeValuableFormDataMap
        ))
    }


    override fun onRecycle() {
        super.onRecycle()

        stopListeningToWalletsBalancesUpdates()
    }


    override fun toString(): String {
        return "${super.toString()}_${currencyMarket.pairName}"
    }


}