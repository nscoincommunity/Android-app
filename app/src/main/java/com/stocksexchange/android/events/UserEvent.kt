package com.stocksexchange.android.events

import com.stocksexchange.android.utils.helpers.tag

class UserEvent private constructor(
    type: Int,
    sourceTag: String,
    val action: Action
) : BaseEvent<Void?>(type, null, sourceTag) {


    companion object {


        fun signOut(source: Any): UserEvent {
            return init(source, Action.SIGNED_OUT)
        }


        private fun init(source: Any, action: Action): UserEvent {
            return UserEvent(TYPE_SINGLE_ITEM, tag(source), action)
        }


    }


    enum class Action {

        SIGNED_OUT

    }


}