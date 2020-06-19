package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.extensions.getUserAdmissionButtonDrawable
import com.stocksexchange.android.ui.views.base.containers.BaseConstraintLayoutView
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.extensions.setColor
import kotlinx.android.synthetic.main.user_admission_button_layout.view.*

/**
 * A custom button with a ProgressBar widget to use inside user admission screens.
 */
class UserAdmissionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseConstraintLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_BUTTON_TEXT_COLOR = "button_text_color"
        private const val ATTRIBUTE_BUTTON_PROGRESS_BAR_COLOR = "button_progress_bar"
        private const val ATTRIBUTE_BUTTON_TEXT = "button_text"

        private const val DEFAULT_BUTTON_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_BUTTON_PROGRESS_BAR_COLOR = Color.GREEN
        private const val DEFAULT_BUTTON_TEXT = ""

    }


    private var mButtonText: String = ""




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.UserAdmissionButton, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_BUTTON_TEXT_COLOR, getColor(R.styleable.UserAdmissionButton_buttonTextColor, DEFAULT_BUTTON_TEXT_COLOR))
                save(ATTRIBUTE_BUTTON_PROGRESS_BAR_COLOR, getColor(R.styleable.UserAdmissionButton_buttonProgressBarColor, DEFAULT_BUTTON_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_BUTTON_TEXT, (getString(R.styleable.UserAdmissionButton_buttonText) ?: DEFAULT_BUTTON_TEXT))
            }
        }
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setButtonTextColor(get(ATTRIBUTE_BUTTON_TEXT_COLOR, DEFAULT_BUTTON_TEXT_COLOR))
            setButtonProgressBarColor(get(ATTRIBUTE_BUTTON_PROGRESS_BAR_COLOR, DEFAULT_BUTTON_PROGRESS_BAR_COLOR))
            setButtonText(get(ATTRIBUTE_BUTTON_TEXT, DEFAULT_BUTTON_TEXT))
        }
    }


    fun showProgressBar() {
        button.text = ""
        progressBar.makeVisible()
    }


    fun hideProgressBar() {
        button.text = mButtonText
        progressBar.makeGone()
    }


    fun setButtonTextColor(@ColorInt color: Int) {
        button.setTextColor(color)
    }


    fun setButtonProgressBarColor(@ColorInt color: Int) {
        progressBar.setColor(color)
    }


    fun setButtonText(text: String) {
        mButtonText = text

        button.text = text
    }


    fun setButtonBackgroundColor(@ColorInt color: Int) {
        button.background = context.getUserAdmissionButtonDrawable(color)
    }


    fun setOnButtonClickListener(listener: ((View) -> Unit)) {
        button.setOnClickListener(listener)
    }


    fun setButtonBackground(drawable: Int) {
        button.background = context.getCompatDrawable(drawable)
    }


    override fun getLayoutResourceId(): Int = R.layout.user_admission_button_layout


}