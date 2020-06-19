package com.stocksexchange.android.ui.passwordrecovery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.model.PasswordRecoveryProcessPhase
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.core.utils.extensions.clearTop
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow
import com.stocksexchange.core.utils.extensions.intentFor


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        passwordResetToken = getString(ExtrasKeys.KEY_PASSWORD_RESET_TOKEN, ""),
        processPhase = getSerializableOrThrow(ExtrasKeys.KEY_PROCESS_PHASE),
        transitionAnimations = getSerializableOrThrow(ExtrasKeys.KEY_TRANSITION_ANIMATIONS)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        passwordResetToken = getOrThrow(PresenterStateKeys.KEY_PASSWORD_RESET_TOKEN),
        transitionAnimations = getOrThrow(PresenterStateKeys.KEY_TRANSITION_ANIMATIONS)
    )
}


internal object ExtrasKeys {

    const val KEY_PASSWORD_RESET_TOKEN = "password_reset_token"
    const val KEY_PROCESS_PHASE = "process_phase"
    const val KEY_TRANSITION_ANIMATIONS = "transition_animations"

}


internal object PresenterStateKeys {

    const val KEY_PASSWORD_RESET_TOKEN = "password_reset_token"
    const val KEY_TRANSITION_ANIMATIONS = "transition_animations"

}


internal data class Extras(
    val passwordResetToken: String,
    val processPhase: PasswordRecoveryProcessPhase,
    val transitionAnimations: TransitionAnimations
)


internal data class PresenterState(
    val passwordResetToken: String,
    val transitionAnimations: TransitionAnimations
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_PASSWORD_RESET_TOKEN, state.passwordResetToken)
    save(PresenterStateKeys.KEY_TRANSITION_ANIMATIONS, state.transitionAnimations)
}


fun PasswordRecoveryActivity.Companion.newFirstPhaseInstance(
    context: Context,
    transitionAnimations: TransitionAnimations = TransitionAnimations.FADING_ANIMATIONS
) = newInstance(
    context = context,
    processPhase = PasswordRecoveryProcessPhase.AWAITING_EMAIL_ADDRESS,
    transitionAnimations = transitionAnimations,
    passwordResetToken = ""
)


fun PasswordRecoveryActivity.Companion.newSecondPhaseInstance(
    context: Context,
    passwordResetToken: String,
    transitionAnimations: TransitionAnimations = TransitionAnimations.FADING_ANIMATIONS
) = newInstance(
    context = context,
    processPhase = PasswordRecoveryProcessPhase.AWAITING_NEW_PASSWORD,
    transitionAnimations = transitionAnimations,
    passwordResetToken = passwordResetToken
)


fun PasswordRecoveryActivity.Companion.newInstance(
    context: Context,
    processPhase: PasswordRecoveryProcessPhase,
    transitionAnimations: TransitionAnimations,
    passwordResetToken: String
): Intent {
    return context.intentFor<PasswordRecoveryActivity>().apply {
        putExtra(ExtrasKeys.KEY_PROCESS_PHASE, processPhase)
        putExtra(ExtrasKeys.KEY_TRANSITION_ANIMATIONS, transitionAnimations)
        putExtra(ExtrasKeys.KEY_PASSWORD_RESET_TOKEN, passwordResetToken)
    }.clearTop()
}