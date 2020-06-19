package com.stocksexchange.android.data.stores.useradmission

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rest.parameters.PasswordRecoveryParameters
import com.stocksexchange.api.model.rest.parameters.SignInParameters
import com.stocksexchange.api.model.rest.parameters.SignUpParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class UserAdmissionServerDataStore(
    private val stexRestApi: StexRestApi
) : UserAdmissionDataStore {


    override suspend fun signUp(params: SignUpParameters): Result<SignUpResponse> {
        return performBackgroundOperation {
            stexRestApi.signUp(params)
        }
    }


    override suspend fun sendAccountVerificationEmail(email: String): Result<AccountVerificationEmailSendingResponse> {
        return performBackgroundOperation {
            stexRestApi.sendAccountVerificationEmail(email)
        }
    }


    override suspend fun verifyAccount(verificationToken: String): Result<AccountVerificationResponse> {
        return performBackgroundOperation {
            stexRestApi.verifyAccount(verificationToken)
        }
    }


    override suspend fun sendPasswordResetEmail(email: String): Result<PasswordResetEmailSendingResponse> {
        return performBackgroundOperation {
            stexRestApi.sendPasswordResetEmail(email)
        }
    }


    override suspend fun resetPassword(params: PasswordRecoveryParameters): Result<PasswordResetResponse> {
        return performBackgroundOperation {
            stexRestApi.resetPassword(params)
        }
    }


    override suspend fun signIn(params: SignInParameters): Result<SignInResponse> {
        return performBackgroundOperation {
            stexRestApi.signIn(params)
        }
    }


    override suspend fun confirmSignIn(params: SignInParameters): Result<SignInConfirmationResponse> {
        return performBackgroundOperation {
            stexRestApi.confirmSignIn(params)
        }
    }


    override suspend fun getNewOAuthCredentials(refreshToken: String): Result<OAuthCredentials> {
        return performBackgroundOperation {
            stexRestApi.getNewOAuthCredentials(refreshToken)
        }
    }


}