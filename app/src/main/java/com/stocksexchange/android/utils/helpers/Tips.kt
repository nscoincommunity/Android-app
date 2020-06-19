package com.stocksexchange.android.utils.helpers

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.stocksexchange.android.theming.model.Theme


fun showTip(
    activity: Activity,
    targetView: View,
    theme: Theme,
    title: String,
    description: String,
    listener: TapTargetView.Listener? = null
) : TapTargetView {
    return TapTargetView.showFor(
        activity,
        createTip(
            targetView = targetView,
            theme = theme,
            title = title,
            description = description
        ),
        listener
    )
}


private fun createTip(
    targetView: View,
    theme: Theme,
    title: String,
    description: String
) : TapTarget {
    return TapTarget.forView(targetView, title, description)
        .id(targetView.id)
        .outerCircleColorInt(theme.generalTheme.primaryColor)
        .targetCircleColorInt(theme.generalTheme.accentColor)
        .titleTextColorInt(theme.generalTheme.primaryTextColor)
        .descriptionTextColorInt(theme.generalTheme.secondaryTextColor)
        .dimColorInt(Color.BLACK)
        .outerCircleAlpha(0.9f)
        .titleTextSize(20)
        .descriptionTextSize(14)
        .cancelable(true)
        .tintTarget(false)
}