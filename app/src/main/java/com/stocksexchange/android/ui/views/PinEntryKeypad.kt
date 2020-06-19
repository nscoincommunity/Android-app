package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.android.utils.extensions.*
import com.stocksexchange.core.utils.extensions.disable
import com.stocksexchange.core.utils.extensions.enable
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.pin_entry_keypad_layout.view.*

/**
 * A keypad used for entering a PIN code.
 */
class PinEntryKeypad @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr), View.OnClickListener {


    companion object {

        private const val ATTRIBUTE_DIGIT_BUTTON_TEXT_COLOR = "digit_button_text_color"

        private const val DEFAULT_DIGIT_BUTTON_TEXT_COLOR = Color.WHITE

    }


    private var mEnabledDigitButtonBackgroundColor: Int = 0
    private var mDisabledDigitButtonBackgroundColor: Int = 0

    private var mEnabledFingerprintButtonBackgroundColor: Int = 0
    private var mDisabledFingerprintButtonBackgroundColor: Int = 0
    private var mEnabledFingerprintButtonForegroundColor: Int = 0
    private var mDisabledFingerprintButtonForegroundColor: Int = 0

    private var mEnabledDeleteButtonBackgroundColor: Int = 0
    private var mDisabledDeleteButtonBackgroundColor: Int = 0
    private var mEnabledDeleteButtonForegroundColor: Int = 0
    private var mDisabledDeleteButtonForegroundColor: Int = 0

    private lateinit var mDigitBtnTvs: Array<TextView>

