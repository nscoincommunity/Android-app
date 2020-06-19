package com.stocksexchange.android.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.SignInConfirmationType
import com.stocksexchange.core.utils.extensions.*


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        wasAccountJustVerified = getBoolean(ExtrasKeys.KEY_WAS_ACCOUNT_JUST_VERIFIED, false),
        transitionAnimations = getSerializableOrThrow(ExtrasKeys.KEY_TRANSITION_ANIMATIONS),
        destinationIntent = getParcelableOrThrow(ExtrasKeys.KEY_DESTINATION_INTENT)
    )
}


internal val activityStateExtractor: (Bundle.() -> ActivityState) = {
    ActivityState(
        destinationIntent = getParcelableOrThrow(ActivityStateKeys.KEY_DESTINATION_INTENT)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        wasAccountJustVerified = getOrThrow(PresenterStateKeys.KEY_WAS_ACCOUNT_JUST_VERIFIED),
        isAccountVerifiedDialogAlreadyShown = getOrThrow(PresenterStateKeys.KEY_IS_ACCOUNT_VERIFIED_DIALOG_ALREADY_SHOWN),
        confirmationType = getOrThrow(PresenterStateKeys.KEY_CONFIRMATION_TYPE),
        transitionAnimations = getOrThrow(PresenterStateKeys.KEY_TRANSITION_ANIMATIONS)
    )
}


internal object ExtrasKeys {

    const val KEY_WAS_ACCOUNT_JUST_VERIFIED = "was_account_just_verified"
    const val KEY_TRANSITION_ANIMATIONS = "transition_animations"
    const val KEY_DESTINATION_INTENT = "destination_intent"

}


internal object ActivityStateKeys {

    const val KEY_DESTINATION_INTENT = "destination_intent"

}


internal object PresenterStateKeys {

    const val KEY_WAS_ACCOUNT_JUST_VERIFIED = "was_account_just_verified"
    const val KEY_IS_ACCOUNT_VERIFIED_DIALOG_ALREADY_SHOWN = "is_account_verified_dialog_already_shown"
    const val KEY_CONFIRMATION_TYPE = "confirmation_type"
    const val KEY_TRANSITION_ANIMATIONS = "transition_animations"

}


internal data class Extras(
    val wasAccountJustVerified: Boolean,
    val transitionAnimations: TransitionAnimations,
    val destinationIntent: Intent
)


internal data class ActivityState(
    val destinationIntent: Intent
)


internal data class PresenterState(
    val wasAccountJustVerified: Boolean,
    val isAccountVerifiedDialogAlreadyShown: Boolean,
    val confirmationType: SignInConfirmationType,
    val transitionAnimations: TransitionAnimations
)


internal fun Bundle.saveState(state: ActivityState) {
    putParcelable(ActivityStateKeys.KEY_DESTINATION_INTENT, state.destinationIntent)
}


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_WAS_ACCOUNT_JUST_VERIFIED, state.wasAccountJustVerified)
    save(PresenterStateKeys.KEY_IS_ACCOUNT_VERIFIED_DIALOG_ALREADY_SHOWN, state.isAccountVerifiedDialogAlreadyShown)
    save(PresenterStateKeys.KEY_CONFIRMATION_TYPE, state.confirmationType)
    save(PresenterStateKeys.KEY_TRANSITION_ANIMATIONS, state.transitionAnimations)
}


fun LoginActivity.Companion.newInstance(
    context: Context,
    wasAccountJustVerified: Boolean = false,
    transitionAnimations: TransitionAnimations = TransitionAnimations.FADING_ANIMATIONS,
    destinationIntent: Intent = DashboardActivity.newInstance(context)
): Intent {
    return context.intentFor<LoginActivity>().apply {
        putExtra(ExtrasKeys.KEY_WAS_ACCOUNT_JUST_VERIFIED, wasAccountJustVerified)
        putExtra(ExtrasKeys.KEY_TRANSITION_ANIMATIONS, transitionAnimations)
        putExtra(ExtrasKeys.KEY_DESTINATION_INTENT, destinationIntent)
    }.clearTop().singleTop()
}