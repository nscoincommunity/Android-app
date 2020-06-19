package com.stocksexchange.android.model

import com.stocksexchange.android.R
import com.stocksexchange.android.utils.providers.StringProvider

data class DepthChartTab(
    val position: Int,
    val level: Int
) {


    companion object {

        private const val LAST_LEVEL = Integer.MAX_VALUE

        private const val SECOND_TAB_LEVEL_ADDITION = 15
        private const val THIRD_TAB_LEVEL_ADDITION = (SECOND_TAB_LEVEL_ADDITION + 20)


        fun getDepthChartTabsForDepthLevel(depthLevel: Int): List<DepthChartTab> {
            return listOf(
                DepthChartTab(0, depthLevel),
                DepthChartTab(1, (depthLevel + SECOND_TAB_LEVEL_ADDITION)),
                DepthChartTab(2, (depthLevel + THIRD_TAB_LEVEL_ADDITION)),
                DepthChartTab(3, LAST_LEVEL)
            )
        }

    }


    val isLastLevel: Boolean
        get() = (level == LAST_LEVEL)




    fun getTitle(stringProvider: StringProvider): String {
        return when(level) {
            LAST_LEVEL -> stringProvider.getString(R.string.depth_chart_all_tab_title)
            else -> level.toString()
        }
    }


}