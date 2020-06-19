package com.stocksexchange.android.model

import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class DepthChartLineStyle(
    @StringRes val titleId: Int
) {

    LINEAR(
        titleId = R.string.depth_chart_line_style_linear
    ),
    STEPPED(
        titleId = R.string.depth_chart_line_style_stepped
    )

}