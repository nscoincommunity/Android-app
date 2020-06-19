package com.stocksexchange.android.ui.views.mapviews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseLinearLayoutView
import com.stocksexchange.core.utils.extensions.setTypefaceStyle
import com.stocksexchange.core.utils.extensions.spToPx
import kotlinx.android.synthetic.main.simple_map_view_layout.view.*

/**
 * A container that holds two TextView widgets where one acts
 * as a key and second one acts as a value. Separated by a simple
 * delimiter (like colon), hence the name.
 */
class SimpleMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLinearLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_TITLE_COLOR = "title_color"
        private const val ATTRIBUTE_KEY_VALUE_COLOR = "value_color"
        private const val ATTRIBUTE_KEY_TITLE_SIZE = "title_size"
        private const val ATTRIBUTE_KEY_VALUE_SIZE = "value_size"
        private const val ATTRIBUTE_KEY_TITLE_TEXT = "title_text"
        private const val ATTRIBUTE_KEY_VALUE_TEXT = "vlaue_text"

        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_TEXT_SIZE = 14f
        private const val DEFAULT_TEXT = ""

    }




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.SimpleMapView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_TITLE_COLOR, getColor(R.styleable.SimpleMapView_titleColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_VALUE_COLOR, getColor(R.styleable.SimpleMapView_valueColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_TITLE_SIZE, getDimension(R.styleable.SimpleMapView_titleSize, spToPx(DEFAULT_TEXT_SIZE)))
                save(ATTRIBUTE_KEY_VALUE_SIZE, getDimension(R.styleable.SimpleMapView_valueSize, spToPx(DEFAULT_TEXT_SIZE)))
                save(ATTRIBUTE_KEY_TITLE_TEXT, getString(R.styleable.SimpleMapView_title) ?: DEFAULT_TEXT)
                save(ATTRIBUTE_KEY_VALUE_TEXT, getString(R.styleable.SimpleMapView_value) ?: DEFAULT_TEXT)
            }
        }
    }


    override fun init() {
        super.init()

        orientation = HORIZONTAL
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setTitleColor(get(ATTRIBUTE_KEY_TITLE_COLOR, DEFAULT_TEXT_COLOR))
            setValueColor(get(ATTRIBUTE_KEY_VALUE_COLOR, DEFAULT_TEXT_COLOR))
            setTitleSize(get(ATTRIBUTE_KEY_TITLE_SIZE, spToPx(DEFAULT_TEXT_SIZE)))
            setValueSize(get(ATTRIBUTE_KEY_VALUE_SIZE, spToPx(DEFAULT_TEXT_SIZE)))
            setTitleText(get(ATTRIBUTE_KEY_TITLE_TEXT, DEFAULT_TEXT))
            setValueText(get(ATTRIBUTE_KEY_VALUE_TEXT, DEFAULT_TEXT))
        }
    }


    fun setTitleColor(@ColorInt color: Int) {
        titleTv.setTextColor(color)
    }


    fun setValueColor(@ColorInt color: Int) {
        valueTv.setTextColor(color)
    }


    fun setTextColor(@ColorInt color: Int) {
        setTitleColor(color)
        setValueColor(color)
    }


    fun setTitleTypfaceStyle(style: Int) {
        titleTv.setTypefaceStyle(style)
    }


    fun setValueTypefaceStyle(style: Int) {
        valueTv.setTypefaceStyle(style)
    }


    fun setTitleSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        titleTv.setTextSize(unit, textSize)
    }


    fun setValueSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        valueTv.setTextSize(unit, textSize)
    }


    fun setTitleText(text: String) {
        titleTv.text = text
    }


    fun setValueText(text: String) {
        valueTv.text = text
    }


    override fun getLayoutResourceId(): Int = R.layout.simple_map_view_layout


}