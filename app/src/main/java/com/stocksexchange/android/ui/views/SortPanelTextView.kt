package com.stocksexchange.android.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.SortOrder
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.core.utils.extensions.*

/**
 * An implementation of the TextView widget to be used
 * inside [CurrencyMarketsSortPanel].
 */
class SortPanelTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {


    companion object {

        private const val ANIMATION_DURATION = 300L

        private val INTERPOLATOR = LinearInterpolator()

    }


    private var mSelectedTitleTextColor: Int = 0
    private var mUnselectedTitleTextColor: Int = 0

    private lateinit var mArrowDrawables: Map<SortOrder, Drawable?>

    private var mValueAnimator: ValueAnimator = ValueAnimator.ofInt(0, 10000)

    /**
     * Current comparator associated with this TextView.
     */
    lateinit var comparator: CurrencyMarketComparator




    init {
        initArrowDrawables()
        initValueAnimator()
    }


    private fun initArrowDrawables() {
        setArrowDrawableColor(Color.BLACK)
    }


    private fun initValueAnimator() {
        mValueAnimator.addUpdateListener {
            getRightDrawable()?.level = (it.animatedValue as Int)
        }

        mValueAnimator.interpolator = INTERPOLATOR
        mValueAnimator.duration = ANIMATION_DURATION
    }



    override fun setSelected(isSelected: Boolean) {
        super.setSelected(isSelected)

        if(isSelected) {
            setTypefaceStyle(Typeface.BOLD)
            setTextColor(mSelectedTitleTextColor)
            updateDrawable(false)
        } else {
            setTypefaceStyle(Typeface.NORMAL)
            setTextColor(mUnselectedTitleTextColor)
            cancelAnimation()
            clearDrawable()
        }
    }


    fun setSelectedTitleTextColor(@ColorInt color: Int) {
        mSelectedTitleTextColor = color

        if(isSelected) {
            setTextColor(color)
        }
    }


    fun setUnselectedTitleTextColor(@ColorInt color: Int) {
        mUnselectedTitleTextColor = color

        if(!isSelected) {
            setTextColor(color)
        }
    }


    fun setArrowDrawableColor(@ColorInt color: Int) {
        mArrowDrawables = mapOf(
            SortOrder.ASC to context.getColoredCompatDrawable(
                R.drawable.arrow_down_rotation_drawable,
                color
            ),
            SortOrder.DESC to context.getColoredCompatDrawable(
                R.drawable.arrow_up_rotation_drawable,
                color
            )
        )

        if(isSelected) {
            updateDrawable(false)
        }
    }


    /**
     * Toggles the comparator by changing the current one
     * to its opposite as well as updating the drawable.
     */
    fun toggleComparator() {
        comparator = !comparator
        updateDrawable(true)
    }


    private fun updateDrawable(animate: Boolean) {
        val newRightDrawable = mArrowDrawables[comparator.order]

        if(animate) {
            cancelAnimation()

            mValueAnimator.removeAllListeners()
            mValueAnimator.doOnEnd {
                getRightDrawable()?.level = 0
                setRightDrawable(newRightDrawable)
            }
            mValueAnimator.start()
        } else {
            setRightDrawable(newRightDrawable)
        }
    }


    private fun cancelAnimation() {
        if(mValueAnimator.isRunning) {
            mValueAnimator.cancel()
        }
    }


}