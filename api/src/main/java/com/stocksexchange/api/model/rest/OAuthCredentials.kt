package com.stocksexchange.api.model.rest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class OAuthCredentials(
    @SerializedName(JSON_FIELD_KEY_TOKEN_TYPE) val tokenType: String,
    @SerializedName(JSON_FIELD_KEY_ACCESS_TOKEN) val accessToken: String,
    @SerializedName(JSON_FIELD_KEY_REFRESH_TOKEN) val refreshToken: String,
    @SerializedName(JSON_FIELD_KEY_ACCESS_TOKEN_EXPIRATION_TIME) val accessTokenExpirationTime: Long, // in seconds
    @SerializedName(JSON_FIELD_KEY_REFRESH_TOKEN_EXPIRATION_TIME) val refreshTokenExpirationTime: Long  // in seconds
) {

    companion object {

        private const val JSON_FIELD_KEY_TOKEN_TYPE = "token_type"
        private const val JSON_FIELD_KEY_ACCESS_TOKEN = "access_token"
        private const val JSON_FIELD_KEY_REFRESH_TOKEN = "refresh_token"
        private const val JSON_FIELD_KEY_ACCESS_TOKEN_EXPIRATION_TIME = "expires_at"
        private const val JSON_FIELD_KEY_REFRESH_TOKEN_EXPIRATION_TIME = "refresh_expires_at"


        fun newInstance(jsonObject: JsonObject): OAuthCredentials? {
            val hasTokenType = jsonObject.has(JSON_FIELD_KEY_TOKEN_TYPE)
            val hasAccessToken = jsonObject.has(JSON_FIELD_KEY_ACCESS_TOKEN)
            val hasRefreshToken = jsonObject.has(JSON_FIELD_KEY_REFRESH_TOKEN)
            val hasAccessTokenExpirationTime = jsonObject.has(JSON_FIELD_KEY_ACCESS_TOKEN_EXPIRATION_TIME)
            val hasRefreshTokenExpirationTime = jsonObject.has(JSON_FIELD_KEY_REFRESH_TOKEN_EXPIRATION_TIME)
            val hasData = (
                hasTokenType &&
                hasAccessToken &&
                hasRefreshToken &&
                hasAccessTokenExpirationTime &&
                hasRefreshTokenExpirationTime
            )

            return if(hasData) {
                OAuthCredentials(
                    tokenType = jsonObject.get(JSON_FIELD_KEY_TOKEN_TYPE).asString,
                    accessToken = jsonObject.get(JSON_FIELD_KEY_ACCESS_TOKEN).asString,
                    refreshToken = jsonObject.get(JSON_FIELD_KEY_REFRESH_TOKEN).asString,
                    accessTokenExpirationTime = jsonObject.get(JSON_FIELD_KEY_ACCESS_TOKEN_EXPIRATION_TIME).asLong,
                    refreshTokenExpirationTime = jsonObject.get(JSON_FIELD_KEY_REFRESH_TOKEN_EXPIRATION_TIME).asLong
                )
            } else {
                null
            }
        }

    }


    val isAccessTokenExpired: Boolean
        get() = (System.currentTimeMillis() >= accessTokenExpirationTimeInMillis)


    val isRefreshTokenExpired: Boolean
        get() = (System.currentTimeMillis() >= refreshTokenExpirationTimeInMillis)


    val accessTokenExpirationTimeInMillis: Long
        get() = (accessTokenExpirationTime * 1000L)


    val refreshTokenExpirationTimeInMillis: Long
        get() = (refreshTokenExpirationTime * 1000L)


    val authorizationHeader: String
        get() = "$tokenType $accessToken"


}