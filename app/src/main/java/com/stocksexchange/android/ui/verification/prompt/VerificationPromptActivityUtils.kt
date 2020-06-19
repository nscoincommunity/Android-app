package com.stocksexchange.android.ui.verification.prompt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.model.VerificationPromptDescriptionType
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow
import com.stocksexchange.core.utils.extensions.intentFor


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        descriptionType = getSerializableOrThrow(ExtrasKeys.KEY_DESCRIPTION_TYPE)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        descriptionType = getOrThrow(PresenterStateKeys.KEY_DESCRIPTION_TYPE)
    )
}


internal object ExtrasKeys {

    const val KEY_DESCRIPTION_TYPE = "description_type"

}


internal object PresenterStateKeys {

    const val KEY_DESCRIPTION_TYPE = "description_type"

}


internal data class Extras(
    val descriptionType: VerificationPromptDescriptionType
)


internal data class PresenterState(
    val descriptionType: VerificationPromptDescriptionType
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_DESCRIPTION_TYPE, state.descriptionType)
}


fun VerificationPromptActivity.Companion.newInstance(
    context: Context,
    descriptionType: VerificationPromptDescriptionType
): Intent {
    return context.intentFor<VerificationPromptActivity>().apply {
        putExtra(ExtrasKeys.KEY_DESCRIPTION_TYPE, descriptionType)
    }
}