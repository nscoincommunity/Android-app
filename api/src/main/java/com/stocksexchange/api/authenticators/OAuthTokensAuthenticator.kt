package com.stocksexchange.api.authenticators

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.HttpCodes
import com.stocksexchange.api.model.HttpHeaders
import com.stocksexchange.core.model.Result
import com.stocksexchange.api.utils.CredentialsHandler
import com.stocksexchange.api.utils.extensions.responseCount
import okhttp3.*

/**
 * An authenticator used for refreshing OAuth tokens when they expire.
 */
class OAuthTokensAuthenticator(
    private val credentialsHandler: CredentialsHandler,
    private val stexRestApi: Lazy<StexRestApi>
) : Authenticator {


    override fun authenticate(route: Route?, response: Response): Request? {
        if((response.code() != HttpCodes.UNAUTHORIZED) ||
            (response.responseCount >= 3) ||
            !credentialsHandler.hasOAuthCredentials()) {
            return null
        }

        val oldOAuthCredentials = credentialsHandler.getOAuthCredentials()
        val newOAuthCredentialsResult = stexRestApi.value.getNewOAuthCredentials(oldOAuthCredentials.refreshToken)

        if(newOAuthCredentialsResult is Result.Failure) {
            return null
        }

        val newOAuthCredentials = (newOAuthCredentialsResult as Result.Success).value
        val adjustedNewOAuthCredentials = newOAuthCredentials.copy(
            refreshTokenExpirationTime = oldOAuthCredentials.refreshTokenExpirationTime
        )

        credentialsHandler.saveOAuthCredentials(adjustedNewOAuthCredentials)

        return response.request().newBuilder()
            .header(HttpHeaders.AUTHORIZATION, adjustedNewOAuthCredentials.authorizationHeader)
            .build()
    }


}