    var onButtonClickListener: OnButtonClickListener? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.PinEntryKeypad, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_DIGIT_BUTTON_TEXT_COLOR, getColor(R.styleable.PinEntryKeypad_digitButtonTextColor, DEFAULT_DIGIT_BUTTON_TEXT_COLOR))
            }
        }
    }


    override fun init() {
        super.init()

        mDigitBtnTvs = arrayOf(
            oneDigitBtnTv,
            twoDigitBtnTv,
            threeDigitBtnTv,
            fourDigitBtnTv,
            fiveDigitBtnTv,
            sixDigitBtnTv,
            sevenDigitBtnTv,
            eightDigitBtnTv,
            nineDigitBtnTv,
            zeroDigitBtnTv
        )
        mDigitBtnTvs.forEach {
            it.setOnClickListener(this)
        }

        fingerprintActionBtnIv.setOnClickListener(this)
        fingerprintOverlayView.setOnClickListener(this)

        deleteActionBtnIv.setOnClickListener(this)
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setDigitButtonTextColor(get(ATTRIBUTE_DIGIT_BUTTON_TEXT_COLOR, DEFAULT_DIGIT_BUTTON_TEXT_COLOR))
        }
    }


    private fun updateDigitButtonBackground(isEnabled: Boolean) {
        mDigitBtnTvs.forEach {
            it.background = context.getPinEntryDigitButtonDrawable(if(isEnabled) {
                mEnabledDigitButtonBackgroundColor
            } else {
                mDisabledDigitButtonBackgroundColor
            })
        }
    }


    private fun updateFingerprintButtonBackground() {
        if(fingerprintActionBtnIv.isEnabled) {
            fingerprintActionBtnIv.background = context.getPinEntryFingerprintButtonDrawable(
                backgroundColor = mEnabledFingerprintButtonBackgroundColor,
                foregroundColor = mEnabledFingerprintButtonForegroundColor
            )
        } else {
            fingerprintActionBtnIv.background = context.getPinEntryFingerprintButtonDrawable(
                backgroundColor = mDisabledFingerprintButtonBackgroundColor,
                foregroundColor = mDisabledFingerprintButtonForegroundColor
            )
        }
    }


    private fun updateDeleteButtonBackground() {
        if(deleteActionBtnIv.isEnabled) {
            deleteActionBtnIv.background = context.getPinEntryDeleteButtonDrawable(
                backgroundColor = mEnabledDeleteButtonBackgroundColor,
                foregroundColor = mEnabledDeleteButtonForegroundColor
            )
        } else {
            deleteActionBtnIv.background = context.getPinEntryDeleteButtonDrawable(
                backgroundColor = mDisabledDeleteButtonBackgroundColor,
                foregroundColor = mDisabledDeleteButtonForegroundColor
            )
        }
    }


    fun showFingerprintButton() {
        if(!fingerprintButtonContainerFl.isVisible) {
            fingerprintButtonContainerFl.makeVisible()
        }
    }


    fun hideFingerprintButton() {
        if(!fingerprintButtonContainerFl.isGone) {
            fingerprintButtonContainerFl.makeGone()
        }
    }


    fun showFingerprintOverlayView() {
        if(!fingerprintOverlayView.isVisible) {
            fingerprintOverlayView.makeVisible()
        }
    }


    fun hideFingerprintOverlayView() {
        if(!fingerprintOverlayView.isGone) {
            fingerprintOverlayView.makeGone()
        }
    }


    fun enableFingerprintButton() {
        if(!fingerprintActionBtnIv.isEnabled) {
            fingerprintActionBtnIv.enable()

            updateFingerprintButtonBackground()
        }
    }


    fun disableFingerprintButton() {
        if(fingerprintActionBtnIv.isEnabled) {
            fingerprintActionBtnIv.disable()

            updateFingerprintButtonBackground()
        }
    }


    fun enableDeleteButton() {
        if(!deleteActionBtnIv.isEnabled) {
            deleteActionBtnIv.enable()

            updateDeleteButtonBackground()
        }
    }


    fun disableDeleteButton() {
        if(deleteActionBtnIv.isEnabled) {
            deleteActionBtnIv.disable()

            updateDeleteButtonBackground()
        }
    }


    fun enableDigitButtons() {
        mDigitBtnTvs.forEach {
            it.enable()
            updateDigitButtonBackground(true)
        }
    }


    fun disableDigitButtons() {
        mDigitBtnTvs.forEach {
            it.disable()
            updateDigitButtonBackground(false)
        }
    }


    fun setDigitButtonTextColor(@ColorInt color: Int) {
        mDigitBtnTvs.forEach {
            it.setTextColor(color)
        }
    }


    fun setDigitButtonColors(@ColorInt enabledButtonBackgroundColor: Int,
                             @ColorInt disabledButtonBackgroundColor: Int) {
        mEnabledDigitButtonBackgroundColor = enabledButtonBackgroundColor
        mDisabledDigitButtonBackgroundColor = disabledButtonBackgroundColor

        updateDigitButtonBackground(mDigitBtnTvs.first().isEnabled)
    }


    fun setFingerprintButtonColors(@ColorInt enabledButtonBackgroundColor: Int,
                                   @ColorInt disabledButtonBackgroundColor: Int,
                                   @ColorInt enabledButtonForegroundColor: Int,
                                   @ColorInt disabledButtonForegroundColor: Int) {
        mEnabledFingerprintButtonBackgroundColor = enabledButtonBackgroundColor
        mDisabledFingerprintButtonBackgroundColor = disabledButtonBackgroundColor
        mEnabledFingerprintButtonForegroundColor = enabledButtonForegroundColor
        mDisabledFingerprintButtonForegroundColor = disabledButtonForegroundColor

        updateFingerprintButtonBackground()
    }


    fun setDeleteButtonColors(@ColorInt enabledButtonBackgroundColor: Int,
                              @ColorInt disabledButtonBackgroundColor: Int,
                              @ColorInt enabledButtonForegroundColor: Int,
                              @ColorInt disabledButtonForegroundColor: Int) {
        mEnabledDeleteButtonBackgroundColor = enabledButtonBackgroundColor
        mDisabledDeleteButtonBackgroundColor = disabledButtonBackgroundColor
        mEnabledDeleteButtonForegroundColor = enabledButtonForegroundColor
        mDisabledDeleteButtonForegroundColor = disabledButtonForegroundColor

        updateDeleteButtonBackground()
    }


    override fun getLayoutResourceId(): Int = R.layout.pin_entry_keypad_layout


    override fun onClick(view: View) {
        when(view.id) {

            R.id.oneDigitBtnTv,
            R.id.twoDigitBtnTv,
            R.id.threeDigitBtnTv,
            R.id.fourDigitBtnTv,
            R.id.fiveDigitBtnTv,
            R.id.sixDigitBtnTv,
            R.id.sevenDigitBtnTv,
            R.id.eightDigitBtnTv,
            R.id.nineDigitBtnTv,
            R.id.zeroDigitBtnTv -> {
                onButtonClickListener?.onDigitButtonClicked(view.tag as String)
            }

            R.id.fingerprintActionBtnIv -> {
                onButtonClickListener?.onFingerprintButtonClicked()
            }

            R.id.fingerprintOverlayView -> {
                onButtonClickListener?.onFingerprintOverlayClicked()
            }

            R.id.deleteActionBtnIv -> {
                onButtonClickListener?.onDeleteButtonClicked()
            }

        }
    }


    interface OnButtonClickListener {

        fun onDigitButtonClicked(digit: String)

        fun onFingerprintButtonClicked()

        fun onFingerprintOverlayClicked()

        fun onDeleteButtonClicked()

    }


}