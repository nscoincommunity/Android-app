package com.stocksexchange.android.ui.trade.views

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.model.TradeInputView
import com.stocksexchange.android.model.TradeSeekBarType
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.formviews.BaseFormView
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.trade_form_view_layout.view.*

/**
 * A form view containing input fields and additional controls (like
 * sliders to provide amount percentage) that are used when creating an order.
 */
class TradeFormView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseFormView<TradeInputView>(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_CUSTOM_MARGIN = "custom_margin"
        private const val ATTRIBUTE_KEY_LABEL_TOP_MARGIN = "label_top_margin"

        private const val DEFAULT_CUSTOM_MARGIN_IN_DP = 15
        private const val DEFAULT_LABEL_TOP_MARGIN_IN_SP = 10

        private const val INPUT_CHARACTERS_LIMIT = 20

    }


    private var mCustomMargin: Int = 0
    private var mLabelTopMargin: Int = 0

    private var mLabelContainersArray: Array<TradeAmountLabelContainer> = arrayOf()




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.TradeFormView, defStyleAttr, 0) {
            with(mAttributes) {
                save(
                    ATTRIBUTE_KEY_CUSTOM_MARGIN,
                    getDimensionPixelSize(
                        R.styleable.TradeFormView_customMargin,
                        dpToPx(DEFAULT_CUSTOM_MARGIN_IN_DP)
                    )
                )
                save(
                    ATTRIBUTE_KEY_LABEL_TOP_MARGIN,
                    getDimensionPixelSize(
                        R.styleable.TradeFormView_labelTopMargin,
                        spToPx(DEFAULT_LABEL_TOP_MARGIN_IN_SP)
                    )
                )
            }
        }
    }


    override fun init() {
        super.init()

        initLabelContainers()
    }


    override fun initInputViews() {
        super.initInputViews()

        mInputViewsArray.forEach {
            it.setEtHintText(mStringProvider.getString(R.string.required))
            it.setEtFilters(arrayOf(InputFilter.LengthFilter(INPUT_CHARACTERS_LIMIT)))
            it.setLabelVisible(true)
        }

        amountLabelContainer.setLabelText(mStringProvider.getString(R.string.trade_form_view_amount_label))
        totalLabelContainer.setLabelText(mStringProvider.getString(R.string.trade_form_view_total_label))
    }


    private fun initLabelContainers() {
        mLabelContainersArray = arrayOf(
            amountLabelContainer,
            totalLabelContainer
        )
        mLabelContainersArray.forEach {
            // Disabling automatic state saving & restoring
            // since the problems arise with two identical IDs
            // of seek bars and percent labels
            it.disableSeekbarControlsStateSaving()
        }
    }



    override fun applyAttributes() {
        with(mAttributes) {
            setCustomMargin(get(ATTRIBUTE_KEY_CUSTOM_MARGIN, dpToPx(DEFAULT_CUSTOM_MARGIN_IN_DP)))
            setLabelTopMargin(get(ATTRIBUTE_KEY_LABEL_TOP_MARGIN, spToPx(DEFAULT_LABEL_TOP_MARGIN_IN_SP)))
        }
    }


    private fun showStopPriceViews() {
        stopPriceLabelTv.makeVisible()
        stopPriceIv.makeVisible()
    }


    private fun hideStopPriceViews() {
        stopPriceLabelTv.makeGone()
        stopPriceIv.makeGone()
    }


    private fun showPriceViews() {
        priceLabelTv.makeVisible()
        priceIv.makeVisible()
    }


    private fun hidePriceViews() {
        priceLabelTv.makeGone()
        priceIv.makeGone()
    }


    private fun showAmountViews() {
        amountLabelContainer.makeVisible()
        amountIv.makeVisible()
    }


    private fun hideAmountViews() {
        amountLabelContainer.makeGone()
        amountIv.makeGone()
    }


    fun showViewsForMarketOrder() {
        amountLabelContainer.clearTopMargin()

        hideStopPriceViews()
        hidePriceViews()
        showAmountViews()
    }


    fun showViewsForLimitOrder() {
        priceLabelTv.clearTopMargin()
        amountLabelContainer.setTopMargin(mLabelTopMargin)

        hideStopPriceViews()
        showPriceViews()
        showAmountViews()
    }


    fun showViewsForStopLimitOrder() {
        stopPriceLabelTv.clearTopMargin()
        priceLabelTv.setTopMargin(mLabelTopMargin)
        amountLabelContainer.setTopMargin(mLabelTopMargin)

        showStopPriceViews()
        showPriceViews()
        showAmountViews()
    }


    fun showSeekBar(seekBarType: TradeSeekBarType) {
        getLabelContainerForType(seekBarType).showSeekBarControls()
    }


    fun hideSeekBar(seekBarType: TradeSeekBarType) {
        getLabelContainerForType(seekBarType).hideSeekBarControls()
    }


    fun setSeekBarEnabled(isEnabled: Boolean) {
        mLabelContainersArray.forEach {
            it.setSeekBarEnabled(isEnabled)
        }
    }


    private fun setCustomMargin(margin: Int) {
        for(view in children) {
            if(view.id != separatorView.id) {
                view.setHorizontalMargin(margin)
            }
        }
    }


    private fun setLabelTopMargin(margin: Int) {
        mLabelTopMargin = margin
    }


    override fun setLabelTextColor(@ColorInt color: Int) {
        super.setLabelTextColor(color)

        mLabelContainersArray.forEach {
            it.setLabelTextColor(color)
        }
    }


    fun setSeekBarThumbColor(@ColorInt color: Int) {
        mLabelContainersArray.forEach {
            it.setSeekBarThumbColor(color)
        }
    }


    fun setSeekBarPrimaryProgressColor(@ColorInt color: Int) {
        mLabelContainersArray.forEach {
            it.setSeekBarPrimaryProgressColor(color)
        }
    }


    fun setSeekBarSecondaryProgressColor(@ColorInt color: Int) {
        mLabelContainersArray.forEach {
            it.setSeekBarSecondaryProgressColor(color)
        }
    }


    fun setSeekBarProgress(seekBarType: TradeSeekBarType, progress: Int) {
        getLabelContainerForType(seekBarType).setSeekBarProgress(progress)
    }


    fun setStopPriceLabelText(text: String) {
        stopPriceLabelTv.text = text
    }


    fun setPriceLabelText(text: String) {
        priceLabelTv.text = text
    }


    fun setSeekBarPercentLabelText(seekBarType: TradeSeekBarType, text: String) {
        getLabelContainerForType(seekBarType).setSeekBarPercentLabelText(text)
    }


    fun setSeekBarPercentLabelText(text: String) {
        mLabelContainersArray.forEach {
            it.setSeekBarPercentLabelText(text)
        }
    }


    fun setSeekBarChangeListener(seekBarType: TradeSeekBarType,
                                 listener: SeekBar.OnSeekBarChangeListener) {
        getLabelContainerForType(seekBarType).setSeekBarChangeListener(listener)
    }


    override fun getLayoutResourceId(): Int = R.layout.trade_form_view_layout


    fun getSeekBarProgress(seekBarType: TradeSeekBarType): Int {
        return getLabelContainerForType(seekBarType).getSeekBarProgress()
    }


    override fun getInputViewForType(inputViewType: TradeInputView): InputView {
        return when(inputViewType) {
            TradeInputView.STOP_PRICE -> stopPriceIv
            TradeInputView.PRICE -> priceIv
            TradeInputView.AMOUNT -> amountIv
            TradeInputView.TOTAL -> totalIv
        }
    }


    private fun getLabelContainerForType(seekBarType: TradeSeekBarType): TradeAmountLabelContainer {
        return when(seekBarType) {
            TradeSeekBarType.AMOUNT -> amountLabelContainer
            TradeSeekBarType.TOTAL -> totalLabelContainer
        }
    }


    override fun getLabelViewsArray(): Array<TextView> {
        return arrayOf(stopPriceLabelTv, priceLabelTv)
    }


    override fun getInputViewsArray(): Array<InputView> {
        return arrayOf(stopPriceIv, priceIv, amountIv, totalIv)
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        if(state is SavedState) {
            mLabelContainersArray.forEachIndexed { index, tradeAmountLabelContainer ->
                with(tradeAmountLabelContainer) {
                    setSeekBarEnabled(state.seekBarsStateArray[index])
                    setSeekBarProgress(state.seekBarsProgressArray[index])
                    setSeekBarPercentLabelText(state.seekBarsPercentLabelArray[index].toString())
                }
            }

            for(child in children) {
                child.restoreHierarchyState(state.container)
            }
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    override fun onSaveInstanceState(savedState: BaseState) {
        super.onSaveInstanceState(savedState)

        if(savedState is SavedState) {
            with(savedState) {
                seekBarsStateArray = mLabelContainersArray.map { it.isSeekBarEnabled() }.toBooleanArray()
                seekBarsProgressArray = mLabelContainersArray.map { it.getSeekBarProgress() }.toIntArray()
                seekBarsPercentLabelArray = mLabelContainersArray.map { it.getSeekBarPercentLabelText() }.toTypedArray()

                for(child in children) {
                    child.saveHierarchyState(container)
                }
            }
        }
    }


    private class SavedState : BaseState {

        companion object {

            private const val KEY_SEEK_BARS_STATE_ARRAY = "seek_bars_state_array"
            private const val KEY_SEEK_BARS_PROGRESS_ARRAY = "seek_bars_progress_array"
            private const val KEY_SEEK_BARS_PERCENT_LABEL_ARRAY = "seek_bars_percent_label_array"


            @JvmField
            var CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }

            }

        }


        internal var seekBarsStateArray: BooleanArray = booleanArrayOf()
        internal var seekBarsProgressArray: IntArray = intArrayOf()
        internal var seekBarsPercentLabelArray: Array<CharSequence> = arrayOf()


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(Constants.CLASS_LOADER)?.run {
                seekBarsStateArray = (getBooleanArray(KEY_SEEK_BARS_STATE_ARRAY) ?: booleanArrayOf())
                seekBarsProgressArray = (getIntArray(KEY_SEEK_BARS_PROGRESS_ARRAY) ?: intArrayOf())
                seekBarsPercentLabelArray = (getCharSequenceArray(KEY_SEEK_BARS_PERCENT_LABEL_ARRAY) ?: arrayOf())
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(Constants.CLASS_LOADER).apply {
                putBooleanArray(KEY_SEEK_BARS_STATE_ARRAY, seekBarsStateArray)
                putIntArray(KEY_SEEK_BARS_PROGRESS_ARRAY, seekBarsProgressArray)
                putCharSequenceArray(KEY_SEEK_BARS_PERCENT_LABEL_ARRAY, seekBarsPercentLabelArray)
            })
        }

    }


}