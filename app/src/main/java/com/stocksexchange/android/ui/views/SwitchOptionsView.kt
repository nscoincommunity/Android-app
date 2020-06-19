package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseLinearLayoutView
import com.stocksexchange.core.utils.extensions.setColor
import com.stocksexchange.core.utils.extensions.spToPx
import kotlinx.android.synthetic.main.switch_options_view_layout.view.*

/**
 * A group view representing a switch and its two options.
 */
class SwitchOptionsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLinearLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_LEFT_OPTION_TITLE_TEXT = "left_option_title_text"
        private const val ATTRIBUTE_KEY_RIGHT_OPTION_TITLE_TEXT = "right_option_title_text"
        private const val ATTRIBUTE_KEY_OPTIONS_TITLE_TEXT_SIZE = "options_title_text_size"
        private const val ATTRIBUTE_KEY_OPTIONS_TITLE_TEXT_COLOR = "options_title_text_color"
        private const val ATTRIBUTE_KEY_SWITCH_COLOR = "switch_color"

        private const val DEFAULT_TITLE_TEXT = ""
        private const val DEFAULT_TEXT_SIZE = 16f

        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_SWITCH_COLOR = Color.GREEN

    }


    private var mShouldNotifyListener: Boolean = false

    var onOptionChangeListener: OnOptionSelectionListener? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.SwitchOptionsView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_LEFT_OPTION_TITLE_TEXT, getString(R.styleable.SwitchOptionsView_leftOptionTitleText) ?: DEFAULT_TITLE_TEXT)
                save(ATTRIBUTE_KEY_RIGHT_OPTION_TITLE_TEXT, getString(R.styleable.SwitchOptionsView_rightOptionTitleText) ?: DEFAULT_TITLE_TEXT)
                save(ATTRIBUTE_KEY_OPTIONS_TITLE_TEXT_SIZE, getDimension(R.styleable.SwitchOptionsView_optionsTitleTextSize, spToPx(DEFAULT_TEXT_SIZE)))
                save(ATTRIBUTE_KEY_OPTIONS_TITLE_TEXT_COLOR, getColor(R.styleable.SwitchOptionsView_optionsTitleTextColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_SWITCH_COLOR, getColor(R.styleable.SwitchOptionsView_switchColor, DEFAULT_SWITCH_COLOR))
            }
        }
    }


    override fun init() {
        super.init()

        orientation = HORIZONTAL

        initSwitch()
    }


    private fun initSwitch() {
        switchView.setOnCheckedChangeListener { _, isChecked ->
            if(!mShouldNotifyListener) {
                return@setOnCheckedChangeListener
            }

            if(isChecked) {
                onOptionChangeListener?.onRightOptionSelected()
            } else {
                onOptionChangeListener?.onLeftOptionSelected()
            }
        }
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setLeftOptionTitleText(get(ATTRIBUTE_KEY_LEFT_OPTION_TITLE_TEXT, DEFAULT_TITLE_TEXT))
            setRightOptionTitleText(get(ATTRIBUTE_KEY_RIGHT_OPTION_TITLE_TEXT, DEFAULT_TITLE_TEXT))
            setOptionsTitleTextSize(get(ATTRIBUTE_KEY_OPTIONS_TITLE_TEXT_SIZE, spToPx(DEFAULT_TEXT_SIZE)))
            setOptionsTitleTextColor(get(ATTRIBUTE_KEY_OPTIONS_TITLE_TEXT_COLOR, DEFAULT_TEXT_COLOR))
            setSwitchColor(get(ATTRIBUTE_KEY_SWITCH_COLOR, DEFAULT_SWITCH_COLOR))
        }
    }


    fun setLeftOptionTitleText(text: String) {
        leftOptionTv.text = text
    }


    fun setRightOptionTitleText(text: String) {
        rightOptionTv.text = text
    }


    fun setOptionsTitleTextSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        leftOptionTv.setTextSize(unit, textSize)
        rightOptionTv.setTextSize(unit, textSize)
    }


    fun setOptionsTitleTextColor(@ColorInt color: Int) {
        leftOptionTv.setTextColor(color)
        rightOptionTv.setTextColor(color)
    }


    fun setSwitchColor(@ColorInt color: Int) {
        switchView.setColor(color)
    }


    fun setLeftOptionSelected(shouldNotifyListener: Boolean = true) {
        mShouldNotifyListener = shouldNotifyListener
        switchView.isChecked = false
        mShouldNotifyListener = true
    }


    fun setRightOptionSelected(shouldNotifyListener: Boolean = true) {
        mShouldNotifyListener = shouldNotifyListener
        switchView.isChecked = true
        mShouldNotifyListener = true
    }


    fun isLeftOptionSelected(): Boolean {
        return !isRightOptionSelected()
    }


    fun isRightOptionSelected(): Boolean {
        return switchView.isChecked
    }


    override fun getLayoutResourceId(): Int = R.layout.switch_options_view_layout


    fun getSwitchView(): SwitchCompat {
        return switchView
    }


    /**
     * A listener to use to get notified when the left
     * and right options are selected.
     */
    interface OnOptionSelectionListener {

        fun onLeftOptionSelected() {
            // Stub
        }

        fun onRightOptionSelected() {
            // Stub
        }

    }


}