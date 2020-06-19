package com.stocksexchange.api.utils

import com.stocksexchange.api.model.rest.OAuthCredentials
import com.stocksexchange.core.handlers.PreferenceHandler

class CredentialsHandler(private val preferences: PreferenceHandler) {


    fun saveEmail(email: String) = preferences.saveEmail(email)


    fun saveOAuthCredentials(credentials: OAuthCredentials) {
        with(preferences) {
            saveTokenType(credentials.tokenType)
            saveAccessToken(credentials.accessToken)
            saveRefreshToken(credentials.refreshToken)
            saveAccessTokenExpirationTime(credentials.accessTokenExpirationTime)
            saveRefreshTokenExpirationTime(credentials.refreshTokenExpirationTime)
        }
    }


    fun clearEmail() = preferences.removeEmail()


    fun clearOAuthCredentials() {
        with(preferences) {
            removeTokenType()
            removeAccessToken()
            removeRefreshToken()
            removeAccessTokenExpirationTime()
            removeRefreshTokenExpirationTime()
        }
    }


    fun clearAuthenticationData() {
        with(preferences) {
            removeFingerprintAttemptsUsedUp()
            removeLastAuthTimestamp()
            removeInvalidPinCodeAttemptsNumber()
            removeAllowAuthTimestamp()
        }
    }


    fun clearUserData() {
        clearEmail()
        clearOAuthCredentials()
        clearAuthenticationData()
    }


    fun hasEmail() = preferences.hasEmail()


    fun hasOAuthCredentials(): Boolean {
        return (preferences.hasTokenType() &&
                preferences.hasAccessToken() &&
                preferences.hasRefreshToken() &&
                preferences.hasAccessTokenExpirationTime() &&
                preferences.hasRefreshTokenExpirationTime())
    }


    fun getEmail(): String = preferences.getEmail()


    fun getOAuthCredentials(): OAuthCredentials {
        return OAuthCredentials(
            tokenType = preferences.getTokenType(),
            accessToken = preferences.getAccessToken(),
            refreshToken = preferences.getRefreshToken(),
            accessTokenExpirationTime = preferences.getAccessTokenExpirationTime(),
            refreshTokenExpirationTime = preferences.getRefreshTokenExpirationTime()
        )
    }


}