package com.stocksexchange.android.ui.alertprice.additem

import android.os.Bundle
import android.text.TextWatcher
import android.view.WindowManager.LayoutParams.*
import android.widget.ImageView
import androidx.core.view.isVisible
import com.stocksexchange.android.R
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.views.AlertPriceItemView
import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.api.model.rest.AlertPriceComparison
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.managers.KeyboardManager
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.listeners.adapters.TextWatcherAdapter
import kotlinx.android.synthetic.main.alert_price_add_fragment_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.android.ext.android.get

class AlertPriceAddAFragment : BaseFragment<AlertPriceAddPresenter>(), AlertPriceAddContract.View {


    companion object {

        private const val KEYBOARD_SHOWING_DELAY = 150L

    }


    override val mPresenter: AlertPriceAddPresenter by inject { parametersOf(this) }

    private val mNumberFormatter: NumberFormatter by inject()
    private val mKeyboardManager: KeyboardManager by inject()

    private var mLessAlertPriceItem: AlertPrice? = null
    private var mMoreAlertPriceItem: AlertPrice? = null




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.currencyMarket = it.currencyMarket
        }
    }


    override fun preInit() {
        super.preInit()

        setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)
    }


    override fun init() {
        super.init()

        val currencyMarket = mPresenter.currencyMarket

        initContentContainer()
        initCurrentPriceInfo(currencyMarket)
        initInputPriceContainer(currencyMarket)
        initPairName(currencyMarket)
        initClearButton()
        initMoreLessItems(currencyMarket)
        initAcceptButton()
        initToolbar(currencyMarket)
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.AlertPriceAdd.contentContainer(this, getAppTheme())
    }


    private fun initCurrentPriceInfo(currencyMarket: CurrencyMarket) = with(mRootView.lastPriceTv) {
        text = getPriceFormattedString(currencyMarket.lastPrice)

        ThemingUtil.AlertPriceAdd.setTitleTextColor(this, getAppTheme())
    }


    private fun initInputPriceContainer(currencyMarket: CurrencyMarket) {
        mRootView.priceAlertEt.addTextChangedListener(mPriceTextWatcher)
        setTextToInputField(currencyMarket.lastPrice)

        mRootView.currentPriceTv.text = mStringProvider.getString(R.string.alert_price_current_price)

        ThemingUtil.AlertPriceAdd.setTitleTextColor(mRootView.currentPriceTv, getAppTheme())
    }

    private fun initPairName(currencyMarket: CurrencyMarket) = with(mRootView.pairNameTv) {
        text = String.format(
            "%s / %s",
            currencyMarket.baseCurrencySymbol,
            currencyMarket.quoteCurrencySymbol
        )

        ThemingUtil.AlertPriceAdd.setTitleTextColor(this, getAppTheme())
    }


    private fun initClearButton() {
        mRootView.clearAlertTv.text = mStringProvider.getString(R.string.alert_price_clear)

        mRootView.clearButtonRl.setOnClickListener {
            mPresenter.onClickClearButton()
        }
    }


    private fun initMoreLessItems(currencyMarket: CurrencyMarket) {
        mPresenter.onShowMoreLessItems(currencyMarket.pairId)

        mRootView.lessAlertPriceApiv.setOnDeleteButtonClickListener {
            mPresenter.onClickDeleteLessItem()
        }

        mRootView.moreAlertPriceApiv.setOnDeleteButtonClickListener {
            mPresenter.onClickDeleteMoreItem()
        }


        with(ThemingUtil.AlertPrice.AlertPriceItem) {
            setTitleTextColor(mRootView.moreAlertPriceApiv.getTitleTextView(), getNotActiveTextColor())
            setTitleTextColor(mRootView.lessAlertPriceApiv.getTitleTextView(), getNotActiveTextColor())
        }
    }


    private fun initAcceptButton() {
        mRootView.createAlertTv.text = mStringProvider.getString(R.string.alert_price_create)

        mRootView.createAlertButtonRl.setOnClickListener {
            mPresenter.onClickCreateAlertPrice()
        }
    }


    private fun initToolbar(currencyMarket: CurrencyMarket) {
        with(mRootView.toolbar) {
            setTitleText(getStr(R.string.alert_price_short_title))

            setOnLeftButtonClickListener {
                mPresenter.onNavigateUpPressed()
            }

            setOnRightButtonClickListener {
                mPresenter.onRightButtonClicked()
            }

            ThemingUtil.AlertPriceAdd.toolbar(this, getAppTheme())
        }

        initToolbarFavoriteButton(currencyMarket)
        updateMoreLessView()
    }


    private fun initToolbarFavoriteButton(currencyMarket: CurrencyMarket) {
        mCoroutineHandler.createBgLaunchCoroutine {
            val isFavorite = get<CurrencyMarketsRepository>().isCurrencyMarketFavorite(currencyMarket)

            withContext(Dispatchers.Main) {
                updateFavoriteButtonState(isFavorite)
            }
        }
    }


    override fun showKeyboard() {
        mRootView.priceAlertEt.requestFocus()
        mRootView.priceAlertEt.postDelayed(
            { mKeyboardManager.showKeyboard(mRootView.priceAlertEt) },
            KEYBOARD_SHOWING_DELAY
        )
    }


    override fun hideKeyboard() {
        mRootView.priceAlertEt.clearFocus()
        mKeyboardManager.hideKeyboard(mRootView.priceAlertEt)
    }


    override fun showMoreLessItems(alertPriceItems: List<AlertPrice>) {
        alertPriceItems.forEach {
            when (it.comparisonType) {
                AlertPriceComparison.LESS.title -> {
                    mLessAlertPriceItem = it
                    setDataToMoreLessItem(it, mRootView.lessAlertPriceApiv)
                }

                AlertPriceComparison.GREATER.title -> {
                    mMoreAlertPriceItem = it
                    setDataToMoreLessItem(it, mRootView.moreAlertPriceApiv)
                }
            }
        }

        updateMoreLessView()
    }


    private fun setDataToMoreLessItem(alertPrice: AlertPrice, alertPriceItemView: AlertPriceItemView) {
        alertPriceItemView.setPriceText(
            getPriceFormattedString(alertPrice.price)
        )

        alertPriceItemView.setData(alertPrice.comparisonType, alertPrice.active)

        if (!alertPrice.active) {
            ThemingUtil.AlertPrice.AlertPriceItem.setTitleTextColor(
                alertPriceItemView.getPriceTextView(),
                getNotActiveTextColor()
            )
        }
    }


    private fun getNotActiveTextColor(): Int {
        return getAppTheme().generalTheme.secondaryTextColor
    }


    override fun deleteAlertPriceItem(id: Int) {
        mLessAlertPriceItem?.let {
            if (it.id == id) {
                mLessAlertPriceItem = null
            }
        }
        mMoreAlertPriceItem?.let {
            if (it.id == id) {
                mMoreAlertPriceItem = null
            }
        }

        updateMoreLessView()
    }


    private fun updateMoreLessView() {
        mRootView.lineSeparatorBottomV.isVisible = (
            (mLessAlertPriceItem != null) && (mMoreAlertPriceItem != null)
        )
        mRootView.lessAlertPriceApiv.isVisible = (mLessAlertPriceItem != null)
        mRootView.moreAlertPriceApiv.isVisible = (mMoreAlertPriceItem != null)
    }


    private fun onInputFieldTextChanged() {
        val price: Double
        try {
            price = mRootView.priceAlertEt.text.toString().toDouble()
        } catch (e: Exception) {
            return
        }

        val currencyMarket = mPresenter.currencyMarket

        mRootView.priceArrowDownIv.isVisible = (price <= currencyMarket.lastPrice)
        mRootView.priceArrowUpIv.isVisible = (price > currencyMarket.lastPrice)
    }


    override fun setTextToInputField(price: Double) {
        val priceForEt = mNumberFormatter.formatInputAlertPrice(price)

        mRootView.priceAlertEt.setText(priceForEt)
        mRootView.priceAlertEt.setSelection(priceForEt.length)
    }


    override fun updateLastPrice(lastPrice: Double) {
        val lastPriceString = getPriceFormattedString(lastPrice)

        mRootView.lastPriceTv.crossFadeItself {
            mRootView.lastPriceTv.text = lastPriceString
            onInputFieldTextChanged()
        }
    }


    override fun updateFavoriteButtonState(isFavorite: Boolean) {
        with(ThemingUtil.CurrencyMarketPreview) {
            val rightButtonIv: ImageView = mRootView.toolbar.getRightButtonIv()

            if(isFavorite) {
                favoriteButton(rightButtonIv)
            } else {
                unfavoriteButton(rightButtonIv, getAppTheme())
            }
        }
    }


    override fun showToolbarProgressBar() {
        mRootView.toolbar.invisibleAlertPriceButton()
        mRootView.toolbar.showProgressBar()
    }


    override fun hideToolbarProgressBar() {
        mRootView.toolbar.hideProgressBar()
        mRootView.toolbar.showAlertPriceButton()
    }


    override fun getLessAlertPriceItem(): AlertPrice? = mLessAlertPriceItem


    override fun getMoreAlertPriceItem(): AlertPrice? = mMoreAlertPriceItem


    override fun getInputText(): String {
        return mRootView.priceAlertEt.text.toString()
    }


    override fun getContentLayoutResourceId() = R.layout.alert_price_add_fragment_layout


    override fun canReceiveRealTimeDataUpdateEvent(): Boolean = true


    override fun getPriceFormattedString(price: Double): String {
        return mNumberFormatter.formatFixedPrice(price)
    }


    private val mPriceTextWatcher: TextWatcher = object : TextWatcherAdapter {
        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            when {
                text.isEmpty() -> setTextToInputField(0.0)
                text.length == 2 -> correctInputText(text)
                else -> onInputFieldTextChanged()
            }
        }
    }


    private fun correctInputText(text: CharSequence) {
        val price = when (text.toString()) {
            "01" -> 1.0
            "02" -> 2.0
            "03" -> 3.0
            "04" -> 4.0
            "05" -> 5.0
            "06" -> 6.0
            "07" -> 7.0
            "08" -> 8.0
            "09" -> 9.0
            "00" -> 0.0
            else -> -1.0
        }

        if (price >= 0) {
            setTextToInputField(price)
        } else {
            onInputFieldTextChanged()
        }
    }


    override fun onRecycle() {
        super.onRecycle()

        setSoftInputMode(SOFT_INPUT_STATE_UNSPECIFIED)
    }


}