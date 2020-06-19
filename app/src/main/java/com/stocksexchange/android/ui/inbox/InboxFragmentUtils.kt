package com.stocksexchange.android.ui.inbox

import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.parameters.InboxParameters


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        inboxParameters = getOrThrow(PresenterStateKeys.KEY_INBOX_PARAMS)
    )
}


internal object PresenterStateKeys {

    const val KEY_INBOX_PARAMS = "inbox_params"

}


internal data class PresenterState(
    val inboxParameters: InboxParameters
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_INBOX_PARAMS, state.inboxParameters)
}