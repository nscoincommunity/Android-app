package com.stocksexchange.android.events

import com.stocksexchange.android.utils.helpers.tag

class ReloadWalletsEvent private constructor(
    type: Int,
    sourceTag: String,
    val action: Action
) : BaseEvent<Void?>(type, null, sourceTag) {


    companion object {

        fun reloadWallets(source: Any): ReloadWalletsEvent {
            return init(source, Action.WALLETS)
        }

        private fun init(source: Any, action: Action): ReloadWalletsEvent {
            return ReloadWalletsEvent(
                TYPE_SINGLE_ITEM,
                tag(source),
                action
            )
        }

    }


    enum class Action {

        WALLETS

    }

}