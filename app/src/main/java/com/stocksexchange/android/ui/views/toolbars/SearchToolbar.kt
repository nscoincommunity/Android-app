package com.stocksexchange.android.ui.views.toolbars

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.core.managers.KeyboardManager
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.interfaces.Recyclable
import kotlinx.android.synthetic.main.search_toolbar_layout.view.*
import org.koin.core.inject

/**
 * A container that holds widgets that may be found in
 * a standard implementation of a search toolbar.
 */
class SearchToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr), Recyclable {


    companion object {

        private const val ATTRIBUTE_KEY_HINT_COLOR = "hint_color"
        private const val ATTRIBUTE_KEY_TEXT_COLOR = "text_color"
        private const val ATTRIBUTE_KEY_BUTTON_DRAWABLE_COLOR = "button_drawable_color"
        private const val ATTRIBUTE_KEY_PROGRESS_BAR_COLOR = "progress_bar_color"
        private const val ATTRIBUTE_KEY_HINT_TEXT = "hint_text"
        private const val ATTRIBUTE_KEY_LEFT_BUTTON_DRAWABLE = "left_button_drawable"
        private const val ATTRIBUTE_KEY_CLEAR_INPUT_BUTTON_DRAWABLE = "clear_input_button_drawable"
        private const val ATTRIBUTE_KEY_CURSOR_DRAWABLE = "cursor_drawable"

        private const val DEFAULT_HINT_TEXT = ""
        private const val DEFAULT_HINT_COLOR = Color.WHITE

        private const val DEFAULT_TEXT_COLOR = Color.WHITE

        private const val DEFAULT_BUTTON_DRAWABLE_COLOR = Color.WHITE
        private const val DEFAULT_PROGRESS_BAR_COLOR = Color.WHITE

        private const val KEYBOARD_SHOWING_DELAY = 150L

        private const val CLEAR_INPUT_BUTTON_ANIMATION_DURATION = 100L

    }


    private val defaultToolbarHeight: Int = context.dimenInPx(R.dimen.toolbar_height)

    private val mKeyboardManager: KeyboardManager by inject()

