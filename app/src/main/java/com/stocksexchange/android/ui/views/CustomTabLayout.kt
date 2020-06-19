package com.stocksexchange.android.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

class CustomTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val tabsHolderView = ((getChildAt(0) as? ViewGroup) ?: return)

        if(tabCount > 0) {
            val tabMinWidth = (resources.displayMetrics.widthPixels / tabCount)

            for(i in 0 until tabCount) {
                tabsHolderView.getChildAt(i)?.minimumWidth = tabMinWidth
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


}