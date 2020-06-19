package com.stocksexchange.android.ui.trade

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.android.ui.views.mapviews.data.MapViewData

interface TradeContract {


    interface View : BaseView {

        fun showProgressBar(type: TradeProgressBarType)

        fun hideProgressBar(type: TradeProgressBarType)

        fun showUserBalancesMainViews(shouldAnimate: Boolean)

        fun hideUserBalancesMainViews()

        fun showUserBalancesErrorViews(caption: String)

        fun hideUserBalancesErrorViews()

        fun updateFavoriteButtonState(isFavorite: Boolean)

        fun updateInboxButtonItemCount()

        fun updateFormViewFields(newOrderType: OrderType)

        fun updateSeekBarsVisibility(newTradeType: TradeType)

        fun updateTradeButton(newTradeType: TradeType)

        fun updateMainContainer(block: (() -> Unit))

        fun updateBaseCurrencyUserBalanceData(data: MapViewData)

        fun updateQuoteCurrencyUserBalanceData(data: MapViewData)

        fun clearTradeFormFocus()

        fun clearInputViewText(inputView: TradeInputView,
                               shouldNotifyListener: Boolean = true)

        fun navigateToInboxScreen()

        fun setSeekBarsEnabled(areEnabled: Boolean)

        fun setSeekBarProgress(type: TradeSeekBarType, progress: Int)

        fun setSeekBarPercentLabelProgress(type: TradeSeekBarType, progress: Int)

        fun setTradeFormStopPriceLabelText(stopPrice: Double)

        fun setTradeFormPriceLabelText(price: Double)

        fun setSelectedPrice(selectedPrice: Double)

        fun setPriceInfoViewData(currencyMarket: CurrencyMarket, animate: Boolean)

        fun setInputViewState(inputViews: List<TradeInputView>, state: InputViewState)

        fun setInputViewValue(inputView: TradeInputView, value: String,
                              shouldNotifyListener: Boolean = true)

        fun setUserBalancesData(baseCurrencyData: MapViewData,
                                quoteCurrencyData: MapViewData)

        fun getSeekBarProgress(type: TradeSeekBarType): Int

        fun getInputViewValue(inputView: TradeInputView): String

        fun postActionDelayed(delay: Long, action: () -> Unit)

    }


    interface ActionListener {

        fun onToolbarRightButtonClicked()

        fun onToolbarInboxButtonClicked()

        fun onUserBalanceViewClicked(viewId: Int)

        fun onTradeTypeTabSelected(type: TradeType)

        fun onOrderTypeSelected(type: OrderType)

        fun onInputViewValueChanged(type: TradeInputView)

        fun onStartedTrackingSeekBar(type: TradeSeekBarType)

        fun onSeekBarProgressChanged(type: TradeSeekBarType, progress: Int, fromUser: Boolean)

        fun onStoppedTrackingSeekBar(type: TradeSeekBarType)

        fun onTradeButtonClicked()

    }


}