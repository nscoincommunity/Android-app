package com.stocksexchange.android.ui.views.toolbars

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

/**
 * A container that holds widgets that may be found in
 * a standard implementation of a toolbar.
 */
class Toolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_BUTTON_DRAWABLE_COLOR = "button_drawable_color"
        private const val ATTRIBUTE_KEY_PROGRESS_BAR_COLOR = "progress_bar_color"
        private const val ATTRIBUTE_KEY_TITLE_COLOR = "title_color"
        private const val ATTRIBUTE_TITLE = "title"
        private const val ATTRIBUTE_LEFT_BUTTON_DRAWABLE = "left_button_drawable"
        private const val ATTRIBUTE_PRE_RIGHT_BUTTON_DRAWABLE = "pre_right_button_drawable"
        private const val ATTRIBUTE_RIGHT_BUTTON_DRAWABLE = "right_button_drawable"
        private const val ATTRIBUTE_ALERT_PRICE_BUTTON_DRAWABLE = "alert_price_button_drawable"
        private const val ATTRIBUTE_INBOX_BUTTON_VISIBLE = "inbox_button_visible"

        private const val DEFAULT_BUTTON_DRAWABLE_COLOR = Color.WHITE
        private const val DEFAULT_PROGRESS_BAR_COLOR = Color.WHITE
        private const val DEFAULT_TITLE_COLOR = Color.WHITE

        private const val DEFAULT_TITLE = ""
        private const val DEFAULT_INBOX_BUTTON_VISIBLE = false
        private const val DEFAULT_INBOX_BUTTON_COUNT_MESSAGE = 0

        private const val DEFAULT_ALERT_PRICE_BUTTON_VISIBLE = false

        private const val BUTTON_ANIMATION_DURATION = 150L
        private val BUTTON_ANIMATION_INTERPOLATOR = LinearInterpolator()

    }


    private val defaultToolbarHeight: Int = dimenInPx(R.dimen.toolbar_height)




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.CustomToolbar, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_BUTTON_DRAWABLE_COLOR, getColor(R.styleable.CustomToolbar_buttonDrawableColor, DEFAULT_BUTTON_DRAWABLE_COLOR))
                save(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, getColor(R.styleable.CustomToolbar_progressBarColor, DEFAULT_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_KEY_TITLE_COLOR, getColor(R.styleable.CustomToolbar_titleColor, DEFAULT_TITLE_COLOR))
                save(ATTRIBUTE_TITLE, getString(R.styleable.CustomToolbar_title) ?: DEFAULT_TITLE)
                save(ATTRIBUTE_LEFT_BUTTON_DRAWABLE, getDrawable(R.styleable.CustomToolbar_leftButtonDrawable))
                save(ATTRIBUTE_PRE_RIGHT_BUTTON_DRAWABLE, getDrawable(R.styleable.CustomToolbar_preRightButtonDrawable))
                save(ATTRIBUTE_ALERT_PRICE_BUTTON_DRAWABLE, getDrawable(R.styleable.CustomToolbar_alertPriceButtonDrawable))
                save(ATTRIBUTE_RIGHT_BUTTON_DRAWABLE, getDrawable(R.styleable.CustomToolbar_rightButtonDrawable))
                save(ATTRIBUTE_INBOX_BUTTON_VISIBLE, getBoolean(R.styleable.CustomToolbar_inboxButtonVisible, DEFAULT_INBOX_BUTTON_VISIBLE))
            }
        }
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setButtonDrawableColor(get(ATTRIBUTE_KEY_BUTTON_DRAWABLE_COLOR, DEFAULT_BUTTON_DRAWABLE_COLOR))
            setProgressBarColor(get(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, DEFAULT_PROGRESS_BAR_COLOR))
            setTitleColor(get(ATTRIBUTE_KEY_TITLE_COLOR, DEFAULT_TITLE_COLOR))
            setTitleText(get(ATTRIBUTE_TITLE, DEFAULT_TITLE))

            get<Drawable?>(ATTRIBUTE_LEFT_BUTTON_DRAWABLE, null)?.also {
                setLeftButtonDrawable(it)
            }

            get<Drawable?>(ATTRIBUTE_PRE_RIGHT_BUTTON_DRAWABLE, null)?.also {
                setPreRightButtonDrawable(it)
            }

            get<Drawable?>(ATTRIBUTE_RIGHT_BUTTON_DRAWABLE, null)?.also {
                setRightButtonDrawable(it)
            }

            get<Drawable?>(ATTRIBUTE_ALERT_PRICE_BUTTON_DRAWABLE, null)?.also {
                setAlertPriceButtonDrawable(it)
            }

            setVisibleInboxButton(get(ATTRIBUTE_INBOX_BUTTON_VISIBLE, DEFAULT_INBOX_BUTTON_VISIBLE))
        }
    }


    fun showLeftButton() {
        if(leftBtnIv.isVisible) {
            return
        }

        leftBtnIv.makeVisible()
    }


    fun hideLeftButton() {
        if(leftBtnIv.isGone) {
            return
        }

        leftBtnIv.makeGone()
    }


    fun showPreRightButton(animate: Boolean = false) {
        if(preRightBtnIv.isVisible) {
            return
        }

        if(animate) {
            preRightBtnIv.makeVisible()

            preRightBtnIv.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(BUTTON_ANIMATION_DURATION)
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setListener(null)
                .start()
        } else {
            preRightBtnIv.makeVisible()
        }
    }


    fun hidePreRightButton(animate: Boolean = false) {
        if(preRightBtnIv.isGone) {
            return
        }

        if(animate) {
            preRightBtnIv.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(BUTTON_ANIMATION_DURATION)
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        preRightBtnIv.makeGone()
                    }

                })
                .start()
        } else {
            preRightBtnIv.makeGone()
        }
    }


    fun showRightButton() {
        if(rightBtnIv.isVisible) {
            return
        }

        rightBtnIv.makeVisible()
    }


    fun hideRightButton() {
        if(rightBtnIv.isGone) {
            return
        }

        rightBtnIv.makeGone()
    }


    fun showInboxButton() {
        if(inboxIrv.isVisible) {
            return
        }

        inboxIrv.makeVisible()
    }


    fun hideInboxButton() {
        if(inboxIrv.isGone) {
            return
        }

        inboxIrv.makeGone()
    }


    fun showAlertPriceButton() {
        if(alertPriceIv.isVisible) {
            return
        }

        alertPriceIv.makeVisible()
    }


    fun hideAlertPriceButton() {
        if(alertPriceIv.isGone) {
            return
        }

        alertPriceIv.makeGone()
    }


    fun invisibleAlertPriceButton() {
        if(alertPriceIv.isInvisible) {
            return
        }

        alertPriceIv.makeInvisible()
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


    private fun setVisibleInboxButton(isVisible: Boolean) {
        if (isVisible) {
            inboxIrv.makeVisible()
            setInboxButtonCountMessage(DEFAULT_INBOX_BUTTON_COUNT_MESSAGE)
        } else {
            inboxIrv.makeGone()
        }
    }


    private fun setVisibleAlertPriceButton(isVisible: Boolean) {
        alertPriceIv.isVisible = isVisible
    }


    fun setButtonDrawableColor(@ColorInt color: Int) {
        setLeftButtonDrawableColor(color)
        setPreRightButtonDrawableColor(color)
        setRightButtonDrawableColor(color)
    }


    fun setLeftButtonDrawableColor(@ColorInt color: Int) {
        leftBtnIv.setColor(color)
    }


    fun setPreRightButtonDrawableColor(@ColorInt color: Int) {
        preRightBtnIv.setColor(color)
    }


    fun setRightButtonDrawableColor(@ColorInt color: Int) {
        rightBtnIv.setColor(color)
    }


    fun setProgressBarColor(@ColorInt color: Int) {
        progressBar.setColor(color)
    }


    fun setTitleColor(@ColorInt color: Int) {
        titleTv.setTextColor(color)
    }


    fun setInboxButtonCountMessage(count: Int) {
        inboxIrv.setInboxCountMessage(count)
    }


    fun setTitleText(text: String) {
        titleTv.text = text
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
            showLeftButton()
        }
    }


    /**
     * Sets a drawable of the pre right button.
     *
     * @param drawable The drawable to set
     * @param showIfHidden Whether to show the button if it is hidden.
     * Default is true.
     */
    fun setPreRightButtonDrawable(drawable: Drawable?, showIfHidden: Boolean = true) {
        preRightBtnIv.setImageDrawable(drawable)

        if(showIfHidden) {
            showPreRightButton()
        }
    }


    /**
     * Sets a drawable of the right button.
     *
     * @param drawable The drawable to set
     * @param showIfHidden Whether to show the button if it is hidden.
     * Default is true.
     */
    fun setRightButtonDrawable(drawable: Drawable?, showIfHidden: Boolean = true) {
        rightBtnIv.setImageDrawable(drawable)

        if(showIfHidden) {
            showRightButton()
        }
    }

    /**
     * Sets a drawable of the alert price button.
     *
     * @param drawable The drawable to set
     * @param showIfHidden Whether to show the button if it is hidden.
     * Default is true.
     */
    fun setAlertPriceButtonDrawable(drawable: Drawable?, showIfHidden: Boolean = true) {
        alertPriceIv.setImageDrawable(drawable)

        if(showIfHidden) {
            showAlertPriceButton()
        }
    }


    fun setOnLeftButtonClickListener(listener: (View) -> Unit) {
        leftBtnIv.setOnClickListener(listener)
    }


    fun setOnPreRightButtonClickListener(listener: (View) -> Unit) {
        preRightBtnIv.setOnClickListener(listener)
    }


    fun setOnRightButtonClickListener(listener: (View) -> Unit) {
        rightBtnIv.setOnClickListener(listener)
    }


    fun setOnInboxButtonClickListener(listener: (View) -> Unit) {
        inboxIrv.setOnClickListener(listener)
    }


    fun setOnAlertPriceButtonClickListener(listener: (View) -> Unit) {
        alertPriceIv.setOnClickListener(listener)
    }


    override fun getLayoutResourceId(): Int = R.layout.toolbar_layout


    fun getLeftButtonIv(): ImageView = leftBtnIv


    fun getPreRightButtonIv(): ImageView = preRightBtnIv


    fun getRightButtonIv(): ImageView = rightBtnIv


    fun getProgressBar(): ProgressBar = progressBar


    fun getAlertPriceIv(): ImageView = alertPriceIv


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(
                View.resolveSize(defaultToolbarHeight, heightMeasureSpec),
                MeasureSpec.EXACTLY
            )
        )
    }


}