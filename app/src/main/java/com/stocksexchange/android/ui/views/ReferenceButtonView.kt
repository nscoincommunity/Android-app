package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.core.utils.extensions.dimenInPx
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import com.stocksexchange.core.utils.extensions.setHorizontalPadding
import kotlinx.android.synthetic.main.reference_button_view_layout.view.*

/**
 * A view representing a reference to some URL by showing an icon,
 * a title and a subtitle.
 */
class ReferenceButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_SUBTITLE = "subtitle"
        private const val ATTRIBUTE_KEY_TITLE = "title"
        private const val ATTRIBUTE_KEY_ICON = "icon"

        private const val DEFAULT_TEXT = ""
        private const val DEFAULT_ICON = R.mipmap.ic_launcher

    }




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.ReferenceButtonView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_SUBTITLE, (getText(R.styleable.ReferenceButtonView_subtitle) ?: DEFAULT_TEXT))
                save(ATTRIBUTE_KEY_TITLE, (getText(R.styleable.ReferenceButtonView_title) ?: DEFAULT_TEXT))
                save(ATTRIBUTE_KEY_ICON, (getDrawable(R.styleable.ReferenceButtonView_icon) ?: getCompatDrawable(DEFAULT_ICON)))
            }
        }
    }


    override fun init() {
        super.init()

        setHorizontalPadding(dimenInPx(R.dimen.reference_button_view_horizontal_padding))
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setSubtitleText(get(ATTRIBUTE_KEY_SUBTITLE, DEFAULT_TEXT))
            setTitleText(get(ATTRIBUTE_KEY_TITLE, DEFAULT_TEXT))
            setIconDrawable(get(ATTRIBUTE_KEY_ICON, getCompatDrawable(DEFAULT_ICON)))
        }
    }


    fun setTextColor(color: Int) {
        subtitleTv.setTextColor(color)
        titleTv.setTextColor(color)
    }


    fun setSubtitleText(text: String) {
        subtitleTv.text = text
    }


    fun setTitleText(text: String) {
        titleTv.text = text
    }


    fun setIconDrawable(drawable: Drawable?) {
        iconIv.setImageDrawable(drawable)
    }


    override fun getLayoutResourceId(): Int = R.layout.reference_button_view_layout


}