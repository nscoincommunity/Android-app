package com.stocksexchange.android.events

import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.utils.helpers.tag

class WalletEvent private constructor(
    type: Int,
    attachment: Wallet,
    sourceTag: String,
    val action: Action
) : BaseEvent<Wallet>(type, attachment, sourceTag) {


    companion object {


        fun createId(wallet: Wallet, source: Any): WalletEvent {
            return init(wallet, source, Action.ID_CREATED)
        }


        fun updateBalance(wallet: Wallet, source: Any): WalletEvent {
            return init(wallet, source, Action.BALANCE_UPDATED)
        }


        private fun init(wallet: Wallet, source: Any, action: Action): WalletEvent {
            return WalletEvent(
                TYPE_SINGLE_ITEM,
                wallet,
                tag(source),
                action
            )
        }


    }


    enum class Action {

        ID_CREATED,
        BALANCE_UPDATED
    }


}