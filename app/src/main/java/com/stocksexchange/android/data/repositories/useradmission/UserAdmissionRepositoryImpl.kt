package com.stocksexchange.android.data.repositories.useradmission

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.stores.useradmission.UserAdmissionDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.api.model.rest.parameters.PasswordRecoveryParameters
import com.stocksexchange.api.model.rest.parameters.SignInParameters
import com.stocksexchange.api.model.rest.parameters.SignUpParameters
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class UserAdmissionRepositoryImpl(
    private val serverDataStore: UserAdmissionDataStore,
    private val connectionProvider: ConnectionProvider
) : UserAdmissionRepository {


    @Synchronized
    override suspend fun signUp(params: SignUpParameters): RepositoryResult<SignUpResponse> {
        return performTask {
            serverDataStore.signUp(params)
        }
    }


    @Synchronized
    override suspend fun sendAccountVerificationEmail(email: String): RepositoryResult<AccountVerificationEmailSendingResponse> {
        return performTask {
            serverDataStore.sendAccountVerificationEmail(email)
        }
    }


    @Synchronized
    override suspend fun verifyAccount(verificationToken: String): RepositoryResult<AccountVerificationResponse> {
        return performTask {
            serverDataStore.verifyAccount(verificationToken)
        }
    }


    @Synchronized
    override suspend fun sendPasswordResetEmail(email: String): RepositoryResult<PasswordResetEmailSendingResponse> {
        return performTask {
            serverDataStore.sendPasswordResetEmail(email)
        }
    }


    @Synchronized
    override suspend fun resetPassword(params: PasswordRecoveryParameters): RepositoryResult<PasswordResetResponse> {
        return performTask {
            serverDataStore.resetPassword(params)
        }
    }


    @Synchronized
    override suspend fun signIn(params: SignInParameters): RepositoryResult<SignInResponse> {
        return performTask {
            serverDataStore.signIn(params)
        }
    }


    @Synchronized
    override suspend fun confirmSignIn(params: SignInParameters): RepositoryResult<SignInConfirmationResponse> {
        return performTask {
            serverDataStore.confirmSignIn(params)
        }
    }


    @Synchronized
    override suspend fun getNewOAuthCredentials(refreshToken: String): RepositoryResult<OAuthCredentials> {
        return performTask {
            serverDataStore.getNewOAuthCredentials(refreshToken)
        }
    }


    private suspend fun <T> performTask(getServerResult: suspend (() -> Result<T>)): RepositoryResult<T> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        return RepositoryResult(serverResult = getServerResult())
    }


}