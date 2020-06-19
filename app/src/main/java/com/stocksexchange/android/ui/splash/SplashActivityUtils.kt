package com.stocksexchange.android.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.model.Shortcut
import com.stocksexchange.core.utils.extensions.intentFor


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        shortcutName = getString(ExtrasKeys.KEY_SHORTCUT_NAME, ""),
        destinationIntent = getParcelable(ExtrasKeys.KEY_DESTINATION_INTENT)
    )
}


internal object ExtrasKeys {

    const val KEY_SHORTCUT_NAME = "shortcut_name"
    const val KEY_DESTINATION_INTENT = "destination_intent"

}


internal data class Extras(
    val shortcutName: String,
    val destinationIntent: Intent?
) {

    val hasShortcutName: Boolean
        get() = shortcutName.isNotBlank()

    val hasDestinationIntent: Boolean
        get() = (destinationIntent != null)

}


fun SplashActivity.Companion.newInstance(
    context: Context,
    shortcut: Shortcut
) : Intent {
    return context.intentFor<SplashActivity>().apply {
        putExtra(ExtrasKeys.KEY_SHORTCUT_NAME, shortcut.name)
    }
}


fun SplashActivity.Companion.newInstance(
    context: Context,
    destinationIntent: Intent? = null
) : Intent {
    return context.intentFor<SplashActivity>().apply {
        putExtra(ExtrasKeys.KEY_DESTINATION_INTENT, destinationIntent)
    }
}