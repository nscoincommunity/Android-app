package com.stocksexchange.api.model.rest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class SignInConfirmation(
    @SerializedName(JSON_FIELD_KEY_MESSAGE) val message: String,
    @SerializedName(JSON_FIELD_KEY_CONFIRM_TYPE) val confirmationTypeStr: String
) {


    companion object {

        private const val JSON_FIELD_KEY_MESSAGE = "message"
        private const val JSON_FIELD_KEY_CONFIRM_TYPE = "confirm_type"


        fun newInstance(jsonObject: JsonObject): SignInConfirmation? {
            return if(jsonObject.has(JSON_FIELD_KEY_MESSAGE) && jsonObject.has(JSON_FIELD_KEY_CONFIRM_TYPE)) {
                SignInConfirmation(
                    message = jsonObject.get(JSON_FIELD_KEY_MESSAGE).asString,
                    confirmationTypeStr = jsonObject.get(JSON_FIELD_KEY_CONFIRM_TYPE).asString
                )
            } else {
                null
            }
        }

    }


    val confirmationType: SignInConfirmationType
        get() = SignInConfirmationType.newInstance(confirmationTypeStr)


}