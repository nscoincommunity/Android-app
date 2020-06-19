package com.stocksexchange.android.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow
import com.stocksexchange.core.utils.extensions.intentFor


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
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
        transitionAnimations = getOrThrow(PresenterStateKeys.KEY_TRANSITION_ANIMATIONS)
    )
}


internal object ExtrasKeys {

    const val KEY_TRANSITION_ANIMATIONS = "transition_animations"
    const val KEY_DESTINATION_INTENT = "destination_intent"

}


internal object ActivityStateKeys {

    const val KEY_DESTINATION_INTENT = "destination_intent"

}


internal object PresenterStateKeys {

    const val KEY_TRANSITION_ANIMATIONS = "transition_animations"

}


internal data class Extras(
    val transitionAnimations: TransitionAnimations,
    val destinationIntent: Intent
)


internal data class ActivityState(
    val destinationIntent: Intent
)


internal data class PresenterState(
    val transitionAnimations: TransitionAnimations
)


internal fun Bundle.saveState(state: ActivityState) {
    putParcelable(ActivityStateKeys.KEY_DESTINATION_INTENT, state.destinationIntent)
}


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_TRANSITION_ANIMATIONS, state.transitionAnimations)
}


fun RegistrationActivity.Companion.newInstance(
    context: Context,
    transitionAnimations: TransitionAnimations = TransitionAnimations.FADING_ANIMATIONS,
    destinationIntent: Intent = DashboardActivity.newInstance(context)
): Intent {
    return context.intentFor<RegistrationActivity>().apply {
        putExtra(ExtrasKeys.KEY_TRANSITION_ANIMATIONS, transitionAnimations)
        putExtra(ExtrasKeys.KEY_DESTINATION_INTENT, destinationIntent)
    }
}