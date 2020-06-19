package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseLinearLayoutView
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.info_view_layout.view.*

/**
 * A view container showing an informational view
 * in case there is no data or error has occurred.
 */
class InfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLinearLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_ICON_COLOR = "icon_color"
        private const val ATTRIBUTE_KEY_ICON_SIZE = "icon_size"
        private const val ATTRIBUTE_KEY_ICON = "icon"
        private const val ATTRIBUTE_KEY_CAPTION_COLOR = "caption_color"
        private const val ATTRIBUTE_KEY_CAPTION_TEXT_SIZE = "caption_text_size"
        private const val ATTRIBUTE_KEY_CAPTION = "caption"
        private const val ATTRIBUTE_KEY_VIEWS_VERTICAL_DISTANCE = "views_vertical_distance"

        private const val DEFAULT_ICON_COLOR = Color.WHITE
        private const val DEFAULT_ICON_SIZE = R.dimen.info_view_icon_size
        private const val DEFAULT_ICON = R.mipmap.ic_information_stub

        private const val DEFAULT_CAPTION_COLOR = Color.WHITE
        private const val DEFAULT_CAPTION_TEXT_SIZE = R.dimen.info_view_caption_text_size
        private const val DEFAULT_CAPTION = ""

        private const val DEFAULT_VIEWS_VERTICAL_DISTANCE = R.dimen.info_view_margin_bottom

    }




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.InfoView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_ICON_COLOR, getColor(R.styleable.InfoView_iconColor, DEFAULT_ICON_COLOR))
                save(ATTRIBUTE_KEY_ICON_SIZE, getDimensionPixelSize(R.styleable.InfoView_iconSize, context.resources.getDimensionPixelSize(DEFAULT_ICON_SIZE)))
                save(ATTRIBUTE_KEY_ICON, getDrawable(R.styleable.InfoView_icon) ?: getCompatDrawable(DEFAULT_ICON))
                save(ATTRIBUTE_KEY_CAPTION_COLOR, getColor(R.styleable.InfoView_captionColor, DEFAULT_CAPTION_COLOR))
                save(ATTRIBUTE_KEY_CAPTION_TEXT_SIZE, getDimension(R.styleable.InfoView_captionTextSize, dimen(DEFAULT_CAPTION_TEXT_SIZE)))
                save(ATTRIBUTE_KEY_CAPTION, getString(R.styleable.InfoView_caption) ?: DEFAULT_CAPTION)
                save(ATTRIBUTE_KEY_VIEWS_VERTICAL_DISTANCE, getDimensionPixelSize(R.styleable.InfoView_viewsVerticalDistance, context.resources.getDimensionPixelSize(DEFAULT_VIEWS_VERTICAL_DISTANCE)))
            }
        }
    }


    override fun init() {
        super.init()

        orientation = VERTICAL
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setIconColor(get(ATTRIBUTE_KEY_ICON_COLOR, DEFAULT_ICON_COLOR))
            setIconSize(get(ATTRIBUTE_KEY_ICON_SIZE, context.resources.getDimensionPixelSize(DEFAULT_ICON_SIZE)))
            setIcon(get(ATTRIBUTE_KEY_ICON, getCompatDrawable(DEFAULT_ICON)))
            setCaptionColor(get(ATTRIBUTE_KEY_CAPTION_COLOR, DEFAULT_CAPTION_COLOR))
            setCaptionTextSize(get(ATTRIBUTE_KEY_CAPTION_TEXT_SIZE, dimen(DEFAULT_CAPTION_TEXT_SIZE)))
            setCaption(get(ATTRIBUTE_KEY_CAPTION, DEFAULT_CAPTION))
            setViewsVerticalDistance(get(ATTRIBUTE_KEY_VIEWS_VERTICAL_DISTANCE, context.resources.getDimensionPixelSize(DEFAULT_VIEWS_VERTICAL_DISTANCE)))
        }
    }


    fun showIcon() {
        iconIv.makeVisible()
    }


    fun hideIcon() {
        iconIv.makeGone()
    }


    fun showCaption() {
        captionTv.makeVisible()
    }


    fun hideCaption() {
        captionTv.makeGone()
    }


    fun setIconColor(@ColorInt color: Int) {
        iconIv.setColor(color)
    }


    fun setIconSize(size: Int) {
        iconIv.setSize(size)
    }


    fun setIcon(drawable: Drawable?) {
        iconIv.setImageDrawable(drawable)
    }


    fun setCaption(text: String) {
        captionTv.text = text
    }


    fun setCaptionColor(@ColorInt color: Int) {
        captionTv.setTextColor(color)
    }


    fun setCaptionTextSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        captionTv.setTextSize(unit, textSize)
    }


    fun setColor(@ColorInt color: Int) {
        setIconColor(color)
        setCaptionColor(color)
    }


    /**
     * Sets a vertical distance between two views (icon and caption).
     *
     * @param distance The distance to set
     */
    fun setViewsVerticalDistance(distance: Int) {
        iconIv.setBottomMargin(distance)
    }


    override fun getLayoutResourceId(): Int = R.layout.info_view_layout


}