    private var mValueAnimator: ValueAnimator? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.SearchToolbar, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_HINT_COLOR, getColor(R.styleable.SearchToolbar_hintColor, DEFAULT_HINT_COLOR))
                save(ATTRIBUTE_KEY_TEXT_COLOR, getColor(R.styleable.SearchToolbar_textColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_BUTTON_DRAWABLE_COLOR, getColor(R.styleable.SearchToolbar_buttonDrawableColor, DEFAULT_BUTTON_DRAWABLE_COLOR))
                save(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, getColor(R.styleable.SearchToolbar_progressBarColor, DEFAULT_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_KEY_HINT_TEXT, getString(R.styleable.SearchToolbar_hintText) ?: DEFAULT_HINT_TEXT)
                save(ATTRIBUTE_KEY_LEFT_BUTTON_DRAWABLE, getDrawable(R.styleable.SearchToolbar_leftButtonDrawable))
                save(ATTRIBUTE_KEY_CLEAR_INPUT_BUTTON_DRAWABLE, getDrawable(R.styleable.SearchToolbar_clearInputButtonDrawable))
                save(ATTRIBUTE_KEY_CURSOR_DRAWABLE, getDrawable(R.styleable.SearchToolbar_cursorDrawable))
            }
        }
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setHintColor(get(ATTRIBUTE_KEY_HINT_COLOR, DEFAULT_HINT_COLOR))
            setTextColor(get(ATTRIBUTE_KEY_TEXT_COLOR, DEFAULT_TEXT_COLOR))
            setButtonDrawableColor(get(ATTRIBUTE_KEY_BUTTON_DRAWABLE_COLOR, DEFAULT_BUTTON_DRAWABLE_COLOR))
            setProgressBarColor(get(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, DEFAULT_PROGRESS_BAR_COLOR))
            setHintText(get(ATTRIBUTE_KEY_HINT_TEXT, DEFAULT_HINT_TEXT))

            get<Drawable?>(ATTRIBUTE_KEY_LEFT_BUTTON_DRAWABLE, null)?.also {
                setLeftButtonDrawable(it)
            }

            get<Drawable?>(ATTRIBUTE_KEY_CLEAR_INPUT_BUTTON_DRAWABLE, null)?.also {
                setClearInputButtonDrawable(it)
            }

            get<Drawable?>(ATTRIBUTE_KEY_CURSOR_DRAWABLE, null)?.also {
                setCursorDrawable(it)
            }
        }
    }


    private fun runClearInputButtonAnimation(valueAnimator: ValueAnimator) {
        with(valueAnimator) {
            mValueAnimator = this

            addUpdateListener {
                clearInputBtnIv.setScale(it.animatedValue as Float)
            }
            interpolator = LinearInterpolator()
            duration = CLEAR_INPUT_BUTTON_ANIMATION_DURATION
            start()
        }
    }


    fun showKeyboard(shouldDelay: Boolean) {
        queryInputEt.requestFocus()
        queryInputEt.postDelayed(
            { mKeyboardManager.showKeyboard(queryInputEt) },
            (if(shouldDelay) KEYBOARD_SHOWING_DELAY else 0L)
        )
    }


    fun hideKeyboard() {
        queryInputEt.clearFocus()
        mKeyboardManager.hideKeyboard(queryInputEt)
    }


    fun showLeftButtonIv() {
        if(leftBtnIv.isVisible) {
            return
        }

        leftBtnIv.makeVisible()
    }


    fun hideLeftButtonIv() {
        if(leftBtnIv.isGone) {
            return
        }

        leftBtnIv.makeGone()
    }


    fun showProgressBar() {
        if(progressBar.isVisible) {
            return
        }

        progressBar.makeVisible()
    }


    fun hideProgressBar() {
        if(progressBar.isGone) {
            return
        }

        progressBar.makeGone()
    }


    fun showClearInputButton(animate: Boolean) {
        if(animate) {
            mValueAnimator?.cancel()

            clearInputBtnIv.setScale(0f)
            clearInputBtnIv.makeVisible()

            runClearInputButtonAnimation(ValueAnimator.ofFloat(0f, 1f))
        } else {
            clearInputBtnIv.makeVisible()
        }
    }



    fun hideClearInputButton(animate: Boolean) {
        if(animate) {
            mValueAnimator?.cancel()

            clearInputBtnIv.setScale(1f)

            val valueAnimator = ValueAnimator.ofFloat(1f, 0f).apply {
                doOnEnd { clearInputBtnIv.makeGone() }
            }

            runClearInputButtonAnimation(valueAnimator)
        } else {
            clearInputBtnIv.makeGone()
        }
    }


    fun setHintColor(@ColorInt color: Int) {
        queryInputEt.setHintTextColor(color)
    }


    fun setTextColor(@ColorInt color: Int) {
        queryInputEt.setTextColor(color)
    }


    fun setLeftButtonDrawableColor(@ColorInt color: Int) {
        leftBtnIv.setColor(color)
    }


    fun setClearInputButtonDrawableColor(@ColorInt color: Int) {
        clearInputBtnIv.setColor(color)
    }


    fun setButtonDrawableColor(@ColorInt color: Int) {
        setLeftButtonDrawableColor(color)
        setClearInputButtonDrawableColor(color)
    }


    fun setProgressBarColor(@ColorInt color: Int) {
        progressBar.setColor(color)
    }


    fun setInputType(inputType: Int) {
        queryInputEt.inputType = inputType
    }


    fun setHintText(text: String) {
        queryInputEt.hint = text
    }


    fun setText(text: String) {
        queryInputEt.setText(text)
        queryInputEt.setSelection(text.length)
    }


    fun setCursorDrawable(drawable: Drawable?) {
        queryInputEt.setCursorDrawable(drawable)
    }


    /**
     * Sets a drawable of the left button.
     *
     * @param drawable The drawable to set
     * @param showIfHidden Whether to show the button if it is hidden.
     * Default is true.
     */
    fun setLeftButtonDrawable(drawable: Drawable?, showIfHidden: Boolean = true) {
        leftBtnIv.setImageDrawable(drawable)

        if(showIfHidden) {
            showLeftButtonIv()
        }
    }


    /**
     * Sets a drawable of the clear input button.
     *
     * @param drawable The drawable to set
     * @param showIfHidden Whether to show the button if it is hidden.
     * Default is true.
     */
    fun setClearInputButtonDrawable(drawable: Drawable?, showIfHidden: Boolean = true) {
        clearInputBtnIv.setImageDrawable(drawable)

        if(showIfHidden && !queryInputEt.isEmpty()) {
            showClearInputButton(false)
        }
    }


    fun addTextWatcher(textWatcher: TextWatcher) {
        queryInputEt.addTextChangedListener(textWatcher)
    }


    fun setOnEditorActionListener(listener: TextView.OnEditorActionListener) {
        queryInputEt.setOnEditorActionListener(listener)
    }


    fun setOnLeftButtonClickListener(listener: (View) -> Unit) {
        leftBtnIv.setOnClickListener(listener)
    }


    fun setOnClearInputButtonClickListener(listener: (View) -> Unit) {
        clearInputBtnIv.setOnClickListener(listener)
    }


    override fun getLayoutResourceId(): Int = R.layout.search_toolbar_layout


    fun getProgressBar(): ProgressBar {
        return progressBar
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(
                View.resolveSize(defaultToolbarHeight, heightMeasureSpec),
                MeasureSpec.EXACTLY
            )
        )
    }


    override fun recycle() {
        mKeyboardManager.recycle()
        mValueAnimator?.end()
    }


}