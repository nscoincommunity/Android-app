package com.stocksexchange.android.ui.settings

import android.os.Bundle
import com.arthurivanets.adapster.model.BaseItem
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow
import java.io.Serializable


internal val fragmentStateExtractor: (Bundle.() -> FragmentState) = {
    FragmentState(
        items = getSerializableOrThrow(FragmentStateKeys.KEY_ITEMS)
    )
}


internal object FragmentStateKeys {

    const val KEY_ITEMS = "items"

}


internal data class FragmentState(
    val items: MutableList<BaseItem<*, *, *>>
)


internal fun Bundle.saveState(state: FragmentState) {
    putSerializable(FragmentStateKeys.KEY_ITEMS, (state.items as Serializable))
}