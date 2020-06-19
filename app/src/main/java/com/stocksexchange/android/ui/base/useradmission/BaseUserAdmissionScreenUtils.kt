package com.stocksexchange.android.ui.base.useradmission

import android.os.Parcelable
import com.stocksexchange.android.utils.SavedState


internal val presenterStateExtractor: (SavedState.() -> PresenterState<*, *>) = {
    PresenterState(
        parameters = getOrThrow(PresenterStateKeys.KEY_PARAMETERS),
        processPhase = getOrThrow(PresenterStateKeys.KEY_PROCESS_PHASE)
    )
}


internal object PresenterStateKeys {

    const val KEY_PARAMETERS = "parameters"
    const val KEY_PROCESS_PHASE = "process_phase"

}


internal data class PresenterState<P : Parcelable, PP : Enum<*>>(
    val parameters: P,
    val processPhase: PP
)


internal fun <P : Parcelable, PP : Enum<*>> SavedState.saveState(state: PresenterState<P, PP>) {
    save(PresenterStateKeys.KEY_PARAMETERS, state.parameters)
    save(PresenterStateKeys.KEY_PROCESS_PHASE, state.processPhase)
}