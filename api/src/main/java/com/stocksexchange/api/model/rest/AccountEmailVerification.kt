package com.stocksexchange.api.model.rest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class AccountEmailVerification(
    @SerializedName(JSON_FIELD_KEY_MESSAGE) val message: String
) {


    companion object {

        private const val JSON_FIELD_KEY_MESSAGE = "message"


        fun newInstance(jsonObject: JsonObject): AccountEmailVerification? {
            return if(jsonObject.has(JSON_FIELD_KEY_MESSAGE)) {
                AccountEmailVerification(jsonObject.get(JSON_FIELD_KEY_MESSAGE).asString)
            } else {
                null
            }
        }

    }


}