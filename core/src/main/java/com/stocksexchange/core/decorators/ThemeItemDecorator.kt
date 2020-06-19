package com.stocksexchange.core.decorators

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stocksexchange.core.utils.extensions.containsBits

/**
 * An item decorator for applying horizontal spacing
 * between RecyclerView items.
 */
class ThemeItemDecorator(
    spacing: Int,
    private val spanCount: Int
) : DefaultSpacingItemDecorator(
    spacing = spacing,
    sideFlags = (SIDE_LEFT or SIDE_RIGHT)
) {


    override fun shouldAssignSpacing(view: View, parent: RecyclerView): Boolean {
        return when {
            sideFlags.containsBits(SIDE_RIGHT) -> (parent.getChildAdapterPosition(view) == 0)
            sideFlags.containsBits(SIDE_LEFT) -> (parent.getChildAdapterPosition(view) == spanCount)

            else -> super.shouldAssignSpacing(view, parent)
        }
    }


}