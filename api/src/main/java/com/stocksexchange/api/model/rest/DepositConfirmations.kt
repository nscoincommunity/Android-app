package com.stocksexchange.api.model.rest

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DepositConfirmations(
    val receivedConfirmationsNumber: Int,
    val totalConfirmationsNumber: Int
) : Parcelable {


    companion object {

        private const val CONFIRMATIONS_STRING_DELIMITER = ' '
        private const val CONFIRMATIONS_STRING_TOKENS_COUNT = 3


        fun newInstance(confirmationsStr: String): DepositConfirmations? {
            val tokens = confirmationsStr.split(CONFIRMATIONS_STRING_DELIMITER)

            return if((tokens.size != CONFIRMATIONS_STRING_TOKENS_COUNT) ||
                    (tokens.first().toIntOrNull() == null) ||
                    (tokens.last().toIntOrNull() == null)) {
                null
            } else {
                DepositConfirmations(
                    receivedConfirmationsNumber = tokens.first().toInt(),
                    totalConfirmationsNumber = tokens.last().toInt()
                )
            }
        }

    }


}