package com.stocksexchange.core.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * An item decorator for applying horizontal and bottom spacing
 * for orderbook items.
 */
abstract class OrderbookItemDecorator(
    private val spanCount: Int,
    private val horizontalSpacing: Int,
    private val bottomSpacing: Int
) : RecyclerView.ItemDecoration() {


    private var position: Int = -1

    private var column: Int = -1

    private var lastRowItemPositions: IntArray = IntArray(spanCount)




    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        position = parent.getChildAdapterPosition(view)

        for(i in 0 until spanCount) {
            lastRowItemPositions[i] = (parent.adapter!!.itemCount - 1 - i)
        }

        if(spanCount == 1) {
            outRect.right = horizontalSpacing
            outRect.left = horizontalSpacing
        } else {
            column = (position % spanCount)

            if(column == 0) {
                outRect.right = horizontalSpacing
            } else if(column == spanCount) {
                outRect.left = horizontalSpacing
            }
        }

        if(!isHeader(position, view) &&
            (position !in lastRowItemPositions)) {
            outRect.bottom = bottomSpacing
        }
    }


    /**
     * Checks whether the specified item at the [adapterPosition] is
     * a header or not.
     *
     * @param adapterPosition The position of the item
     * @param view The view of the item
     *
     * @return true if header; false otherwise
     */
    abstract fun isHeader(adapterPosition: Int, view: View): Boolean


}