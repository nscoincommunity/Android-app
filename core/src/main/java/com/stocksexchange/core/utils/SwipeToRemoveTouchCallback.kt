package com.stocksexchange.core.utils

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.stocksexchange.core.utils.extensions.dpToPx
import kotlin.math.abs

open class SwipeToRemoveTouchCallback(
    context: Context,
    val listener: Listener
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {


    companion object {

        private const val DEFAULT_PADDING_IN_DP = 10

    }


    private val isLabelSet: Boolean
        get() = labelText.isNotBlank()

    private val isIconSet: Boolean
        get() = (icon != null)

    var padding: Int = context.dpToPx(DEFAULT_PADDING_IN_DP)

    @get:ColorInt
    var labelTextColor: Int
        set(@ColorInt color) {
            labelTextPaint.color = color
        }
        get() = labelTextPaint.color

    var labelTextSize: Float
        set(value) { labelTextPaint.textSize = value }
        get() = labelTextPaint.textSize

    var labelText: String = ""

    private val labelTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val labelTextBounds: Rect = Rect()

    var icon: Bitmap? = null




    init {
        initLabelTextPaint()
    }


    private fun initLabelTextPaint() {
        labelTextPaint.typeface = Typeface.DEFAULT_BOLD
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemSwiped(viewHolder)
    }


    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        canvas.drawLabelWithIcon(viewHolder.itemView, isCurrentlyActive)

        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }


    private fun Canvas.drawLabelWithIcon(itemView: View, isCurrentlyActive: Boolean) {
        if(isLabelSet) {
            labelTextPaint.getTextBounds(labelText, 0, labelText.length, labelTextBounds)
        }

        val itemViewWidth = itemView.measuredWidth
        val itemViewHeight = itemView.measuredHeight
        val itemViewX = itemView.x
        val itemViewY = itemView.y

        val iconWidth = (if(isIconSet) (icon?.width ?: 0) else 0)
        val iconHeight = (if(isIconSet) (icon?.height ?: 0) else 0)
        val iconX = (itemViewWidth + itemViewX + padding)
        val iconY = (itemViewY + ((itemViewHeight / 2) - (iconHeight / 2)))

        val textPadding = (if(isIconSet) padding else 0)
        val textX = (iconX + iconWidth + textPadding)
        val textY = (itemViewY + (itemViewHeight / 2) - labelTextBounds.exactCenterY())

        if(isCurrentlyActive || (abs(itemViewX) <= (itemViewWidth / 2))) {
            if(isIconSet) {
                drawBitmap(icon!!, iconX, iconY, labelTextPaint)
            }

            if(isLabelSet) {
                drawText(labelText, textX, textY, labelTextPaint)
            }
        }
    }


    interface Listener {

        fun onItemSwiped(viewHolder: RecyclerView.ViewHolder)

    }


}