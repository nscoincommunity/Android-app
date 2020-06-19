package com.stocksexchange.android.ui.views.mapviews

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.android.ui.views.base.interfaces.AdvancedView
import com.stocksexchange.android.ui.views.mapviews.data.MapViewData
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.vertical_map_view_layout.view.*

/**
 * A container that holds two TextView widgets where one acts
 * as a key and second one acts as a value. The widgets are placed
 * vertically (first comes the key, then the value), hence the name.
 */
class VerticalMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr), AdvancedView<MapViewData> {


    companion object {

        const val SHADOW_NONE = 0b0000
        const val SHADOW_TOP = 0b0001
        const val SHADOW_BOTTOM = 0b0010
        const val SHADOW_LEFT = 0b0100
        const val SHADOW_RIGHT = 0b1000
        const val SHADOW_ALL = 0b1111

        private const val ATTRIBUTE_KEY_TITLE_COLOR = "title_color"
        private const val ATTRIBUTE_KEY_SUBTITLE_COLOR = "subtitle_color"
        private const val ATTRIBUTE_KEY_PROGRESS_BAR_COLOR = "progress_bar_color"
        private const val ATTRIBUTE_KEY_INFO_VIEW_CAPTION_COLOR = "info_view_caption_color"
        private const val ATTRIBUTE_KEY_TITLE_SIZE = "title_size"
        private const val ATTRIBUTE_KEY_SUBTITLE_SIZE = "subtitle_size"
        private const val ATTRIBUTE_KEY_VERTICAL_SHADOW_SIZE = "vertical_shadow_size"
        private const val ATTRIBUTE_KEY_HORIZONTAL_SHADOW_SIZE = "horizontal_shadow_size"
        private const val ATTRIBUTE_KEY_SHADOWS = "shadows"

        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_PROGRESS_BAR_COLOR = Color.GREEN
        private const val DEFAULT_TEXT_SIZE = 14f
        private const val DEFAULT_SHADOW_SIZE_ID = R.dimen.vertical_map_view_shadow_size
        private const val DEFAULT_SHADOW = SHADOW_NONE

        private const val MAIN_VIEW_ANIMATION_DURATION = 300L

    }


    private var viewHeight: Int = dimenInPx(R.dimen.vertical_map_view_height)

    private var mData: MapViewData? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.VerticalMapView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_TITLE_COLOR, getColor(R.styleable.VerticalMapView_titleColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_SUBTITLE_COLOR, getColor(R.styleable.VerticalMapView_subtitleColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, getColor(R.styleable.VerticalMapView_progressBarColor, DEFAULT_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_KEY_INFO_VIEW_CAPTION_COLOR, getColor(R.styleable.VerticalMapView_infoViewCaptionColor, DEFAULT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_TITLE_SIZE, getDimension(R.styleable.VerticalMapView_titleSize, spToPx(DEFAULT_TEXT_SIZE)))
                save(ATTRIBUTE_KEY_SUBTITLE_SIZE, getDimension(R.styleable.VerticalMapView_subtitleSize, spToPx(DEFAULT_TEXT_SIZE)))
                save(ATTRIBUTE_KEY_VERTICAL_SHADOW_SIZE, getDimensionPixelSize(R.styleable.VerticalMapView_verticalShadowSize, dimenInPx(DEFAULT_SHADOW_SIZE_ID)))
                save(ATTRIBUTE_KEY_HORIZONTAL_SHADOW_SIZE, getDimensionPixelSize(R.styleable.VerticalMapView_horizontalShadowSize, dimenInPx(DEFAULT_SHADOW_SIZE_ID)))
                save(ATTRIBUTE_KEY_SHADOWS, getInt(R.styleable.VerticalMapView_shadows, DEFAULT_SHADOW))
            }
        }
    }


    override fun init() {
        super<BaseRelativeLayoutView>.init()

        isClickable = true

        initProgressBar()
        initInfoView()
    }


    private fun initProgressBar() {
        hideProgressBar()
    }


