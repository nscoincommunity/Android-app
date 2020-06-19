package com.stocksexchange.api.model.rest

import com.google.gson.annotations.SerializedName

data class ApiResponse<Data>(
    @SerializedName(JSON_FIELD_KEY_SUCCESS) val isSuccess: Boolean = false,
    @SerializedName(JSON_FIELD_KEY_DATA) val data: Data? = null,
    @SerializedName(JSON_FIELD_KEY_MESSAGE) private val _message: String? = null,
    @SerializedName(JSON_FIELD_KEY_ERRORS) private val _errorsMap: LinkedHashMap<String, List<String>>? = null
) {


    companion object {

        internal const val JSON_FIELD_KEY_SUCCESS = "success"
        internal const val JSON_FIELD_KEY_DATA = "data"
        internal const val JSON_FIELD_KEY_MESSAGE = "message"
        internal const val JSON_FIELD_KEY_ERRORS = "errors"

    }




    val hasData: Boolean
        get() = (data != null)


    val hasMessage: Boolean
        get() = ((_message != null) && message.isNotBlank())


    val hasErrors: Boolean
        get() = (_errorsMap != null)


    val message: String
        get() = (_message ?: "")


    val errorList: List<String>
        get() = mutableListOf<String>().apply {
            if(!hasErrors) {
                return@apply
            }

            for(entry in _errorsMap!!) {
                addAll(entry.value)
            }
        }


    val errorsMap: LinkedHashMap<String, List<String>>
        get() = _errorsMap ?: LinkedHashMap()


}