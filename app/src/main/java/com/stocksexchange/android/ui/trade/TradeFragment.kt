package com.stocksexchange.android.ui.trade

import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.views.SwitchOptionsView
import com.stocksexchange.android.ui.views.mapviews.VerticalMapView
import com.stocksexchange.android.ui.views.mapviews.data.MapViewData
import com.stocksexchange.android.utils.extensions.*
import com.stocksexchange.android.utils.handlers.FiatCurrencyPriceHandler
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.listeners.adapters.OnSeekBarChangeListenerAdapter
import com.stocksexchange.core.utils.listeners.adapters.OnTabSelectedListenerAdapter
import com.stocksexchange.core.utils.listeners.adapters.TextWatcherAdapter
import kotlinx.android.synthetic.main.trade_fragment_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class TradeFragment : BaseFragment<TradePresenter>(), TradeContract.View {


    companion object {}


    override val mPresenter: TradePresenter by inject { parametersOf(this) }

    private val mPreferenceHandler: PreferenceHandler by inject()
    private val mFiatCurrencyPriceHandler: FiatCurrencyPriceHandler by inject()

    private val mNumberFormatter: NumberFormatter by inject()

    private var mUserBalanceViewsArray: Array<VerticalMapView> = arrayOf()




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.selectedPrice = it.selectedPrice
            mPresenter.tradeScreenOpener = it.tradeScreenOpener
            mPresenter.selectedTradeType = it.selectedTradeType
            mPresenter.selectedOrderType = it.selectedOrderType
            mPresenter.currencyMarket = it.currencyMarket
            mPresenter.selectedAmount = it.selectedAmount
        }
    }


    override fun init() {
        super.init()

        val currencyMarket = mPresenter.currencyMarket

        initContentContainer()
        initToolbar(currencyMarket)
        initPriceInfoView(currencyMarket)
        initUserBalanceContainer()
        initTradeTypesTabLayout()
        initOrderTypesSwitch()
        initFormView()
        initTradeButton()
    }


    private fun initContentContainer() = with(mRootView.scrollView) {
        ThemingUtil.Trade.contentContainer(this, getAppTheme())
    }


    private fun initToolbar(currencyMarket: CurrencyMarket) = with(mRootView.toolbar) {
        setTitleText(String.format(
            "%s / %s",
            currencyMarket.baseCurrencySymbol,
            currencyMarket.quoteCurrencySymbol
        ))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }
        setOnRightButtonClickListener {
            mPresenter.onToolbarRightButtonClicked()
        }

        if (mSessionManager.isUserSignedIn() && Constants.IMPLEMENTATION_NOTIFICATION_TURN_ON) {
            setOnInboxButtonClickListener {
                mPresenter.onToolbarInboxButtonClicked()
            }

            updateInboxButtonItemCount()
        } else {
            hideInboxButton()
        }

        ThemingUtil.Trade.toolbar(this, getAppTheme())

        initToolbarFavoriteButton(currencyMarket)
    }


    private fun initToolbarFavoriteButton(currencyMarket: CurrencyMarket) {
        mCoroutineHandler.createBgLaunchCoroutine {
            val isFavorite = get<CurrencyMarketsRepository>().isCurrencyMarketFavorite(currencyMarket)

            withContext(Dispatchers.Main) {
                updateFavoriteButtonState(isFavorite)
            }
        }
    }


    private fun initPriceInfoView(currencyMarket: CurrencyMarket) = with(mRootView.priceInfoView) {
        setPriceInfoViewData(currencyMarket, false)

        ThemingUtil.Trade.priceInfoView(this, getAppTheme())
    }


    private fun initUserBalanceContainer() {
        mUserBalanceViewsArray = arrayOf(
            mRootView.baseCurrencyUserBalanceVmv,
            mRootView.quoteCurrencyUserBalanceVmv
        )

        mUserBalanceViewsArray.forEach { view ->
            view.setOnClickListener {
                mPresenter.onUserBalanceViewClicked(view.id)
            }

            ThemingUtil.Trade.userBalanceVerticalMapView(view, getAppTheme())
        }
    }


    private fun initTradeTypesTabLayout() = with(mRootView.tradeTypesTl) {
        addTab(newTab(), TradeType.BUY.ordinal)
        addTab(newTab(), TradeType.SELL.ordinal)

        getTabAt(mPresenter.selectedTradeType.ordinal)?.select()
        addOnTabSelectedListener(object : OnTabSelectedListenerAdapter {

            override fun onTabSelected(tab: TabLayout.Tab) {
                mPresenter.onTradeTypeTabSelected(when(tab.position) {
                    TradeType.BUY.ordinal -> TradeType.BUY
                    TradeType.SELL.ordinal -> TradeType.SELL

                    else -> throw IllegalStateException()
                })
            }

        })

        getTabAt(TradeType.BUY.ordinal)?.text = getStr(R.string.action_buy)
        getTabAt(TradeType.SELL.ordinal)?.text = getStr(R.string.action_sell)

        ThemingUtil.Trade.tabLayout(this, getAppTheme())
    }


    private fun initOrderTypesSwitch() = with(mRootView.orderTypesSov) {
        setLeftOptionTitleText(getStr(R.string.limit))
        setRightOptionTitleText(getStr(R.string.stop_limit))

        onOptionChangeListener = object : SwitchOptionsView.OnOptionSelectionListener {

            override fun onLeftOptionSelected() {
                mPresenter.onOrderTypeSelected(OrderType.LIMIT)
            }

            override fun onRightOptionSelected() {
                mPresenter.onOrderTypeSelected(OrderType.STOP_LIMIT)
            }

        }

        when(mPresenter.selectedOrderType) {
            OrderType.LIMIT -> setLeftOptionSelected(false)
            OrderType.STOP_LIMIT -> setRightOptionSelected(false)
        }

        ThemingUtil.Trade.orderTypesSwitch(this, getAppTheme())
    }


    private fun initFormView() = with(mRootView.formView) {
        updateFormViewFields(mPresenter.selectedOrderType)
        updateSeekBarsVisibility(mPresenter.selectedTradeType)

        val inputViewType = ctx.getKeyboardNumericInputType()
        val keyListener = ctx.getKeyboardNumericKeyListener()
        val selectedPrice = mPresenter.selectedPrice
        val currencyMarket = mPresenter.currencyMarket


        setSeekBarEnabled(false)
        setInputViewType(inputViewType, false)
        setInputViewLabelText(TradeInputView.STOP_PRICE, currencyMarket.quoteCurrencySymbol)
        setInputViewLabelText(TradeInputView.PRICE, currencyMarket.quoteCurrencySymbol)
        setInputViewLabelText(TradeInputView.AMOUNT, currencyMarket.baseCurrencySymbol)
        setInputViewLabelText(TradeInputView.TOTAL, currencyMarket.quoteCurrencySymbol)
        setSeekBarPercentLabelText(getSeekBarPercentLabelText(0))

        for(tradeSeekBarType in TradeSeekBarType.values()) {
            setSeekBarChangeListener(tradeSeekBarType, OnSeekBarChangeListenerImpl(tradeSeekBarType))
        }

        for(tradeInputView in TradeInputView.values()) {
            addInputViewTextWatcher(tradeInputView, TradeFormViewTextWatcherImpl(tradeInputView))
        }

        if(selectedPrice == 0.0) {
            setTradeFormStopPriceLabelText(-1.0)
            setTradeFormPriceLabelText(-1.0)
        } else {
            setSelectedPrice(selectedPrice)
        }

        if(keyListener != null) {
            setInputViewKeyListener(keyListener)
        }

        ThemingUtil.Trade.formView(this, getAppTheme())
    }


    private fun initTradeButton() = with(mRootView.tradeBtn) {
        setOnClickListener {
            mPresenter.onTradeButtonClicked()
        }

        updateTradeButton(mPresenter.selectedTradeType)
    }


    override fun showProgressBar(type: TradeProgressBarType) {
        when(type) {
            TradeProgressBarType.TOOLBAR -> {
                mRootView.toolbar.hideRightButton()
                mRootView.toolbar.showProgressBar()
            }

            TradeProgressBarType.WALLETS -> {
                mUserBalanceViewsArray.forEach {
                    it.showProgressBar()
                }
            }
        }
    }


    override fun hideProgressBar(type: TradeProgressBarType) {
        when(type) {
            TradeProgressBarType.TOOLBAR -> {
                mRootView.toolbar.hideProgressBar()
                mRootView.toolbar.showRightButton()
            }

            TradeProgressBarType.WALLETS -> {
                mUserBalanceViewsArray.forEach {
                    it.hideProgressBar()
                }
            }
        }
    }


    override fun showUserBalancesMainViews(shouldAnimate: Boolean) {
        mUserBalanceViewsArray.forEach {
            it.showMainView(shouldAnimate)
        }
    }


    override fun hideUserBalancesMainViews() {
        mUserBalanceViewsArray.forEach {
            it.hideMainView()
        }
    }


    override fun showUserBalancesErrorViews(caption: String) {
        mUserBalanceViewsArray.forEach {
            it.showErrorView(caption)
        }
    }


    override fun hideUserBalancesErrorViews() {
        mUserBalanceViewsArray.forEach {
            it.hideInfoView()
        }
    }


    override fun updateFavoriteButtonState(isFavorite: Boolean) {
        with(ThemingUtil.Trade) {
            val rightButtonIv: ImageView = mRootView.toolbar.getRightButtonIv()

            if(isFavorite) {
                favoriteButton(rightButtonIv)
            } else {
                unfavoriteButton(rightButtonIv, getAppTheme())
            }
        }
    }


    override fun updateInboxButtonItemCount() {
        mRootView.toolbar.setInboxButtonCountMessage(mPreferenceHandler.getInboxUnreadCount())
    }


    override fun updateFormViewFields(newOrderType: OrderType) = with(mRootView.formView) {
        when(newOrderType) {
            OrderType.LIMIT -> showViewsForLimitOrder()
            OrderType.STOP_LIMIT -> showViewsForStopLimitOrder()
        }
    }


    override fun updateSeekBarsVisibility(newTradeType: TradeType) = with(mRootView.formView) {
        when(newTradeType) {
            TradeType.BUY -> {
                showSeekBar(TradeSeekBarType.TOTAL)
                hideSeekBar(TradeSeekBarType.AMOUNT)
            }

            TradeType.SELL -> {
                showSeekBar(TradeSeekBarType.AMOUNT)
                hideSeekBar(TradeSeekBarType.TOTAL)
            }
        }
    }


    override fun updateTradeButton(newTradeType: TradeType) {
        mRootView.tradeBtn.text = getTradeButtonText(newTradeType)

        with(ThemingUtil.Trade) {
            when(newTradeType) {
                TradeType.BUY -> buyButton(mRootView.tradeBtn, getAppTheme())
                TradeType.SELL -> sellButton(mRootView.tradeBtn, getAppTheme())
            }
        }
    }


    override fun updateMainContainer(block: (() -> Unit)) {
        mRootView.mainContainerRl.crossFadeItself {
            block()
        }
    }


    override fun updateBaseCurrencyUserBalanceData(data: MapViewData) {
        mRootView.baseCurrencyUserBalanceVmv.updateData(data)
    }


    override fun updateQuoteCurrencyUserBalanceData(data: MapViewData) {
        mRootView.quoteCurrencyUserBalanceVmv.updateData(data)
    }


    override fun clearTradeFormFocus() {
        mRootView.formView.clearFocus()
    }


    override fun clearInputViewText(inputView: TradeInputView,
                                    shouldNotifyListener: Boolean) {
        mRootView.formView.clearInputViewText(inputView, shouldNotifyListener)
    }


    override fun navigateToInboxScreen() {
        navigate(R.id.inboxDest)
    }


    override fun setSeekBarsEnabled(areEnabled: Boolean) {
        mRootView.formView.setSeekBarEnabled(areEnabled)
    }


    override fun setSeekBarProgress(type: TradeSeekBarType, progress: Int) {
        mRootView.formView.setSeekBarProgress(type, progress)
    }


    override fun setSeekBarPercentLabelProgress(type: TradeSeekBarType, progress: Int) {
        mRootView.formView.setSeekBarPercentLabelText(type, getSeekBarPercentLabelText(progress))
    }


    override fun setSelectedPrice(selectedPrice: Double) {
        val selectedPriceString = convertSelectedPriceToString(selectedPrice)

        setTradeFormStopPriceLabelText(selectedPrice)
        setTradeFormPriceLabelText(selectedPrice)

        with(mRootView.formView) {
            setInputViewText(TradeInputView.STOP_PRICE, selectedPriceString, false)
            setInputViewText(TradeInputView.PRICE, selectedPriceString, false)
        }
    }


    override fun setTradeFormStopPriceLabelText(stopPrice: Double) {
        mRootView.formView.setStopPriceLabelText(getTradeFormLabelText(
            label = getStr(R.string.trade_form_view_stop_price_label),
            price = stopPrice
        ))
    }


    override fun setTradeFormPriceLabelText(price: Double) {
        mRootView.formView.setPriceLabelText(getTradeFormLabelText(
            label = getStr(R.string.trade_form_view_price_label),
            price = price
        ))
    }


    override fun setPriceInfoViewData(currencyMarket: CurrencyMarket, animate: Boolean) {
        val performDataBinding = {
            mRootView.priceInfoView.setData(
                currencyMarket = currencyMarket,
                formatter = mNumberFormatter,
                currentFiatCurrency = getSettings().fiatCurrency,
                fiatCurrencyPriceHandler = mFiatCurrencyPriceHandler
            )
        }

        if(animate) {
            mRootView.priceInfoView.crossFadeItself {
                performDataBinding()
            }
        } else {
            performDataBinding()
        }
    }


    override fun setInputViewState(inputViews: List<TradeInputView>, state: InputViewState) {
        for(inputView in inputViews) {
            mRootView.formView.setInputViewState(inputView, state)
        }
    }


    override fun setInputViewValue(inputView: TradeInputView, value: String,
                                   shouldNotifyListener: Boolean) {
        mRootView.formView.setInputViewText(inputView, value.replace(" ", ""), shouldNotifyListener)
    }


    override fun setUserBalancesData(baseCurrencyData: MapViewData,
                                     quoteCurrencyData: MapViewData) {
        mRootView.baseCurrencyUserBalanceVmv.setData(baseCurrencyData, true)
        mRootView.quoteCurrencyUserBalanceVmv.setData(quoteCurrencyData, true)
    }


    override fun postActionDelayed(delay: Long, action: () -> Unit) {
        mRootView.postActionDelayed(delay, action)
    }


    private fun convertSelectedPriceToString(price: Double): String {
        return mNumberFormatter.formatTradeFormPrice(
            value = price,
            maxLength = resources.getInteger(R.integer.trade_form_view_price_max_chars_length)
        )
    }


    override fun getContentLayoutResourceId(): Int = R.layout.trade_fragment_layout


    override fun getSeekBarProgress(type: TradeSeekBarType): Int {
        return mRootView.formView.getSeekBarProgress(type)
    }


    private fun getSeekBarPercentLabelText(progress: Int): String = "$progress%"


    private fun getTradeButtonText(tradeType: TradeType): String {
        val currencyMarket = mPresenter.currencyMarket

        return mStringProvider.getString(
            when(tradeType) {
                TradeType.BUY -> R.string.trade_fragment_buy_button_template
                TradeType.SELL -> R.string.trade_fragment_sell_button_template
            },
            currencyMarket.baseCurrencySymbol,
            currencyMarket.quoteCurrencySymbol
        )
    }


    private fun getTradeFormLabelText(label: String, price: Double): String {
        return StringBuilder().apply {
            append(label)

/*            if(price != -1.0) {
                val priceInFiatCurrency = mFiatCurrencyPriceHandler.getTradeFormLastPriceInFiatCurrency(
                    price = price,
                    fiatCurrency = getSettings().fiatCurrency,
                    fiatCurrencyRates = mCurrencyMarket.fiatCurrencyRates
                )

                if(priceInFiatCurrency != null) {
                    append(" (")
                    append(priceInFiatCurrency)
                    append(")")
                }
            }*/
        }.toString()
    }


    override fun getInputViewValue(inputView: TradeInputView): String {
        return mRootView.formView.getInputViewText(inputView)
    }


    private inner class OnSeekBarChangeListenerImpl(val type: TradeSeekBarType): OnSeekBarChangeListenerAdapter {

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            mPresenter.onStartedTrackingSeekBar(type)
        }

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            mPresenter.onSeekBarProgressChanged(type, progress, fromUser)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            mPresenter.onStoppedTrackingSeekBar(type)
        }

    }


    private inner class TradeFormViewTextWatcherImpl(val type: TradeInputView) : TextWatcherAdapter {

        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            mPresenter.onInputViewValueChanged(type)
        }

    }


}