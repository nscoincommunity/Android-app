package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.parameters.PasswordRecoveryParameters
import com.stocksexchange.api.model.rest.parameters.SignInParameters
import com.stocksexchange.api.model.rest.parameters.SignUpParameters

interface UserAdmissionData<
    SignUpResult,
    AccountVerificationEmailSendingResult,
    AccountVerificationResult,
    PasswordResetEmailSendingResult,
    PasswordResetResult,
    SignInResult,
    SignInConfirmationResult,
    NewOAuthCredentialsResult
> {

    suspend fun signUp(params: SignUpParameters): SignUpResult

    suspend fun sendAccountVerificationEmail(email: String): AccountVerificationEmailSendingResult

    suspend fun verifyAccount(verificationToken: String): AccountVerificationResult

    suspend fun sendPasswordResetEmail(email: String): PasswordResetEmailSendingResult

    suspend fun resetPassword(params: PasswordRecoveryParameters): PasswordResetResult

    suspend fun signIn(params: SignInParameters): SignInResult

    suspend fun confirmSignIn(params: SignInParameters): SignInConfirmationResult

    suspend fun getNewOAuthCredentials(refreshToken: String): NewOAuthCredentialsResult

}