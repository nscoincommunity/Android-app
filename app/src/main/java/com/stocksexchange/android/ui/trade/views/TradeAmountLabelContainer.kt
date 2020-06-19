package com.stocksexchange.android.ui.trade.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.Constants.TRADING_AMOUNT_SEEK_BAR_MAX_PROGRESS
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.trade_amount_label_container_layout.view.*

/**
 * A container that holds a label and extra controls (like a slider).
 */
class TradeAmountLabelContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_LABEL_TEXT_COLOR = "label_text_color"
        private const val ATTRIBUTE_KEY_LABEL_TEXT = "label_text"

        private const val DEFAULT_LABEL_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_LABEL_TEXT = ""

    }


    private var mLabelViewsArray: Array<TextView> = arrayOf()




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.TradeAmountLabelContainer, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_LABEL_TEXT_COLOR, getColor(R.styleable.TradeAmountLabelContainer_labelTextColor, DEFAULT_LABEL_TEXT_COLOR))
                save(ATTRIBUTE_KEY_LABEL_TEXT, (getText(R.styleable.TradeAmountLabelContainer_labelText) ?: DEFAULT_LABEL_TEXT))
            }
        }
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setLabelTextColor(get(ATTRIBUTE_KEY_LABEL_TEXT_COLOR, DEFAULT_LABEL_TEXT_COLOR))
            setLabelText(get(ATTRIBUTE_KEY_LABEL_TEXT, DEFAULT_LABEL_TEXT))
        }
    }


    override fun init() {
        super.init()

        initLabelViews()
        initSeekBar()
    }


    private fun initLabelViews() {
        mLabelViewsArray = arrayOf(labelTv, seekBarPercentLabelTv)
    }


    private fun initSeekBar() {
        seekBar.max = TRADING_AMOUNT_SEEK_BAR_MAX_PROGRESS
    }


    fun showSeekBarControls() {
        seekBar.makeVisible()
        seekBarPercentLabelTv.makeVisible()
    }


    fun hideSeekBarControls() {
        seekBar.makeGone()
        seekBarPercentLabelTv.makeGone()
    }


    fun enableSeekBarControlsStateSaving() {
        seekBar.isSaveEnabled = true
        seekBarPercentLabelTv.isSaveEnabled = true
    }


    fun disableSeekbarControlsStateSaving() {
        seekBar.isSaveEnabled = false
        seekBarPercentLabelTv.isSaveEnabled = false
    }


    fun setSeekBarEnabled(isEnabled: Boolean) {
        seekBar.isEnabled = isEnabled
    }


    fun setSeekBarThumbColor(@ColorInt color: Int) {
        seekBar.setThumbColor(color)
    }


    fun setSeekBarPrimaryProgressColor(@ColorInt color: Int) {
        seekBar.setPrimaryProgressColor(color)
    }


    fun setSeekBarSecondaryProgressColor(@ColorInt color: Int) {
        seekBar.setSecondaryProgressColor(color)
    }


    fun setLabelTextColor(@ColorInt color: Int) {
        mLabelViewsArray.forEach {
            it.setTextColor(color)
        }
    }


    fun setSeekBarProgress(progress: Int) {
        seekBar.progress = progress
    }


    fun setLabelText(text: String) {
        labelTv.text = text
    }


    fun setSeekBarPercentLabelText(text: String) {
        seekBarPercentLabelTv.text = text
    }


    fun setSeekBarChangeListener(listener: SeekBar.OnSeekBarChangeListener) {
        seekBar.setOnSeekBarChangeListener(listener)
    }


    fun isSeekBarEnabled(): Boolean = seekBar.isEnabled


    fun getSeekBarProgress(): Int = seekBar.progress


    fun getSeekBarPercentLabelText(): CharSequence = seekBarPercentLabelTv.text


    override fun getLayoutResourceId(): Int = R.layout.trade_amount_label_container_layout


}