    private fun initInfoView() {
        hideInfoView()
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setTitleColor(get(ATTRIBUTE_KEY_TITLE_COLOR, DEFAULT_TEXT_COLOR))
            setSubtitleColor(get(ATTRIBUTE_KEY_SUBTITLE_COLOR, DEFAULT_TEXT_COLOR))
            setProgressBarColor(get(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, DEFAULT_PROGRESS_BAR_COLOR))
            setInfoViewCaptionColor(get(ATTRIBUTE_KEY_INFO_VIEW_CAPTION_COLOR, DEFAULT_TEXT_COLOR))
            setTitleSize(get(ATTRIBUTE_KEY_TITLE_SIZE, spToPx(DEFAULT_TEXT_SIZE)))
            setSubtitleSize(get(ATTRIBUTE_KEY_SUBTITLE_SIZE, spToPx(DEFAULT_TEXT_SIZE)))
            setVerticalShadowSize(get(ATTRIBUTE_KEY_VERTICAL_SHADOW_SIZE, dimenInPx(DEFAULT_SHADOW_SIZE_ID)))
            setHorizontalShadowSize(get(ATTRIBUTE_KEY_HORIZONTAL_SHADOW_SIZE, dimenInPx(DEFAULT_SHADOW_SIZE_ID)))
            setShadows(get(ATTRIBUTE_KEY_SHADOWS, DEFAULT_SHADOW))
        }
    }


    override fun showMainView(shouldAnimate: Boolean) {
        if(shouldAnimate) {
            mainContainerLl.alpha = 0f
            mainContainerLl
                .animate()
                .alpha(1f)
                .setInterpolator(LinearInterpolator())
                .setDuration(MAIN_VIEW_ANIMATION_DURATION)
                .start()
        } else {
            mainContainerLl.alpha = 1f
        }
    }


    override fun hideMainView() {
        mainContainerLl.animate().cancel()

        if(mainContainerLl.alpha != 0f) {
            mainContainerLl.alpha = 0f
        }
    }


    override fun showProgressBar() {
        progressBar.makeVisible()
    }


    override fun hideProgressBar() {
        progressBar.makeGone()
    }


    override fun showEmptyView(caption: String) {
        infoViewTv.text = caption
        infoViewTv.makeVisible()
    }


    override fun showErrorView(caption: String) {
        infoViewTv.text = caption
        infoViewTv.makeVisible()
    }


    override fun hideInfoView() {
        infoViewTv.makeGone()
    }


    fun showTopShadowView() {
        topShadowView.makeVisible()
    }


    fun showBottomShadowView() {
        bottomShadowView.makeVisible()
    }


    fun showLeftShadowView() {
        leftShadowView.makeVisible()
    }


    fun showRightShadowView() {
        rightShadowView.makeVisible()
    }


    fun setVerticalShadowSize(size: Int) {
        topShadowView.setHeight(size)
        bottomShadowView.setHeight(size)
    }


    fun setHorizontalShadowSize(size: Int) {
        leftShadowView.setWidth(size)
        rightShadowView.setWidth(size)
    }


    fun setTitleMultilineTextEnabled(isMultilineTextEnabled: Boolean) {
        titleTv.setMultilineTextEnabled(isMultilineTextEnabled)
    }


    fun setSubtitleMultilineTextEnabled(isMultilineTextEnabled: Boolean) {
        subtitleTv.setMultilineTextEnabled(isMultilineTextEnabled)
    }


    fun setTitleColor(color: Int) {
        titleTv.setTextColor(color)
    }


    fun setSubtitleColor(color: Int) {
        subtitleTv.setTextColor(color)
    }


    fun setProgressBarColor(color: Int) {
        progressBar.setColor(color)
    }


    fun setInfoViewCaptionColor(@ColorInt color: Int) {
        infoViewTv.setTextColor(color)
    }


    fun setTitleSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        titleTv.setTextSize(unit, textSize)
    }


    fun setSubtitleSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        subtitleTv.setTextSize(unit, textSize)
    }


    fun setShadows(shadows: Int) {
        if(shadows == SHADOW_NONE) {
            return
        }

        if(shadows.containsBits(SHADOW_TOP)) {
            showTopShadowView()
        }

        if(shadows.containsBits(SHADOW_BOTTOM)) {
            showBottomShadowView()
        }

        if(shadows.containsBits(SHADOW_LEFT)) {
            showLeftShadowView()
        }

        if(shadows.containsBits(SHADOW_RIGHT)) {
            showRightShadowView()
        }
    }


    override fun setData(data: MapViewData?, shouldBindData: Boolean) {
        mData = data

        if(shouldBindData) {
            bindData()
        }
    }


    fun updateData(data: MapViewData) {
        if(isDataEmpty()) {
            setData(data, true)
            return
        }

        val oldData = mData!!
        setData(data, false)

        if(data.title != oldData.title) {
            titleTv.crossFadeText(data.title)
        }

        if(data.subtitle != oldData.subtitle) {
            subtitleTv.crossFadeText(data.subtitle)
        }
    }


    override fun bindData() {
        if(isDataEmpty()) {
            return
        }

        val data = mData!!

        titleTv.text = data.title
        subtitleTv.text = data.subtitle
    }


    override fun clearData() {
        mData = null
    }


    override fun isDataEmpty(): Boolean {
        return (mData == null)
    }


    override fun getLayoutResourceId(): Int = R.layout.vertical_map_view_layout


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val adjustedHeightMeasureSpec = if(heightMode == MeasureSpec.EXACTLY) {
            MeasureSpec.makeMeasureSpec(View.resolveSize(viewHeight, heightMeasureSpec), MeasureSpec.EXACTLY)
        } else {
            heightMeasureSpec
        }

        super.onMeasure(widthMeasureSpec, adjustedHeightMeasureSpec)
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state.fetchProperState())

        if(state is SavedState) {
            setData(state.data, true)
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).apply {
            data = mData
        }
    }


    private class SavedState : BaseSavedState {

        companion object {

            private const val KEY_DATA = "data"


            @JvmField
            var CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }

            }

        }


        internal var data: MapViewData? = null


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                data = getParcelable(KEY_DATA)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putParcelable(KEY_DATA, data)
            })
        }

    }


}