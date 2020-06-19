package com.stocksexchange.android.ui.views.mapviews

import android.content.Context
import android.graphics.Color
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.extensions.getDottedLineDrawable
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.core.utils.extensions.crossFadeText
import com.stocksexchange.core.utils.extensions.spToPx
import kotlinx.android.synthetic.main.dotted_map_view_layout.view.*

/**
 * A container that holds two TextView widgets where one acts
 * as a key and second one acts as a value. Separated by a dotted
 * drawable, hence the name.
 */
class DottedMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_TITLE_COLOR = "title_color"
        private const val ATTRIBUTE_KEY_VALUE_COLOR = "value_color"
        private const val ATTRIBUTE_KEY_SEPARATOR_COLOR = "separator_color"
        private const val ATTRIBUTE_KEY_VISIBILITY = "separator_visibility"
        private const val ATTRIBUTE_KEY_TITLE_SIZE = "title_size"
        private const val ATTRIBUTE_KEY_VALUE_SIZE = "value_size"
        private const val ATTRIBUTE_KEY_TITLE_TEXT = "title_text"
        private const val ATTRIBUTE_KEY_VALUE_TEXT = "value_text"

        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_TEXT_SIZE = 14f
        private const val DEFAULT_TEXT = ""
        private const val DEFAULT_SEPARATOR_VISIBILITY = false

        private const val DEFAULT_ANIMATION_DURATION = 300L

        private val DEFAULT_ANIMATION_INTERPOLATOR = LinearInterpolator()

    }




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.DottedMapView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_TITLE_COLOR, getColor(R.styleable.DottedMapView_titleColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_VALUE_COLOR, getColor(R.styleable.DottedMapView_valueColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_SEPARATOR_COLOR, getColor(R.styleable.DottedMapView_separatorColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_TITLE_SIZE, getDimension(R.styleable.DottedMapView_titleSize, spToPx(DEFAULT_TEXT_SIZE)))
                save(ATTRIBUTE_KEY_VALUE_SIZE, getDimension(R.styleable.DottedMapView_valueSize, spToPx(DEFAULT_TEXT_SIZE)))
                save(ATTRIBUTE_KEY_TITLE_TEXT, getString(R.styleable.DottedMapView_title) ?: DEFAULT_TEXT)
                save(ATTRIBUTE_KEY_VALUE_TEXT, getString(R.styleable.DottedMapView_value) ?: DEFAULT_TEXT)
                save(ATTRIBUTE_KEY_VISIBILITY, getBoolean(R.styleable.DottedMapView_separatorVisibility, DEFAULT_SEPARATOR_VISIBILITY))
            }
        }
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setTitleColor(get(ATTRIBUTE_KEY_TITLE_COLOR, DEFAULT_TEXT_COLOR))
            setValueColor(get(ATTRIBUTE_KEY_VALUE_COLOR, DEFAULT_TEXT_COLOR))
            setSeparatorColor(get(ATTRIBUTE_KEY_SEPARATOR_COLOR, DEFAULT_TEXT_COLOR))
            setTitleSize(get(ATTRIBUTE_KEY_TITLE_SIZE, spToPx(DEFAULT_TEXT_SIZE)))
            setValueSize(get(ATTRIBUTE_KEY_VALUE_SIZE, spToPx(DEFAULT_TEXT_SIZE)))
            setTitleText(get(ATTRIBUTE_KEY_TITLE_TEXT, DEFAULT_TEXT))
            setValueText(get(ATTRIBUTE_KEY_VALUE_TEXT, DEFAULT_TEXT))
            setSeparatorVisibility(get(ATTRIBUTE_KEY_VISIBILITY, DEFAULT_SEPARATOR_VISIBILITY))
        }
    }


    fun setTitleColor(color: Int) {
        titleTv.setTextColor(color)
    }


    fun setValueColor(color: Int) {
        valueTv.setTextColor(color)
    }


    fun setSeparatorColor(@ColorInt color: Int) {
        separatorIv.setImageDrawable(context.getDottedLineDrawable(color))
    }


    /**
     * Sets a color of all the elements (title, value, and separator).
     *
     * @param color The color to set
     */
    fun setColor(@ColorInt color: Int) {
        setTitleColor(color)
        setValueColor(color)
        setSeparatorColor(color)
    }


    fun setTitleSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        titleTv.setTextSize(unit, textSize)
    }


    fun setValueSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        valueTv.setTextSize(unit, textSize)
    }


    fun setTitleText(text: CharSequence) {
        titleTv.text = text
    }


    fun setValueText(text: CharSequence) {
        valueTv.text = text
    }


    fun setSeparatorVisibility(visibility: Boolean) {
        separatorIv.isVisible = visibility
    }


    /**
     * Sets a text of the title by cross fading it with the previous one.
     * Primarily used for changing or updating the text.
     *
     * @param text The text to set
     * @param animationDuration The duration of the animation.
     * By default uses [DEFAULT_ANIMATION_DURATION].
     * @param animationInterpolator The interpolator to use.
     * By default uses [DEFAULT_ANIMATION_INTERPOLATOR].
     */
    fun setTitleTextAnimated(text: CharSequence, animationDuration: Long = DEFAULT_ANIMATION_DURATION,
                             animationInterpolator: Interpolator = DEFAULT_ANIMATION_INTERPOLATOR) {
        titleTv.crossFadeText(text, animationDuration, animationInterpolator)
    }


    /**
     * Sets a text of the value by cross fading it with the previous one.
     * Primarily used for changing or updating the text.
     *
     * @param text The text to set
     * @param animationDuration The duration of the animation.
     * By default uses [DEFAULT_ANIMATION_DURATION].
     * @param animationInterpolator The interpolator to use.
     * By default uses [DEFAULT_ANIMATION_INTERPOLATOR].
     */
    fun setValueTextAnimated(text: CharSequence, animationDuration: Long = DEFAULT_ANIMATION_DURATION,
                             animationInterpolator: Interpolator = DEFAULT_ANIMATION_INTERPOLATOR) {
        valueTv.crossFadeText(text, animationDuration, animationInterpolator)
    }


    fun setTitleMovementMethod(method: MovementMethod) {
        titleTv.movementMethod = method
    }


    fun setValueMovementMethod(method: MovementMethod) {
        valueTv.movementMethod = method
    }


    override fun getLayoutResourceId(): Int = R.layout.dotted_map_view_layout


}