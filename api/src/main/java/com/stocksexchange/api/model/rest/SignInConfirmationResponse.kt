package com.stocksexchange.api.model.rest

data class SignInConfirmationResponse(
    val oauthCredentials: OAuthCredentials? = null,
    val confirmation: SignInConfirmation? = null
) {


    val hasOauthCredentials: Boolean
        get() = (oauthCredentials != null)


    val hasConfirmation: Boolean
        get() = (confirmation != null)


    val hasData: Boolean
        get() = (hasOauthCredentials || hasConfirmation)


}