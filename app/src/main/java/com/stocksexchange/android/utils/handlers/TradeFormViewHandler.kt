package com.stocksexchange.android.utils.handlers

import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.model.OrderType
import com.stocksexchange.android.model.TradeInputView
import com.stocksexchange.android.model.TradeSeekBarType
import com.stocksexchange.core.formatters.NumberFormatter

class TradeFormViewHandler(
    private val numberFormatter: NumberFormatter,
    private val listener: Listener
) {


    fun onInputViewValueChanged(type: TradeInputView) {
        // Updating the stop price label text
        if(type == TradeInputView.STOP_PRICE) {
            setTradeFormStopPriceLabelText()
            return
        }

        // Updating the price label text
        if((type == TradeInputView.PRICE) &&
            (listener.getOrderType() in listOf(OrderType.LIMIT, OrderType.STOP_LIMIT))) {
            setTradeFormPriceLabelText()
        }

        // Handling the rest functionality
        when(listener.getTradeType()) {
            TradeType.BUY -> onInputViewValueChangedWhenBuying(type)
            TradeType.SELL -> onInputViewValueChangedWhenSelling(type)
        }
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    private fun onInputViewValueChangedWhenBuying(type: TradeInputView) {
        when(type) {
            TradeInputView.PRICE -> onPriceInputViewValueChangedWhenBuying()
            TradeInputView.AMOUNT -> onAmountInputViewValueChangedWhenBuying()
            TradeInputView.TOTAL -> onTotalInputViewValueChangedWhenBuying()
        }
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    private fun onInputViewValueChangedWhenSelling(type: TradeInputView) {
        when(type) {
            TradeInputView.PRICE -> onPriceInputViewValueChangedWhenSelling()
            TradeInputView.AMOUNT -> onAmountInputViewValueChangedWhenSelling()
            TradeInputView.TOTAL -> onTotalInputViewValueChangedWhenSelling()
        }
    }


    private fun onPriceInputViewValueChangedWhenBuying() {
        onPriceInputViewValueChanged(onTotalUpdated = mOnTotalUpdated)
    }


    private fun onPriceInputViewValueChangedWhenSelling() {
        onPriceInputViewValueChanged(onAmountUpdated = mOnAmountUpdated)
    }


    private fun onPriceInputViewValueChanged(
        onTotalUpdated: (() -> Unit)? = null,
        onAmountUpdated: (() -> Unit)? = null
    ) {
        val isPriceInputValueValid = isInputViewValueValid(TradeInputView.PRICE)
        val isAmountInputValueValid = isInputViewValueValid(TradeInputView.AMOUNT)
        val isTotalInputValueValid = isInputViewValueValid(TradeInputView.TOTAL)

        val price = getInputViewNumberValue(TradeInputView.PRICE)
        val amount = getInputViewNumberValue(TradeInputView.AMOUNT)
        val total = getInputViewNumberValue(TradeInputView.TOTAL)

        if(isAmountInputValueValid) {
            if(isPriceInputValueValid) {
                updateTotal(price, amount)
            } else {
                clearInputViewText(TradeInputView.TOTAL)
            }

            onTotalUpdated?.invoke()
        } else if(isTotalInputValueValid) {
            if(isPriceInputValueValid) {
                updateAmount(total, price)
            }

            onAmountUpdated?.invoke()
        }
    }


    private fun onAmountInputViewValueChangedWhenBuying() {
        onAmountInputViewValueChanged(onTotalUpdated = mOnTotalUpdated)
    }


    private fun onAmountInputViewValueChangedWhenSelling() {
        onAmountInputViewValueChanged(onAmountUpdated = mOnAmountUpdated)
    }


    private fun onAmountInputViewValueChanged(
        onTotalUpdated: (() -> Unit)? = null,
        onAmountUpdated: (() -> Unit)? = null
    ) {
        val isPriceInputValueValid = isInputViewValueValid(TradeInputView.PRICE)
        val isAmountInputValueValid = isInputViewValueValid(TradeInputView.AMOUNT)

        if(isPriceInputValueValid) {
            if(isAmountInputValueValid) {
                val price = getInputViewNumberValue(TradeInputView.PRICE)
                val amount = getInputViewNumberValue(TradeInputView.AMOUNT)

                updateTotal(price, amount)
            } else {
                clearInputViewText(TradeInputView.TOTAL, false)
            }

            onTotalUpdated?.invoke()
        }

        onAmountUpdated?.invoke()
    }


    private fun onTotalInputViewValueChangedWhenBuying() {
        onTotalInputViewValueChanged(onTotalUpdated = mOnTotalUpdated)
    }


    private fun onTotalInputViewValueChangedWhenSelling() {
        onTotalInputViewValueChanged(onAmountUpdated = mOnAmountUpdated)
    }


    private fun onTotalInputViewValueChanged(
        onTotalUpdated: (() -> Unit)? = null,
        onAmountUpdated: (() -> Unit)? = null
    ) {
        val isPriceInputValueValid = isInputViewValueValid(TradeInputView.PRICE)
        val isTotalInputValueValid = isInputViewValueValid(TradeInputView.TOTAL)

        val price = getInputViewNumberValue(TradeInputView.PRICE)
        val total = getInputViewNumberValue(TradeInputView.TOTAL)

        if(isPriceInputValueValid) {
            if(isTotalInputValueValid) {
                updateAmount(total, price)
            } else {
                clearInputViewText(TradeInputView.AMOUNT, false)
            }

            onAmountUpdated?.invoke()
        }

        onTotalUpdated?.invoke()
    }


    fun onSeekBarProgressChanged(type: TradeSeekBarType, progress: Int,
                                 fromUser: Boolean) {
        if(!listener.areWalletsInitialized() || !fromUser) {
            return
        }

        when(type) {
            TradeSeekBarType.AMOUNT -> onAmountSeekBarProgressChanged(progress)
            TradeSeekBarType.TOTAL -> onTotalSeekBarProgressChanged(progress)
        }
    }


    private fun onAmountSeekBarProgressChanged(progress: Int) {
        if(listener.getTradeType() == TradeType.BUY) {
            return
        }

        updateInputViewDueToSeekBarProgressChange(
            inputView = TradeInputView.AMOUNT,
            seekBarType = TradeSeekBarType.AMOUNT,
            progress = progress
        )
    }


    private fun onTotalSeekBarProgressChanged(progress: Int) {
        if(listener.getTradeType() == TradeType.SELL) {
            return
        }

        updateInputViewDueToSeekBarProgressChange(
            inputView = TradeInputView.TOTAL,
            seekBarType = TradeSeekBarType.TOTAL,
            progress = progress
        )
    }


    private fun updateAmount(total: Double, price: Double) {
        val newAmount = (total / price)
        val newAmountStr = numberFormatter.formatAmount(newAmount)

        setInputViewValue(TradeInputView.AMOUNT, newAmountStr, false)
    }



    private fun updateTotal(price: Double, amount: Double) {
        val newTotal = (price * amount)
        val newTotalStr = numberFormatter.formatAmount(newTotal)

        setInputViewValue(TradeInputView.TOTAL, newTotalStr, false)
    }


    private fun updateTotalSeekBarProgress() {
        updateSeekBarProgress(
            seekBarType = TradeSeekBarType.TOTAL,
            inputView = TradeInputView.TOTAL
        )
    }


    private fun updateAmountSeekBarProgress() {
        updateSeekBarProgress(
            seekBarType = TradeSeekBarType.AMOUNT,
            inputView = TradeInputView.AMOUNT
        )
    }


    private fun updateSeekBarProgress(seekBarType: TradeSeekBarType, inputView: TradeInputView) {
        if(!listener.areWalletsInitialized() || listener.isSeekBarBeingTracked(seekBarType)) {
            return
        }

        listener.updateSeekBarProgress(
            seekBarType = seekBarType,
            progress = listener.calculateSeekBarProgressForValue(
                getInputViewNumberValue(inputView)
            )
        )
    }


    private fun updateInputViewDueToSeekBarProgressChange(inputView: TradeInputView,
                                                          seekBarType: TradeSeekBarType,
                                                          progress: Int) {
        val expense = listener.calculateValueForSeekBarProgress(progress)
        val expenseStr = (if(expense != 0.0) numberFormatter.formatAmount(expense) else "")

        listener.setInputViewValue(
            inputView = inputView,
            value = expenseStr,
            shouldNotifyListener = true
        )
        listener.setSeekBarPercentLabelProgress(seekBarType, progress)
    }


    private fun clearInputViewText(inputView: TradeInputView,
                                   shouldNotifyListener: Boolean = true) {
        listener.clearInputViewText(inputView, shouldNotifyListener)
    }


    private fun setTradeFormStopPriceLabelText() {
        listener.setTradeFormStopPriceLabelText(getInputViewNumberValue(
            inputView = TradeInputView.STOP_PRICE,
            defaultValue = -1.0
        ))
    }


    private fun setTradeFormPriceLabelText() {
        listener.setTradeFormPriceLabelText(getInputViewNumberValue(
            inputView = TradeInputView.PRICE,
            defaultValue = -1.0
        ))
    }


    private fun setInputViewValue(inputView: TradeInputView, value: String,
                                  shouldNotifyListener: Boolean = true) {
        listener.setInputViewValue(inputView, value, shouldNotifyListener)
    }


    private fun isInputViewValueValid(inputView: TradeInputView): Boolean {
        return listener.isInputViewValueValid(inputView)
    }


    private fun getInputViewNumberValue(inputView: TradeInputView,
                                        defaultValue: Double = 0.0): Double {
        return listener.getInputViewNumberValue(inputView, defaultValue)
    }


    private val mOnTotalUpdated: (() -> Unit) = {
        updateTotalSeekBarProgress()
    }


    private val mOnAmountUpdated: (() -> Unit) = {
        updateAmountSeekBarProgress()
    }


    interface Listener {

        fun updateSeekBarProgress(seekBarType: TradeSeekBarType, progress: Int)

        fun clearInputViewText(inputView: TradeInputView,
                               shouldNotifyListener: Boolean = true)

        fun setSeekBarPercentLabelProgress(type: TradeSeekBarType, progress: Int)

        fun setTradeFormStopPriceLabelText(stopPrice: Double)

        fun setTradeFormPriceLabelText(price: Double)

        fun setInputViewValue(inputView: TradeInputView, value: String,
                              shouldNotifyListener: Boolean = true)

        fun calculateValueForSeekBarProgress(progress: Int): Double

        fun calculateSeekBarProgressForValue(value: Double): Int

        fun isInputViewValueValid(inputView: TradeInputView): Boolean

        fun isSeekBarBeingTracked(seekBarType: TradeSeekBarType): Boolean

        fun areWalletsInitialized(): Boolean

        fun getInputViewNumberValue(inputView: TradeInputView,
                                    defaultValue: Double = 0.0): Double

        fun getOrderType(): OrderType

        fun getTradeType(): TradeType

        fun getExpenseWalletForCurrentTradeType(): Wallet

    }


}