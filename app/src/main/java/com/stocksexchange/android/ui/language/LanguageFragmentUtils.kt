package com.stocksexchange.android.ui.language

import android.os.Bundle
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow
import java.io.Serializable


internal val fragmentStateExtractor: (Bundle.() -> FragmentState) = {
    FragmentState(
        items = getSerializableOrThrow(FragmentStateKeys.KEY_ITEMS)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        isToolbarRightButtonVisible = getOrThrow(PresenterStateKeys.KEY_IS_TOOLBAR_RIGHT_BUTTON_VISIBLE),
        newSettings = getOrThrow(PresenterStateKeys.KEY_NEW_SETTINGS)
    )
}


internal object FragmentStateKeys {

    const val KEY_ITEMS = "items"

}


internal object PresenterStateKeys {

    const val KEY_IS_TOOLBAR_RIGHT_BUTTON_VISIBLE = "is_toolbar_right_button_visible"
    const val KEY_NEW_SETTINGS = "new_settings"

}


internal data class FragmentState(
    val items: MutableList<LanguageItem>
)


internal data class PresenterState(
    val isToolbarRightButtonVisible: Boolean,
    val newSettings: Settings
)


internal fun Bundle.saveState(state: FragmentState) {
    putSerializable(FragmentStateKeys.KEY_ITEMS, (state.items as Serializable))
}


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_IS_TOOLBAR_RIGHT_BUTTON_VISIBLE, state.isToolbarRightButtonVisible)
    save(PresenterStateKeys.KEY_NEW_SETTINGS, state.newSettings)
}