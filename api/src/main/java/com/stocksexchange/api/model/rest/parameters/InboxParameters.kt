package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.core.utils.interfaces.HasUniqueKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InboxParameters(
    val limit: Int,
    val offset: Int
) : Parcelable, HasUniqueKey {


    companion object {

        fun getDefaultParameters(): InboxParameters {
            return InboxParameters(
                limit = 30,
                offset = 0
            )
        }

    }


    override val uniqueKey: String
        get() = (limit + offset).toString()




    fun resetOffset(): InboxParameters {
        return copy(offset = 0)
    }


    fun increaseOffset(value: Int): InboxParameters {
        return copy(offset = (offset + value))
    }


}