package com.stocksexchange.api.model.rest

data class SignInResponse(
    val accountEmailVerification: AccountEmailVerification? = null,
    val loginConfirmation: SignInConfirmation? = null
) {


    val hasAccountEmailVerification: Boolean
        get() = (accountEmailVerification != null)


    val hasLoginConfirmation: Boolean
        get() = (loginConfirmation != null)


    val hasData: Boolean
        get() = (hasAccountEmailVerification || hasLoginConfirmation)


}