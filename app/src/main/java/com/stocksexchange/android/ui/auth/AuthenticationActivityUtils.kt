package com.stocksexchange.android.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.model.PinCodeMode
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow
import com.stocksexchange.core.utils.extensions.intentFor


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        pinCodeMode = getSerializableOrThrow(ExtrasKeys.KEY_PIN_CODE_MODE),
        transitionAnimations = getSerializableOrThrow(ExtrasKeys.KEY_TRANSITION_ANIMATIONS),
        theme = getSerializableOrThrow(ExtrasKeys.KEY_THEME),
        destinationIntent = getParcelable(ExtrasKeys.KEY_DESTINATION_INTENT)
    )
}


internal val activityStateExtractor: (Bundle.() -> ActivityState) = {
    ActivityState(
        destinationIntent = getParcelable(ActivityStateKeys.KEY_DESTINATION_INTENT)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        areAuthVariablesInitialized = getOrThrow(PresenterStateKeys.KEY_ARE_AUTH_VARIABLES_INITIALIZED),
        isTimerCancelled = getOrThrow(PresenterStateKeys.KEY_IS_TIMER_CANCELLED),
        pinCode = getOrThrow(PresenterStateKeys.KEY_PIN_CODE),
        confirmedPinCode = getOrThrow(PresenterStateKeys.KEY_CONFIRMED_PIN_CODE),
        pinCodeMode = getOrThrow(PresenterStateKeys.KEY_PIN_CODE_MODE),
        transitionAnimations = getOrThrow(PresenterStateKeys.KEY_TRANSITION_ANIMATIONS),
        theme = getOrThrow(PresenterStateKeys.KEY_THEME)
    )
}


internal object ExtrasKeys {

    const val KEY_PIN_CODE_MODE = "pin_code_mode"
    const val KEY_TRANSITION_ANIMATIONS = "transition_animations"
    const val KEY_THEME = "theme"
    const val KEY_DESTINATION_INTENT = "destination_intent"

}


internal object ActivityStateKeys {

    const val KEY_DESTINATION_INTENT = "destination_intent"

}


internal object PresenterStateKeys {

    const val KEY_ARE_AUTH_VARIABLES_INITIALIZED = "are_auth_variables_initialized"
    const val KEY_IS_TIMER_CANCELLED = "is_timer_cancelled"
    const val KEY_PIN_CODE = "pin_code"
    const val KEY_CONFIRMED_PIN_CODE = "confirmed_pin_code"
    const val KEY_PIN_CODE_MODE = "pin_code_mode"
    const val KEY_TRANSITION_ANIMATIONS = "transition_animations"
    const val KEY_THEME = "theme"

}


internal data class Extras(
    val pinCodeMode: PinCodeMode,
    val transitionAnimations: TransitionAnimations,
    val theme: Theme,
    val destinationIntent: Intent?
)


internal data class ActivityState(
    val destinationIntent: Intent?
)


internal data class PresenterState(
    val areAuthVariablesInitialized: Boolean,
    val isTimerCancelled: Boolean,
    val pinCode: String,
    val confirmedPinCode: String,
    val pinCodeMode: PinCodeMode,
    val transitionAnimations: TransitionAnimations,
    val theme: Theme
)


internal fun Bundle.saveState(state: ActivityState) {
    putParcelable(ActivityStateKeys.KEY_DESTINATION_INTENT, state.destinationIntent)
}


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_ARE_AUTH_VARIABLES_INITIALIZED, state.areAuthVariablesInitialized)
    save(PresenterStateKeys.KEY_IS_TIMER_CANCELLED, state.isTimerCancelled)
    save(PresenterStateKeys.KEY_PIN_CODE, state.pinCode)
    save(PresenterStateKeys.KEY_CONFIRMED_PIN_CODE, state.confirmedPinCode)
    save(PresenterStateKeys.KEY_PIN_CODE_MODE, state.pinCodeMode)
    save(PresenterStateKeys.KEY_TRANSITION_ANIMATIONS, state.transitionAnimations)
    save(PresenterStateKeys.KEY_THEME, state.theme)
}


fun AuthenticationActivity.Companion.newInstance(
    context: Context,
    pinCodeMode: PinCodeMode,
    transitionAnimations: TransitionAnimations,
    theme: Theme,
    destinationIntent: Intent? = null
): Intent {
    return context.intentFor<AuthenticationActivity>().apply {
        putExtra(ExtrasKeys.KEY_PIN_CODE_MODE, pinCodeMode)
        putExtra(ExtrasKeys.KEY_TRANSITION_ANIMATIONS, transitionAnimations)
        putExtra(ExtrasKeys.KEY_THEME, theme)
        putExtra(ExtrasKeys.KEY_DESTINATION_INTENT, destinationIntent)
    }
}